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
 * 密码策略
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
@TableName("auth_password_policy")
public class PasswordPolicyEntity extends AbstractStringEntity {

	private static final long serialVersionUID = 1L;

	/**
	 * 最小长度
	 */
	private Integer minLength;

	/**
	 * 最大长度
	 */
	private Integer maxLength;

	/**
	 * 小写
	 */
	private Integer lowerCase;

	/**
	 * 大写
	 */
	private Integer upperCase;

	/**
	 * 数字
	 */
	private Integer digits;

	/**
	 * 特殊单词
	 */
	private Integer specialChar;

	/**
	 * 登录是否锁定
	 */
	private Integer attempts;

	/**
	 * 锁定时长
	 */
	private Integer duration;

	/**
	 * 密码有效期
	 */
	private Integer expiration;

	/**
	 * 用户名
	 */
	private Integer username;

	/**
	 * 历史密码
	 */
	private Integer history;

	/**
	 * 字典
	 */
	private Integer dictionary;

	/**
	 * 字母
	 */
	private Integer alphabetical;

	/**
	 * 数字
	 */
	private Integer numerical;

	/**
	 * 标准键盘
	 */
	private Integer qwerty;

	/**
	 * 事件
	 */
	private Integer occurances;

	/**
	 * 随机密码长度
	 */
	private Integer randomPasswordLength;

	/**
	 * 机构ID
	 */
	private String instId;
}