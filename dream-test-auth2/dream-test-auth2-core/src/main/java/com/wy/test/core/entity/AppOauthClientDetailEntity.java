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
 * oauth_client_details
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
@TableName("auth_app_oauth_client_detail")
public class AppOauthClientDetailEntity extends AbstractStringEntity {

	private static final long serialVersionUID = 117221908707922101L;

	/**
	 * 客户端ID
	 */
	private String clientId;

	/**
	 * 客户端密钥
	 */
	private String clientSecret;

	/**
	 * 资源ID
	 */
	private String resourceIds;

	/**
	 * 授权域
	 */
	private String scope;

	/**
	 * 授权类型
	 */
	private String authorizedGrantTypes;

	/**
	 * 认证返回地址
	 */
	private String webServerRedirectUri;

	private String registeredRedirectUris;

	/**
	 * 权限
	 */
	private String authorities;

	/**
	 * token有效时间,单位秒
	 */
	private Integer accessTokenValidity;

	/**
	 * token刷新有效时间,单位秒
	 */
	private Integer refreshTokenValidity;

	/**
	 * 扩展信息
	 */
	private String additionalInformation;

	/**
	 * 审批提示
	 */
	private String approvalPrompt;

	/**
	 * 自动通过
	 */
	private String autoApprove;

	/**
	 * pkce验证
	 */
	private String pkce;

	/**
	 * 协议地址
	 */
	private String protocol;

	/**
	 * 发布者
	 */
	private String issuer;

	/**
	 * 授权者
	 */
	private String audience;

	/**
	 * 算法类型
	 */
	private String algorithm;

	/**
	 * 算法密钥
	 */
	private String algorithmKey;

	/**
	 * 加密类型
	 */
	private String encryptionMethod;

	/**
	 * 签名类型
	 */
	private String signature;

	/**
	 * 签名密钥
	 */
	private String signatureKey;

	/**
	 * 用户信息响应类型:normal,signing,encryption, 如果同时执行签名和加密,则必须先签名,然后再加密
	 */
	private String userInfoResponse;

	/**
	 * 主题
	 */
	private String subject;

	/**
	 * 机构ID
	 */
	private String instId;
}