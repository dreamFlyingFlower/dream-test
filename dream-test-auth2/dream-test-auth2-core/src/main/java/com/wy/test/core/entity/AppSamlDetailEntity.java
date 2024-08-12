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
 * SAML2详情
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
@TableName("auth_app_saml_detail")
public class AppSamlDetailEntity extends AbstractStringEntity {

	private static final long serialVersionUID = -3285797303108862489L;

	/**
	 * 证书发布者
	 */
	private String certIssuer;

	/**
	 * 证书主题
	 */
	private String certSubject;

	/**
	 * 过期时间
	 */
	private String certExpiration;

	/**
	 * 密钥存储
	 */
	private byte[] keystore;

	/**
	 * 地址
	 */
	private String spAcsUrl;

	/**
	 * 发布者
	 */
	private String issuer;

	/**
	 * 实体标识
	 */
	private String entityId;

	/**
	 * 有效期
	 */
	private Integer validityInterval;

	/**
	 * 名称id格式
	 */
	private String nameIdFormat;

	/**
	 * 名称id转换 0 original 1 uppercase 2 lowercase
	 */
	private String nameIdConvert;

	/**
	 * 名称id后缀
	 */
	private String nameIdSuffix;

	/**
	 * 授权者
	 */
	private String audience;

	/**
	 * 是否加密:0-否;1-是
	 */
	private String encrypted;

	/**
	 * 绑定方式:Redirect-Post Post-Post IdpInit-Post Redirect-PostSimpleSign
	 * Post-PostSimpleSign IdpInit-PostSimpleSign
	 */
	private String binding;

	/**
	 * 签名
	 */
	private String signature;

	/**
	 * 加密类型
	 */
	private String digestMethod;

	/**
	 * 元数据URL
	 */
	private String metaUrl;

	/**
	 * 应用ID
	 */
	private String appId;

	/**
	 * 机构ID
	 */
	private String instId;
}