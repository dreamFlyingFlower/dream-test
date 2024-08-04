package com.wy.test.core.entity;

import com.baomidou.mybatisplus.annotation.TableName;

import dream.flying.flower.framework.mybatis.plus.entity.AbstractEntity;
import dream.flying.flower.helper.DateTimeHelper;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

/**
 * 同步关联
 *
 * @author 飞花梦影
 * @date 2024-08-01
 * @git {@link https://github.com/dreamFlyingFlower}
 */
@Getter
@Setter
@ToString
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@TableName("auth_sync_related")
public class SyncRelatedEntity extends AbstractEntity {

	private static final long serialVersionUID = 1L;

	/**
	 * 对象ID
	 */
	private String objectId;

	/**
	 * 对象名称
	 */
	private String objectName;

	/**
	 * 对象展示名称
	 */
	private String objectDisplayName;

	/**
	 * 对象类型
	 */
	private String objectType;

	/**
	 * 同步ID
	 */
	private String syncId;

	/**
	 * 同步名称
	 */
	private String syncName;

	/**
	 * 原始ID
	 */
	private String originId;

	/**
	 * 原始ID2
	 */
	private String originId2;

	/**
	 * 原始ID3
	 */
	private String originId3;

	/**
	 * 同步时间
	 */
	private String syncTime;

	/**
	 * 原始名称
	 */
	private String originName;

	/**
	 * 机构ID
	 */
	private String instId;

	public SyncRelatedEntity(String objectId, String objectName, String objectDisplayName, String objectType,
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
		this.syncTime = DateTimeHelper.formatDateTime();
	}
}