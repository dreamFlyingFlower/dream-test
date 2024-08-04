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
@TableName("auth_account")
public class AccountEntity extends AbstractEntity {

	private static final long serialVersionUID = 1L;

	public static final String DEFAULT_PASSWORD_SUFFIX = UserEntity.DEFAULT_PASSWORD_SUFFIX;

	/**
	 * 用户ID
	 */
	private String userId;

	/**
	 * 用户名
	 */
	private String username;

	/**
	 * 用户显示名
	 */
	private String displayName;

	/**
	 * 策略名称
	 */
	private String strategyName;

	/**
	 * ID策略
	 */
	private String strategyId;

	/**
	 * 应用ID
	 */
	private String appId;

	/**
	 * 应用名称
	 */
	private String appName;

	/**
	 * 用户名
	 */
	private String relatedUsername;

	/**
	 * 密码
	 */
	private String relatedPassword;

	/**
	 * 创建类型
	 */
	private String createType;

	/**
	 * 机构ID
	 */
	private String instId;

	/**
	 * 用户状态
	 */
	private Integer status;

	public AccountEntity(String id) {
		this.setId(id);
	}

	public AccountEntity(String userId, String appId) {
		this.userId = userId;
		this.appId = appId;
	}

	public AccountEntity(String userId, String appId, String password) {
		this.userId = userId;
		this.appId = appId;
		this.relatedPassword = password;
	}
}