package com.wy.test.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.dromara.mybatis.jpa.entity.JpaEntity;

import com.wy.test.util.DateUtils;

@Entity
@Table(name = "MXK_SYNCHRO_RELATED")
public class SynchroRelated extends JpaEntity implements Serializable {

	private static final long serialVersionUID = 6993697309055585706L;

	@Id
	@Column
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "snowflakeid")
	String id;

	@Column
	protected String objectId;

	@Column
	protected String objectName;

	@Column
	protected String objectDisplayName;

	@Column
	protected String objectType;

	@Column
	protected String syncId;

	@Column
	protected String syncName;

	@Column
	protected String originId;

	@Column
	protected String originId2;

	@Column
	protected String originId3;

	@Column
	protected String originName;

	@Column
	protected String instId;

	protected String instName;

	@Column
	protected String syncTime;

	public SynchroRelated() {
		super();
	}

	public SynchroRelated(String objectId, String objectName, String objectDisplayName, String objectType,
			String syncId, String syncName, String originId, String originName, String originId2, String originId3,
			String instId) {
		super();
		this.objectId = objectId;
		this.objectName = objectName;
		this.objectDisplayName = objectDisplayName;
		this.objectType = objectType;
		this.syncId = syncId;
		this.syncName = syncName;
		this.originId = originId;
		this.originName = originName;
		this.originId2 = originId2;
		this.originId3 = originId3;
		this.instId = instId;
		this.syncTime = DateUtils.formatDateTime(new Date());
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getObjectId() {
		return objectId;
	}

	public void setObjectId(String objectId) {
		this.objectId = objectId;
	}

	public String getObjectName() {
		return objectName;
	}

	public void setObjectName(String objectName) {
		this.objectName = objectName;
	}

	public String getObjectDisplayName() {
		return objectDisplayName;
	}

	public void setObjectDisplayName(String objectDisplayName) {
		this.objectDisplayName = objectDisplayName;
	}

	public String getObjectType() {
		return objectType;
	}

	public void setObjectType(String objectType) {
		this.objectType = objectType;
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

	public String getOriginId() {
		return originId;
	}

	public void setOriginId(String originId) {
		this.originId = originId;
	}

	public String getOriginName() {
		return originName;
	}

	public void setOriginName(String originName) {
		this.originName = originName;
	}

	public String getOriginId2() {
		return originId2;
	}

	public void setOriginId2(String originId2) {
		this.originId2 = originId2;
	}

	public String getOriginId3() {
		return originId3;
	}

	public void setOriginId3(String originId3) {
		this.originId3 = originId3;
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

	public String getSyncTime() {
		return syncTime;
	}

	public void setSyncTime(String syncTime) {
		this.syncTime = syncTime;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("SynchroRelated [id=");
		builder.append(id);
		builder.append(", objectId=");
		builder.append(objectId);
		builder.append(", objectName=");
		builder.append(objectName);
		builder.append(", objectType=");
		builder.append(objectType);
		builder.append(", syncId=");
		builder.append(syncId);
		builder.append(", syncName=");
		builder.append(syncName);
		builder.append(", originId=");
		builder.append(originId);
		builder.append(", originId2=");
		builder.append(originId2);
		builder.append(", originId3=");
		builder.append(originId3);
		builder.append(", instId=");
		builder.append(instId);
		builder.append(", instName=");
		builder.append(instName);
		builder.append(", syncTime=");
		builder.append(syncTime);
		builder.append("]");
		return builder.toString();
	}

}
