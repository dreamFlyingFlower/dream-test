package com.wy.test.mgt.autoconfigure;

import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.core.JdbcTemplate;

import com.wy.test.core.authn.session.SessionManager;
import com.wy.test.core.properties.DreamAuthJobProperties;
import com.wy.test.core.properties.DreamAuthServerProperties;
import com.wy.test.mgt.listener.DynamicRolesListenerAdapter;
import com.wy.test.mgt.listener.ListenerAdapter;
import com.wy.test.mgt.listener.ListenerParameter;
import com.wy.test.mgt.listener.SessionListenerAdapter;
import com.wy.test.persistence.provision.thread.ProvisioningRunner;
import com.wy.test.persistence.provision.thread.ProvisioningRunnerThread;
import com.wy.test.persistence.service.ConnectorService;
import com.wy.test.persistence.service.RoleService;

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
	String dynamicRolesListenerAdapter(Scheduler scheduler, RoleService roleService,
			DreamAuthJobProperties dreamAuthJobProperties) throws SchedulerException {

		ListenerAdapter.addListener(DynamicRolesListenerAdapter.class, scheduler,
				new ListenerParameter().add("roleService", roleService).build(),
				dreamAuthJobProperties.getCron().getSchedule(), DynamicRolesListenerAdapter.class.getSimpleName());
		log.debug("DynamicRoles ListenerAdapter inited .");
		return "dynamicRolesListenerAdapter";
	}

	@Bean
	String provisioningRunnerThread(ConnectorService connectorService, JdbcTemplate jdbcTemplate,
			DreamAuthServerProperties dreamServerProperties) throws SchedulerException {
		if (dreamServerProperties.isProvision()) {
			ProvisioningRunner runner = new ProvisioningRunner(connectorService, jdbcTemplate);
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