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
 * JWT详情
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
@TableName("auth_app_jwt_detail")
public class AppJwtDetailEntity extends AbstractStringEntity {

	private static final long serialVersionUID = -49405909465700551L;

	/**
	 * 发布者
	 */
	private String issuer;

	/**
	 * 主题
	 */
	private String subject;

	/**
	 * 授权者
	 */
	private String audience;

	/**
	 * 秘钥
	 */
	private String algorithmKey;

	/**
	 * 加密算法
	 */
	private String algorithm;

	/**
	 * 加密类型
	 */
	private String encryptionMethod;

	/**
	 * 签名算法
	 */
	private String signature;

	/**
	 * 签名密钥
	 */
	private String signatureKey;

	/**
	 * 过期时间,单位秒
	 */
	private Integer expires;

	/**
	 * 重定向URI
	 */
	private String redirectUri;

	/**
	 * Jwt名称
	 */
	private String jwtName;

	/**
	 * Token类型
	 */
	private String tokenType;

	/**
	 * 机构ID
	 */
	private String instId;
}