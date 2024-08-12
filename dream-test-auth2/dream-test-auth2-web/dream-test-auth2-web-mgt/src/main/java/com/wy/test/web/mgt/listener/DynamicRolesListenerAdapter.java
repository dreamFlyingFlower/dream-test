package com.wy.test.web.mgt.listener;

import java.io.Serializable;

import org.quartz.Job;
import org.quartz.JobExecutionContext;

import com.wy.test.persistence.service.RoleService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DynamicRolesListenerAdapter extends ListenerAdapter implements Job, Serializable {

	private static final long serialVersionUID = 8831626240807856084L;

	private static RoleService roleService;

	@Override
	public void execute(JobExecutionContext context) {
		if (jobStatus == JOBSTATUS.RUNNING) {
			return;
		}

		init(context);

		log.debug("running ... ");
		jobStatus = JOBSTATUS.RUNNING;
		try {
			if (roleService != null) {
				roleService.refreshAllDynamicRoles();
				Thread.sleep(10 * 1000);// 10 minutes
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
		if (roleService == null) {
			roleService = getParameter("roleService", RoleService.class);
		}
	}
}