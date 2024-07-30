package com.wy.test.autoconfigure;

import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.core.JdbcTemplate;

import com.wy.test.core.authn.session.SessionManager;
import com.wy.test.core.properties.DreamAuthJobProperties;
import com.wy.test.core.properties.DreamAuthServerProperties;
import com.wy.test.listener.DynamicRolesListenerAdapter;
import com.wy.test.listener.ListenerAdapter;
import com.wy.test.listener.ListenerParameter;
import com.wy.test.listener.SessionListenerAdapter;
import com.wy.test.persistence.provision.thread.ProvisioningRunner;
import com.wy.test.persistence.provision.thread.ProvisioningRunnerThread;
import com.wy.test.persistence.service.ConnectorsService;
import com.wy.test.persistence.service.RolesService;

import lombok.extern.slf4j.Slf4j;

@AutoConfiguration
@Slf4j
public class DreamMgtListenerConfig implements InitializingBean {

	@Bean
	String sessionListenerAdapter(Scheduler scheduler, SessionManager sessionManager) throws SchedulerException {
		ListenerAdapter.addListener(SessionListenerAdapter.class, scheduler,
				new ListenerParameter().add("sessionManager", sessionManager).build(), "0 0/10 * * * ?", // 10 minutes
				SessionListenerAdapter.class.getSimpleName());
		log.debug("Session ListenerAdapter inited .");
		return "sessionListenerAdapter";
	}

	@Bean
	String dynamicRolesListenerAdapter(Scheduler scheduler, RolesService rolesService,
			DreamAuthJobProperties dreamAuthJobProperties) throws SchedulerException {

		ListenerAdapter.addListener(DynamicRolesListenerAdapter.class, scheduler,
				new ListenerParameter().add("rolesService", rolesService).build(),
				dreamAuthJobProperties.getCron().getSchedule(), DynamicRolesListenerAdapter.class.getSimpleName());
		log.debug("DynamicRoles ListenerAdapter inited .");
		return "dynamicRolesListenerAdapter";
	}

	@Bean
	String provisioningRunnerThread(ConnectorsService connectorsService, JdbcTemplate jdbcTemplate,
			DreamAuthServerProperties dreamServerProperties) throws SchedulerException {
		if (dreamServerProperties.isProvision()) {
			ProvisioningRunner runner = new ProvisioningRunner(connectorsService, jdbcTemplate);
			ProvisioningRunnerThread runnerThread = new ProvisioningRunnerThread(runner);
			runnerThread.start();
			log.debug("provisioning Runner Thread .");
		} else {
			log.debug("not need init provisioning Runner Thread .");
		}
		return "provisioningRunnerThread";
	}

	@Override
	public void afterPropertiesSet() throws Exception {

	}
}