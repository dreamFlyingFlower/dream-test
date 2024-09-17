package com.wy.test.persistence.provision;

import lombok.Data;

@Data
public class ProvisionMessage {

	String id;

	String topic;

	String actionType;

	String sendTime;

	String content;

	int connected;

	int instId;

	Object sourceObject;

	public ProvisionMessage() {
	}

	public ProvisionMessage(String id, String topic, String actionType, String sendTime, String content,
			Object sourceObject) {
		super();
		this.id = id;
		this.topic = topic;
		this.actionType = actionType;
		this.sendTime = sendTime;
		this.content = content;
		this.sourceObject = sourceObject;
	}
}