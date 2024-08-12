package com.wy.test.authentication.provider.authn.support.rememberme;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import com.wy.test.authentication.core.authn.jwt.AuthTokenService;
import com.wy.test.core.properties.DreamAuthLoginProperties;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class JdbcRemeberMeManager extends AbstractRemeberMeManager {

	private static final String DEFAULT_DEFAULT_INSERT_STATEMENT =
			"insert into  auth_remember_me(id, userid,username,lastlogintime,expirationtime)values( ? , ? , ? , ? , ?)";

	private static final String DEFAULT_DEFAULT_SELECT_STATEMENT =
			"select id, userid,username,lastlogintime,expirationtime  from auth_remember_me "
					+ " where id = ?  and username = ?";

	private static final String DEFAULT_DEFAULT_DELETE_STATEMENT = "delete from  auth_remember_me where  username = ?";

	private static final String DEFAULT_DEFAULT_UPDATE_STATEMENT =
			"update auth_remember_me  set  lastlogintime = ? , expirationtime = ?  where id = ?";

	private final JdbcTemplate jdbcTemplate;

	public JdbcRemeberMeManager(JdbcTemplate jdbcTemplate, DreamAuthLoginProperties dreamLoginProperties,
			AuthTokenService authTokenService, Long validity) {
		this.jdbcTemplate = jdbcTemplate;
		this.dreamLoginProperties = dreamLoginProperties;
		this.authTokenService = authTokenService;
		if (validity != 0) {
			this.validity = validity;
		}
	}

	@Override
	public void save(RemeberMe remeberMe) {
		jdbcTemplate.update(DEFAULT_DEFAULT_INSERT_STATEMENT,
				new Object[] { remeberMe.getId(), remeberMe.getUserId(), remeberMe.getUsername(),
						remeberMe.getLastLoginTime(), remeberMe.getExpirationTime() },
				new int[] { Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.TIMESTAMP, Types.TIMESTAMP });
	}

	@Override
	public void update(RemeberMe remeberMe) {
		jdbcTemplate.update(DEFAULT_DEFAULT_UPDATE_STATEMENT,
				new Object[] { remeberMe.getLastLoginTime(), remeberMe.getExpirationTime(), remeberMe.getId() });
	}

	@Override
	public RemeberMe read(RemeberMe remeberMe) {
		List<RemeberMe> listRemeberMe =
				jdbcTemplate.query(DEFAULT_DEFAULT_SELECT_STATEMENT, new RowMapper<RemeberMe>() {

					@Override
					public RemeberMe mapRow(ResultSet rs, int rowNum) throws SQLException {
						RemeberMe remeberMe = new RemeberMe();
						remeberMe.setId(rs.getString(1));
						remeberMe.setUserId(rs.getString(2));
						remeberMe.setUsername(rs.getString(3));
						remeberMe.setLastLoginTime(rs.getDate(4));
						return remeberMe;
					}
				}, remeberMe.getId(), remeberMe.getUsername());
		log.debug("listRemeberMe " + listRemeberMe);
		return (listRemeberMe.size() > 0) ? listRemeberMe.get(0) : null;
	}

	@Override
	public void remove(String username) {
		jdbcTemplate.update(DEFAULT_DEFAULT_DELETE_STATEMENT, username);
	}
}