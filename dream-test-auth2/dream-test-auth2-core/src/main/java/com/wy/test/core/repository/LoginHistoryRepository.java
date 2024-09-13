package com.wy.test.core.repository;

import java.sql.Types;

import org.springframework.jdbc.core.JdbcTemplate;

import com.wy.test.core.entity.HistoryLoginEntity;
import com.wy.test.core.web.AuthWebContext;

public class LoginHistoryRepository {

	private static final String HISTORY_LOGIN_INSERT_STATEMENT =
			"insert into auth_history_login (id , sessionid , userid , username , displayname , logintype , message , code , provider , sourceip , ipregion , iplocation, browser , platform , application , loginurl , sessionstatus ,instid)values( ? , ? , ? , ? , ? , ? , ? , ? , ?, ? , ? , ?, ? , ? , ?, ? , ? , ?)";

	protected JdbcTemplate jdbcTemplate;

	public LoginHistoryRepository(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	public void login(HistoryLoginEntity historyLogin) {
		historyLogin.setId(AuthWebContext.genId());
		historyLogin.setLoginUrl(AuthWebContext.getRequest().getRequestURI());
		// Thread insert
		new Thread(new HistoryLoginRunnable(jdbcTemplate, historyLogin)).start();
	}

	public class HistoryLoginRunnable implements Runnable {

		JdbcTemplate jdbcTemplate;

		HistoryLoginEntity historyLogin;

		public HistoryLoginRunnable(JdbcTemplate jdbcTemplate, HistoryLoginEntity historyLogin) {
			super();
			this.jdbcTemplate = jdbcTemplate;
			this.historyLogin = historyLogin;
		}

		@Override
		public void run() {
			jdbcTemplate.update(HISTORY_LOGIN_INSERT_STATEMENT,
					new Object[] { historyLogin.getId(), historyLogin.getSessionId(), historyLogin.getUserId(),
							historyLogin.getUsername(), historyLogin.getDisplayName(), historyLogin.getLoginType(),
							historyLogin.getMessage(), historyLogin.getCode(), historyLogin.getProvider(),
							historyLogin.getSourceIp(), historyLogin.getIpRegion(), historyLogin.getIpLocation(),
							historyLogin.getBrowser(), historyLogin.getPlatform(), "Browser",
							historyLogin.getLoginUrl(), historyLogin.getSessionStatus(), historyLogin.getInstId() },
					new int[] { Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.VARCHAR,
							Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.VARCHAR,
							Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.INTEGER,
							Types.VARCHAR });
		}
	}
}