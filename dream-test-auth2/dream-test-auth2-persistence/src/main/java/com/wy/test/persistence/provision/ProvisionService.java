package com.wy.test.persistence.provision;

import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import com.wy.test.core.configuration.ApplicationConfig;
import com.wy.test.persistence.provision.thread.ProvisioningThread;

import dream.flying.flower.helper.DateTimeHelper;

@Component
public class ProvisionService {

	private static final Logger _logger = LoggerFactory.getLogger(ProvisionService.class);

	@Autowired
	ApplicationConfig applicationConfig;

	@Autowired
	JdbcTemplate jdbcTemplate;

	/**
	 * send msg to jdbc
	 * 
	 * @param topic TOPIC
	 * @param content msg Object
	 * @param actionType CREATE UPDATE DELETE
	 */
	public void send(String topic, Object content, String actionType) {
		// dream.server.message.queue , if not none
		if (applicationConfig.isProvisionSupport()) {
			ProvisionMessage message = new ProvisionMessage(UUID.randomUUID().toString(), // message id as uuid
					topic, // TOPIC
					actionType, // action of content
					DateTimeHelper.formatDateTime(), // send time
					null, // content Object to json message content
					content);
			// sand msg to provision topic
			Thread thread = null;
			if (applicationConfig.isProvisionSupport()) {
				_logger.trace("message...");
				thread = new ProvisioningThread(jdbcTemplate, message);
				thread.start();
			} else {
				_logger.trace("no send message...");
			}
		}
	}

	public void setApplicationConfig(ApplicationConfig applicationConfig) {
		this.applicationConfig = applicationConfig;
	}

	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	public ApplicationConfig getApplicationConfig() {
		return applicationConfig;
	}

}
