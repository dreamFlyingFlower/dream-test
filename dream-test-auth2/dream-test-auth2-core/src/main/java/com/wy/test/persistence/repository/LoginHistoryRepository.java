package com.wy.test.persistence.repository;

import java.sql.Types;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;

import com.wy.test.entity.HistoryLogin;
import com.wy.test.web.WebContext;

public class LoginHistoryRepository {

	private static Logger _logger = LoggerFactory.getLogger(LoginHistoryRepository.class);

	private static final String HISTORY_LOGIN_INSERT_STATEMENT =
			"insert into mxk_history_login (id , sessionid , userid , username , displayname , logintype , message , code , provider , sourceip , ipregion , iplocation, browser , platform , application , loginurl , sessionstatus ,instid)values( ? , ? , ? , ? , ? , ? , ? , ? , ?, ? , ? , ?, ? , ? , ?, ? , ? , ?)";

	protected JdbcTemplate jdbcTemplate;

	public LoginHistoryRepository(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	public void login(HistoryLogin historyLogin) {
		historyLogin.setId(WebContext.genId());
		historyLogin.setLoginUrl(WebContext.getRequest().getRequestURI());
		_logger.debug(" historyLogin " + historyLogin);
		jdbcTemplate.update(HISTORY_LOGIN_INSERT_STATEMENT,
				new Object[] { historyLogin.getId(), historyLogin.getSessionId(), historyLogin.getUserId(),
						historyLogin.getUsername(), historyLogin.getDisplayName(), historyLogin.getLoginType(),
						historyLogin.getMessage(), historyLogin.getCode(), historyLogin.getProvider(),
						historyLogin.getSourceIp(), historyLogin.getIpRegion(), historyLogin.getIpLocation(),
						historyLogin.getBrowser(), historyLogin.getPlatform(), "Browser", historyLogin.getLoginUrl(),
						historyLogin.getSessionStatus(), historyLogin.getInstId() },
				new int[] { Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.VARCHAR,
						Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.VARCHAR,
						Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.INTEGER, Types.VARCHAR });
	}

}
