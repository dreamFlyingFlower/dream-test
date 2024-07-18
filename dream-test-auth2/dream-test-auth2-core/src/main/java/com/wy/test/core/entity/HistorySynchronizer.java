package com.wy.test.core.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.dromara.mybatis.jpa.entity.JpaEntity;

@Entity
@Table(name = "MXK_HISTORY_SYNCHRONIZER")
public class HistorySynchronizer extends JpaEntity implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1184644499009162756L;

	@Id
	@Column
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "snowflakeid")
	String id;

	@Column
	String syncId;

	@Column
	String sessionId;

	@Column
	String syncName;

	@Column
	String objectId;

	@Column
	String objectType;

	@Column
	String objectName;

	String syncTime;

	@Column
	String result;

	@Column
	private String instId;

	private String instName;

	String startDate;

	String endDate;

	public HistorySynchronizer(String id, String syncId, String syncName, String objectId, String objectType,
			String objectName, String syncTime, String result, String instId) {
		super();
		this.id = id;
		this.syncId = syncId;
		this.syncName = syncName;
		this.objectId = objectId;
		this.objectType = objectType;
		this.objectName = objectName;
		this.syncTime = syncTime;
		this.result = result;
		this.instId = instId;
	}

	public HistorySynchronizer(String id, String syncId, String sessionId, String syncName, String objectId,
			String objectType, String objectName, String syncTime, String result, String instId) {
		super();
		this.id = id;
		this.syncId = syncId;
		this.sessionId = sessionId;
		this.syncName = syncName;
		this.objectId = objectId;
		this.objectType = objectType;
		this.objectName = objectName;
		this.syncTime = syncTime;
		this.result = result;
		this.instId = instId;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getSyncId() {
		return syncId;
	}

	public void setSyncId(String syncId) {
		this.syncId = syncId;
	}

	public String getSyncName() {
		return syncName;
	}

	public void setSyncName(String syncName) {
		this.syncName = syncName;
	}

	public String getObjectId() {
		return objectId;
	}

	public void setObjectId(String objectId) {
		this.objectId = objectId;
	}

	public String getObjectType() {
		return objectType;
	}

	public void setObjectType(String objectType) {
		this.objectType = objectType;
	}

	public String getObjectName() {
		return objectName;
	}

	public void setObjectName(String objectName) {
		this.objectName = objectName;
	}

	public String getSyncTime() {
		return syncTime;
	}

	public void setSyncTime(String syncTime) {
		this.syncTime = syncTime;
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	public HistorySynchronizer() {
		super();
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

	public String getSessionId() {
		return sessionId;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
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

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("HistorySynchronizer [id=");
		builder.append(id);
		builder.append(", syncId=");
		builder.append(syncId);
		builder.append(", sessionId=");
		builder.append(sessionId);
		builder.append(", syncName=");
		builder.append(syncName);
		builder.append(", objectId=");
		builder.append(objectId);
		builder.append(", objectType=");
		builder.append(objectType);
		builder.append(", objectName=");
		builder.append(objectName);
		builder.append(", syncTime=");
		builder.append(syncTime);
		builder.append(", result=");
		builder.append(result);
		builder.append(", startDate=");
		builder.append(startDate);
		builder.append(", endDate=");
		builder.append(endDate);
		builder.append("]");
		return builder.toString();
	}

}
