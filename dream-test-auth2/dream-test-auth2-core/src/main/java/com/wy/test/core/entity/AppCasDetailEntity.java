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
 * CAS详情
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
@TableName("auth_app_cas_detail")
public class AppCasDetailEntity extends AbstractEntity {

	private static final long serialVersionUID = -6338959453672254667L;

	/**
	 * 服务地址
	 */
	private String service;

	/**
	 * 回调地址
	 */
	private String callbackUrl;

	/**
	 * 过期时间,单位秒
	 */
	private Integer expires;

	/**
	 * 机构ID
	 */
	private String instId;

	/**
	 * 单点登录用户
	 */
	private String casUser;
}