package com.wy.test.autoconfigure;

import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.core.JdbcTemplate;

import com.wy.test.core.authn.session.SessionManager;
import com.wy.test.core.properties.DreamServerProperties;
import com.wy.test.listener.DynamicRolesListenerAdapter;
import com.wy.test.listener.ListenerAdapter;
import com.wy.test.listener.ListenerParameter;
import com.wy.test.listener.SessionListenerAdapter;
import com.wy.test.persistence.provision.thread.ProvisioningRunner;
import com.wy.test.persistence.provision.thread.ProvisioningRunnerThread;
import com.wy.test.persistence.service.ConnectorsService;
import com.wy.test.persistence.service.RolesService;

@AutoConfiguration
public class DreamMgtListenerConfig implements InitializingBean {

	private static final Logger _logger = LoggerFactory.getLogger(DreamMgtListenerConfig.class);

	@Bean
	String sessionListenerAdapter(Scheduler scheduler, SessionManager sessionManager) throws SchedulerException {
		ListenerAdapter.addListener(SessionListenerAdapter.class, scheduler,
				new ListenerParameter().add("sessionManager", sessionManager).build(), "0 0/10 * * * ?", // 10 minutes
				SessionListenerAdapter.class.getSimpleName());
		_logger.debug("Session ListenerAdapter inited .");
		return "sessionListenerAdapter";
	}

	@Bean
	String dynamicRolesListenerAdapter(Scheduler scheduler, RolesService rolesService,
			@Value("${dream.job.cron.schedule}") String cronSchedule) throws SchedulerException {

		ListenerAdapter.addListener(DynamicRolesListenerAdapter.class, scheduler,
				new ListenerParameter().add("rolesService", rolesService).build(), cronSchedule,
				DynamicRolesListenerAdapter.class.getSimpleName());
		_logger.debug("DynamicRoles ListenerAdapter inited .");
		return "dynamicRolesListenerAdapter";
	}

	@Bean
	String provisioningRunnerThread(ConnectorsService connectorsService, JdbcTemplate jdbcTemplate,
			DreamServerProperties dreamServerProperties) throws SchedulerException {
		if (dreamServerProperties.isProvision()) {
			ProvisioningRunner runner = new ProvisioningRunner(connectorsService, jdbcTemplate);
			ProvisioningRunnerThread runnerThread = new ProvisioningRunnerThread(runner);
			runnerThread.start();
			_logger.debug("provisioning Runner Thread .");
		} else {
			_logger.debug("not need init provisioning Runner Thread .");
		}
		return "provisioningRunnerThread";
	}

	@Override
	public void afterPropertiesSet() throws Exception {

	}

}
