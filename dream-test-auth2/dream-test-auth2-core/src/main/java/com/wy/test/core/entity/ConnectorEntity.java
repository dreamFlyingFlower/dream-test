package com.wy.test.core.entity;

import com.baomidou.mybatisplus.annotation.SqlCondition;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;

import dream.flying.flower.framework.mybatis.plus.entity.AbstractStringEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

/**
 * 连接器
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
@TableName("auth_connector")
public class ConnectorEntity extends AbstractStringEntity {

	private static final long serialVersionUID = 4660258495864814777L;

	@TableField(condition = SqlCondition.LIKE)
	private String connName;

	private String scheduler;

	private int justInTime;

	private String providerUrl;

	private String principal;

	private String credentials;

	private String filters;

	private String remark;

	private String status;

	private String instId;
}