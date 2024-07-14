package com.wy.test.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.dromara.mybatis.jpa.entity.JpaEntity;

@Entity
@Table(name = "MXK_HISTORY_SYSTEM_LOGS")
public class HistorySystemLogs extends JpaEntity implements Serializable {

	private static final long serialVersionUID = 6560201093784960493L;

	@Id
	@Column
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "snowflakeid")
	String id;

	@Column
	String topic;

	@Column
	String message;

	@Column
	String messageAction;

	@Column
	String messageResult;

	@Column
	String userId;

	@Column
	String username;

	@Column
	String displayName;

	@Column
	String executeTime;

	@Column
	private String instId;

	String jsonCotent;

	private String instName;

	String startDate;

	String endDate;

	public HistorySystemLogs() {
		super();
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTopic() {
		return topic;
	}

	public void setTopic(String topic) {
		this.topic = topic;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getMessageAction() {
		return messageAction;
	}

	public void setMessageAction(String messageAction) {
		this.messageAction = messageAction;
	}

	public String getMessageResult() {
		return messageResult;
	}

	public void setMessageResult(String messageResult) {
		this.messageResult = messageResult;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public String getExecuteTime() {
		return executeTime;
	}

	public void setExecuteTime(String executeTime) {
		this.executeTime = executeTime;
	}

	public String getInstId() {
		return instId;
	}

	public void setInstId(String instId) {
		this.instId = instId;
	}

	public String getInstName() {
		return instName;
	}

	public void setInstName(String instName) {
		this.instName = instName;
	}

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public String getJsonCotent() {
		return jsonCotent;
	}

	public void setJsonCotent(String jsonCotent) {
		this.jsonCotent = jsonCotent;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("HistorySystemLogs [id=");
		builder.append(id);
		builder.append(", topic=");
		builder.append(topic);
		builder.append(", message=");
		builder.append(message);
		builder.append(", messageAction=");
		builder.append(messageAction);
		builder.append(", messageResult=");
		builder.append(messageResult);
		builder.append(", userId=");
		builder.append(userId);
		builder.append(", username=");
		builder.append(username);
		builder.append(", displayName=");
		builder.append(displayName);
		builder.append(", executeTime=");
		builder.append(executeTime);
		builder.append(", jsonCotent=");
		builder.append(jsonCotent);
		builder.append("]");
		return builder.toString();
	}

}
