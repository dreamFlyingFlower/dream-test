package com.wy.test.core.entity;

import com.baomidou.mybatisplus.annotation.TableName;

import dream.flying.flower.framework.mybatis.plus.entity.AbstractStringEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

/**
 * 历史连接器
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
@TableName("auth_history_connector")
public class HistoryConnectorEntity extends AbstractStringEntity {

	private static final long serialVersionUID = 3465459057253994386L;

	private String connName;

	private String topic;

	private String actionType;

	private String sourceId;

	private String sourceName;

	private String objectId;

	private String objectName;

	private String remark;

	private String syncTime;

	private String result;

	private String instId;
}