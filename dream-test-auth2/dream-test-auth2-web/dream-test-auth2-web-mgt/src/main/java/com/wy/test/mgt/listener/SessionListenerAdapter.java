package com.wy.test.mgt.listener;

import java.io.Serializable;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.wy.test.core.authn.session.Session;
import com.wy.test.core.authn.session.SessionManager;
import com.wy.test.core.entity.HistoryLoginEntity;

import dream.flying.flower.helper.DateTimeHelper;

public class SessionListenerAdapter extends ListenerAdapter implements Job, Serializable {

	final static Logger _logger = LoggerFactory.getLogger(SessionListenerAdapter.class);

	private static final long serialVersionUID = 4782358765969474833L;

	SessionManager sessionManager;

	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		if (jobStatus == JOBSTATUS.RUNNING) {
			return;
		}
		init(context);

		_logger.debug("running ... ");
		jobStatus = JOBSTATUS.RUNNING;
		try {
			if (sessionManager != null) {
				int sessionCount = 0;
				for (HistoryLoginEntity login : sessionManager.querySessions()) {
					Session session = sessionManager.get(login.getSessionId());
					if (session == null) {
						_logger.debug("user {} session {}  Login at {} and TimeOut at {} .", login.getUsername(),
								login.getId(), login.getLoginTime(), DateTimeHelper.formatDateTime());
						sessionManager.terminate(login.getSessionId(), login.getUserId(), login.getUsername());
					} else {
						_logger.debug("user {} session {} Login at {} , Last Access at {} will Expired at {}.",
								login.getUsername(), login.getId(), session.getStartTimestamp(),
								session.getLastAccessTime(), session.getExpiredTime());
						sessionCount++;
					}
				}
				_logger.debug("current session count {} .", sessionCount);
			}
			_logger.debug("finished  ");
			jobStatus = JOBSTATUS.FINISHED;
		} catch (Exception e) {
			jobStatus = JOBSTATUS.ERROR;
			_logger.error("Exception ", e);
		}

	}

	@Override
	void init(JobExecutionContext context) {
		super.init(context);
		if (sessionManager == null) {
			sessionManager = getParameter("sessionManager", SessionManager.class);
		}
	}
}
