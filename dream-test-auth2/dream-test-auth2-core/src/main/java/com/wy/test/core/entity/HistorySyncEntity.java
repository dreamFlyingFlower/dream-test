package com.wy.test.core.entity;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.TableName;

import dream.flying.flower.framework.mybatis.plus.entity.AbstractEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@ToString
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@TableName("auth_history_sync")
public class HistorySyncEntity extends AbstractEntity {

	private static final long serialVersionUID = -1184644499009162756L;

	private String syncId;

	private String sessionId;

	private String syncName;

	private String objectId;

	private String objectType;

	private String objectName;

	private Date syncTime;

	private String result;

	private String instId;

	public HistorySyncEntity(String id, String syncId, String sessionId, String syncName, String objectId,
			String objectType, String objectName, Date syncTime, String result, String instId) {
		super();
		setId(id);
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
}