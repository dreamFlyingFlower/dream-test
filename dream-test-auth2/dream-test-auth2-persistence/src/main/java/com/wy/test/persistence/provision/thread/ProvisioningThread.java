package com.wy.test.persistence.provision.thread;

import java.io.Serializable;
import java.sql.Types;

import org.springframework.jdbc.core.JdbcTemplate;

import com.wy.test.persistence.provision.ProvisionMessage;

import dream.flying.flower.framework.core.json.JsonHelpers;
import dream.flying.flower.framework.core.pretty.strategy.JsonPretty;
import dream.flying.flower.lang.SerializableHelper;
import lombok.extern.slf4j.Slf4j;

/**
 * Provisioning Thread for send message
 */
@Slf4j
public class ProvisioningThread extends Thread {

	static final String PROVISION_INSERT_STATEMENT =
			"insert into auth_history_provision(id,topic,actiontype,content,sendtime,connected,instid) values (? , ? , ? , ? , ? , ?  , ? )";

	JdbcTemplate jdbcTemplate;

	ProvisionMessage msg;

	public ProvisioningThread(JdbcTemplate jdbcTemplate, ProvisionMessage msg) {
		this.jdbcTemplate = jdbcTemplate;
		this.msg = msg;
	}

	@Override
	public void run() {
		log.debug("send message \n{}", new JsonPretty().jacksonFormat(msg.getSourceObject()));
		msg.setContent(SerializableHelper.serializeHex((Serializable) msg.getSourceObject()));
		Inst inst = JsonHelpers.read(JsonHelpers.toString(msg.getSourceObject()), Inst.class);
		jdbcTemplate.update(PROVISION_INSERT_STATEMENT,
				new Object[] { msg.getId(), msg.getTopic(), msg.getActionType(), msg.getContent(), msg.getSendTime(),
						msg.getConnected(), inst.getInstId() },
				new int[] { Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.TINYINT,
						Types.TINYINT });
		log.debug("send to Message Queue finished .");
	}

	class Inst {

		int instId;

		public int getInstId() {
			return instId;
		}

		public void setInstId(int instId) {
			this.instId = instId;
		}

		public Inst() {
		}
	}
}