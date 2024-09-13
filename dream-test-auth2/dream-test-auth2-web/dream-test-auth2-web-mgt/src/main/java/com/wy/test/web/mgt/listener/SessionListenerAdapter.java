package com.wy.test.web.mgt.listener;

import java.io.Serializable;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.wy.test.authentication.core.session.Session;
import com.wy.test.authentication.core.session.SessionManager;
import com.wy.test.core.entity.HistoryLoginEntity;

import dream.flying.flower.helper.DateTimeHelper;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SessionListenerAdapter extends ListenerAdapter implements Job, Serializable {

	private static final long serialVersionUID = 4782358765969474833L;

	SessionManager sessionManager;

	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		if (jobStatus == JOBSTATUS.RUNNING) {
			return;
		}
		init(context);

		log.debug("running ... ");
		jobStatus = JOBSTATUS.RUNNING;
		try {
			if (sessionManager != null) {
				int sessionCount = 0;
				for (HistoryLoginEntity login : sessionManager.querySessions()) {
					Session session = sessionManager.get(login.getSessionId());
					if (session == null) {
						log.debug("user {} session {}  Login at {} and TimeOut at {} .", login.getUsername(),
								login.getId(), login.getLoginTime(), DateTimeHelper.formatDateTime());
						sessionManager.terminate(login.getSessionId(), login.getUserId(), login.getUsername());
					} else {
						log.debug("user {} session {} Login at {} , Last Access at {} will Expired at {}.",
								login.getUsername(), login.getId(), session.getStartTimestamp(),
								session.getLastAccessTime(), session.getExpiredTime());
						sessionCount++;
					}
				}
				log.debug("current session count {} .", sessionCount);
			}
			log.debug("finished  ");
			jobStatus = JOBSTATUS.FINISHED;
		} catch (Exception e) {
			jobStatus = JOBSTATUS.ERROR;
			log.error("Exception ", e);
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