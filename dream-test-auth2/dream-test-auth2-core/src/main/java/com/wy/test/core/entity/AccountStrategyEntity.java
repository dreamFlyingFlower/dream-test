package com.wy.test.core.entity;

import com.baomidou.mybatisplus.annotation.TableName;

import dream.flying.flower.framework.mybatis.plus.entity.AbstractEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

/**
 * 用户账号表
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
@TableName("auth_account_strategy")
public class AccountStrategyEntity extends AbstractEntity {

	private static final long serialVersionUID = 1L;

	private String name;

	private String appId;

	private String appName;

	private String mapping;

	String filters;

	String orgIds;

	String suffixes;

	String createType;

	Integer status;

	String remark;

	private String instId;
}