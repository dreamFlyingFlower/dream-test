package com.wy.test.persistence.provision;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import com.wy.test.core.properties.DreamAuthServerProperties;
import com.wy.test.persistence.provision.thread.ProvisioningThread;

import dream.flying.flower.helper.DateTimeHelper;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class ProvisionService {

	@Autowired
	DreamAuthServerProperties dreamServerProperties;

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
		if (dreamServerProperties.isProvision()) {
			ProvisionMessage message = new ProvisionMessage(UUID.randomUUID().toString(), // message id as uuid
					topic, // TOPIC
					actionType, // action of content
					DateTimeHelper.formatDateTime(), // send time
					null, // content Object to json message content
					content);
			// sand msg to provision topic
			Thread thread = null;
			if (dreamServerProperties.isProvision()) {
				log.trace("message...");
				thread = new ProvisioningThread(jdbcTemplate, message);
				thread.start();
			} else {
				log.trace("no send message...");
			}
		}
	}

	public void setApplicationConfig(DreamAuthServerProperties dreamServerProperties) {
		this.dreamServerProperties = dreamServerProperties;
	}

	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	public DreamAuthServerProperties getDreamServerProperties() {
		return dreamServerProperties;
	}
}