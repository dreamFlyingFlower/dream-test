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
 * token详情
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
@TableName("auth_app_token_detail")
public class AppTokenDetailEntity extends AbstractEntity {

	private static final long serialVersionUID = 9145222955877806563L;

	/**
	 * 加密算法
	 */
	private String algorithm;

	/**
	 * 秘钥
	 */
	private String algorithmKey;

	/**
	 * 过期时间,单位秒
	 */
	private Integer expires;

	/**
	 * 重定向URI
	 */
	private String redirectUri;

	/**
	 * cookie名称
	 */
	private String cookieName;

	/**
	 * token类型
	 */
	private String tokenType;

	/**
	 * 机构id
	 */
	private String instId;
}