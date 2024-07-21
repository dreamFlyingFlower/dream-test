package com.wy.test.core.authn.session;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import com.wy.test.common.util.DateUtils;
import com.wy.test.core.constants.ConstsPersistence;
import com.wy.test.core.entity.HistoryLogin;
import com.wy.test.core.entity.UserInfo;
import com.wy.test.core.persistence.redis.RedisConnectionFactory;

/**
 * SessionManager Level 1 in memory,store in Caffeine Level 2 in Redis user
 * session status in database
 * 
 * @author shimh
 *
 */
public class SessionManagerFactory implements SessionManager {

	private static final Logger _logger = LoggerFactory.getLogger(SessionManagerFactory.class);

	private static final String DEFAULT_DEFAULT_SELECT_STATEMENT =
			"select id,sessionid,userId,username,displayname,logintime from mxk_history_login where sessionstatus = 1";

	private static final String LOGOUT_USERINFO_UPDATE_STATEMENT =
			"update mxk_userinfo set lastlogofftime = ? , online = " + UserInfo.ONLINE.OFFLINE + "  where id = ?";

	private static final String HISTORY_LOGOUT_UPDATE_STATEMENT =
			"update mxk_history_login set logouttime = ? ,sessionstatus = 7 where  sessionid = ?";

	private static final String NO_SESSION_UPDATE_STATEMENT =
			"update mxk_history_login set sessionstatus = 7 where sessionstatus = 1 and (sessionid is null or sessionid = '')";

	private JdbcTemplate jdbcTemplate;

	private InMemorySessionManager inMemorySessionManager;

	private RedisSessionManager redisSessionManager;

	private boolean isRedis = false;

	private int validitySeconds;

	public SessionManagerFactory(int persistence, JdbcTemplate jdbcTemplate, RedisConnectionFactory redisConnFactory,
			int validitySeconds) {
		this.validitySeconds = validitySeconds;
		this.jdbcTemplate = jdbcTemplate;
		this.inMemorySessionManager = new InMemorySessionManager(validitySeconds);
		_logger.debug("InMemorySessionManager");
		if (persistence == ConstsPersistence.REDIS) {
			isRedis = true;
			this.redisSessionManager = new RedisSessionManager(redisConnFactory, validitySeconds);
			_logger.debug("RedisSessionManager");
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
	public List<HistoryLogin> querySessions() {
		// clear session id is null
		jdbcTemplate.execute(NO_SESSION_UPDATE_STATEMENT);
		// query on line session
		List<HistoryLogin> listSessions =
				jdbcTemplate.query(DEFAULT_DEFAULT_SELECT_STATEMENT, new OnlineTicketRowMapper());
		return listSessions;
	}

	private void profileLastLogoffTime(String userId, String lastLogoffTime) {
		_logger.trace("userId {} , lastlogofftime {}", userId, lastLogoffTime);
		jdbcTemplate.update(LOGOUT_USERINFO_UPDATE_STATEMENT, new Object[] { lastLogoffTime, userId },
				new int[] { Types.TIMESTAMP, Types.VARCHAR });
	}

	private void sessionLogoff(String sessionId, String lastLogoffTime) {
		_logger.trace("sessionId {} , lastlogofftime {}", sessionId, lastLogoffTime);
		jdbcTemplate.update(HISTORY_LOGOUT_UPDATE_STATEMENT, new Object[] { lastLogoffTime, sessionId },
				new int[] { Types.VARCHAR, Types.VARCHAR });
	}

	@Override
	public void terminate(String sessionId, String userId, String username) {
		String lastLogoffTime = DateUtils.formatDateTime(new Date());
		_logger.trace("{} user {} terminate session {} .", lastLogoffTime, username, sessionId);
		this.profileLastLogoffTime(userId, lastLogoffTime);
		this.sessionLogoff(sessionId, lastLogoffTime);
		this.remove(sessionId);
	}

	@Override
	public int getValiditySeconds() {
		return validitySeconds;
	}

	private final class OnlineTicketRowMapper implements RowMapper<HistoryLogin> {

		@Override
		public HistoryLogin mapRow(ResultSet rs, int rowNum) throws SQLException {
			HistoryLogin history = new HistoryLogin();
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
