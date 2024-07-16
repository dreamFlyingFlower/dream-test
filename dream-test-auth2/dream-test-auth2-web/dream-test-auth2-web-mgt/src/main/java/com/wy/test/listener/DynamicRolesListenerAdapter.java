package com.wy.test.listener;

import java.io.Serializable;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.wy.test.persistence.service.RolesService;

public class DynamicRolesListenerAdapter extends ListenerAdapter implements Job, Serializable {

	final static Logger _logger = LoggerFactory.getLogger(DynamicRolesListenerAdapter.class);

	private static final long serialVersionUID = 8831626240807856084L;

	private static RolesService rolesService = null;

	@Override
	public void execute(JobExecutionContext context) {
		if (jobStatus == JOBSTATUS.RUNNING) {
			return;
		}

		init(context);

		_logger.debug("running ... ");
		jobStatus = JOBSTATUS.RUNNING;
		try {
			if (rolesService != null) {
				rolesService.refreshAllDynamicRoles();
				Thread.sleep(10 * 1000);// 10 minutes
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
		if (rolesService == null) {
			rolesService = getParameter("rolesService", RolesService.class);
		}
	}

}
