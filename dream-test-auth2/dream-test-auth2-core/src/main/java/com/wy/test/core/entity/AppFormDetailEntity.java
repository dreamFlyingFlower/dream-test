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
 * 表单信息
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
@TableName("auth_app_form_detail")
public class AppFormDetailEntity extends AbstractStringEntity {

	private static final long serialVersionUID = 1L;

	/**
	 * 用户名
	 */
	private String usernameMapping;

	/**
	 * 密码
	 */
	private String passwordMapping;

	/**
	 * 重定向uri
	 */
	private String redirectUri;

	/**
	 * 本地授权视图
	 */
	private String authorizeView;

	/**
	 * 密码加密算法
	 */
	private String passwordAlgorithm;

	/**
	 * 机构ID
	 */
	private String instId;
}