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
 * 第三方登录用户
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
@TableName("auth_social_associate")
public class SocialAssociateEntity extends AbstractEntity {

	private static final long serialVersionUID = 1L;

	/**
	 * 用户ID
	 */
	private String userId;

	/**
	 * 用户名
	 */
	private String username;

	/**
	 * 提供者
	 */
	private String provider;

	/**
	 * 社交用户信息
	 */
	private String socialUserInfo;

	/**
	 * 社交用户ID
	 */
	private String socialUserId;

	/**
	 * 扩展属性
	 */
	private String extendAttribute;

	/**
	 * Token
	 */
	private String accessToken;

	/**
	 * 转义
	 */
	private String transMission;

	/**
	 * 机构ID
	 */
	private String instId;
}