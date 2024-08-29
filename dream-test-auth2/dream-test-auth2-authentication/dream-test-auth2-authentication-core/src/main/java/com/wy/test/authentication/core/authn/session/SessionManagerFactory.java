package com.wy.test.authentication.core.authn.session;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import com.wy.test.core.entity.HistoryLoginEntity;
import com.wy.test.core.enums.StoreType;
import com.wy.test.core.persistence.redis.RedisConnectionFactory;

import dream.flying.flower.enums.YesNoEnum;
import dream.flying.flower.helper.DateTimeHelper;
import lombok.extern.slf4j.Slf4j;

/**
 * SessionManager Level 1 in memory,store in Caffeine Level 2 in Redis user session status in database
 */
@Slf4j
public class SessionManagerFactory implements SessionManager {

	private static final String DEFAULT_DEFAULT_SELECT_STATEMENT =
			"select id,session_id,user_id,username,display_name,login_time from auth_history_login where session_status = 1";

	private static final String LOGOUT_USERINFO_UPDATE_STATEMENT =
			"update auth_user set last_logoff_time = ? , online = " + YesNoEnum.NO.getCode() + "  where id = ?";

	private static final String HISTORY_LOGOUT_UPDATE_STATEMENT =
			"update auth_history_login set logout_time = ? ,session_status = 7 where  session_id = ?";

	private static final String NO_SESSION_UPDATE_STATEMENT =
			"update auth_history_login set session_status = 7 where session_status = 1 and (session_id is null or session_id = '')";

	private JdbcTemplate jdbcTemplate;

	private InMemorySessionManager inMemorySessionManager;

	private RedisSessionManager redisSessionManager;

	private boolean isRedis = false;

	private int validitySeconds;

	public SessionManagerFactory(StoreType storeType, JdbcTemplate jdbcTemplate,
			RedisConnectionFactory redisConnFactory, int validitySeconds) {
		this.validitySeconds = validitySeconds;
		this.jdbcTemplate = jdbcTemplate;
		this.inMemorySessionManager = new InMemorySessionManager(validitySeconds);
		log.debug("InMemorySessionManager");
		if (storeType == StoreType.REDIS) {
			isRedis = true;
			this.redisSessionManager = new RedisSessionManager(redisConnFactory, validitySeconds);
			log.debug("RedisSessionManager");
		}
	}

	@Override
	public void create(String sessionId, Session session) {
		inMemorySessionManager.create(sessionId, session);
		if (isRedis) {
			redisSessionManager.create(sessionId, session);
		}
	}

	@Override
	public Session remove(String sessionId) {
		Session session = inMemorySessionManager.remove(sessionId);
		if (isRedis) {
			session = redisSessionManager.remove(sessionId);
		}
		return session;
	}

	@Override
	public Session get(String sessionId) {
		Session session = inMemorySessionManager.get(sessionId);
		if (session == null && isRedis) {
			session = redisSessionManager.get(sessionId);
		}
		return session;
	}

	@Override
	public Session refresh(String sessionId, LocalDateTime refreshTime) {
		Session session = null;
		if (isRedis) {
			session = redisSessionManager.refresh(sessionId, refreshTime);
			// renew one in Memory
			inMemorySessionManager.create(sessionId, session);
		} else {
			session = inMemorySessionManager.refresh(sessionId, refreshTime);
		}
		return session;
	}

	@Override
	public Session refresh(String sessionId) {
		Session session = null;
		if (isRedis) {
			session = redisSessionManager.refresh(sessionId);
			// renew one
			inMemorySessionManager.remove(sessionId);
			inMemorySessionManager.create(sessionId, session);
		} else {
			session = inMemorySessionManager.refresh(sessionId);
		}

		return session;
	}

	@Override
	public List<HistoryLoginEntity> querySessions() {
		// clear session id is null
		jdbcTemplate.execute(NO_SESSION_UPDATE_STATEMENT);
		// query on line session
		List<HistoryLoginEntity> listSessions =
				jdbcTemplate.query(DEFAULT_DEFAULT_SELECT_STATEMENT, new OnlineTicketRowMapper());
		return listSessions;
	}

	private void profileLastLogoffTime(String userId, String lastLogoffTime) {
		log.trace("userId {} , lastlogofftime {}", userId, lastLogoffTime);
		jdbcTemplate.update(LOGOUT_USERINFO_UPDATE_STATEMENT, new Object[] { lastLogoffTime, userId },
				new int[] { Types.TIMESTAMP, Types.VARCHAR });
	}

	private void sessionLogoff(String sessionId, String lastLogoffTime) {
		log.trace("sessionId {} , lastlogofftime {}", sessionId, lastLogoffTime);
		jdbcTemplate.update(HISTORY_LOGOUT_UPDATE_STATEMENT, new Object[] { lastLogoffTime, sessionId },
				new int[] { Types.VARCHAR, Types.VARCHAR });
	}

	@Override
	public void terminate(String sessionId, String userId, String username) {
		String lastLogoffTime = DateTimeHelper.formatDateTime();
		log.trace("{} user {} terminate session {} .", lastLogoffTime, username, sessionId);
		this.profileLastLogoffTime(userId, lastLogoffTime);
		this.sessionLogoff(sessionId, lastLogoffTime);
		this.remove(sessionId);
	}

	@Override
	public int getValiditySeconds() {
		return validitySeconds;
	}

	private final class OnlineTicketRowMapper implements RowMapper<HistoryLoginEntity> {

		@Override
		public HistoryLoginEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
			HistoryLoginEntity history = new HistoryLoginEntity();
			history.setId(rs.getString(1));
			history.setSessionId(rs.getString(2));
			history.setUserId(rs.getString(3));
			history.setUsername(rs.getString(4));
			history.setDisplayName(rs.getString(5));
			history.setLoginTime(rs.getString(6));
			return history;
		}
	}
}
