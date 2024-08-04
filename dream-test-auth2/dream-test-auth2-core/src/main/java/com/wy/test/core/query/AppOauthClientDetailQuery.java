package com.wy.test.core.query;

import dream.flying.flower.framework.web.query.AbstractQuery;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

/**
 * oauth_client_details查询
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
@Schema(description = "oauth_client_details查询")
public class AppOauthClientDetailQuery extends AbstractQuery {

	private static final long serialVersionUID = 1L;

	@Schema(description = "客户端ID")
	private String clientId;

	@Schema(description = "客户端密钥")
	private String clientSecret;

	@Schema(description = "资源ID")
	private String resourceIds;

	@Schema(description = "授权域")
	private String scope;

	@Schema(description = "授权类型")
	private String authorizedGrantTypes;

	@Schema(description = "认证返回地址")
	private String webServerRedirectUri;

	@Schema(description = "权限")
	private String authorities;

	@Schema(description = "token有效时间,单位秒")
	private Integer accessTokenValidity;

	@Schema(description = "token刷新有效时间,单位秒")
	private Integer refreshTokenValidity;

	@Schema(description = "扩展信息")
	private String additionalInformation;

	@Schema(description = "审批提示")
	private String approvalPrompt;

	@Schema(description = "自动通过")
	private String autoApprove;

	@Schema(description = "pkce验证")
	private String pkce;

	@Schema(description = "协议地址")
	private String protocol;

	@Schema(description = "发布者")
	private String issuer;

	@Schema(description = "授权者")
	private String audience;

	@Schema(description = "算法类型")
	private String algorithm;

	@Schema(description = "算法密钥")
	private String algorithmKey;

	@Schema(description = "加密类型")
	private String encryptionType;

	@Schema(description = "签名类型")
	private String signature;

	@Schema(description = "签名密钥")
	private String signatureKey;

	@Schema(description = "用户信息响应类型:normal,signing,encryption, 如果同时执行签名和加密,则必须先签名,然后再加密")
	private String userInfoResponse;

	@Schema(description = "主题")
	private String subject;

	@Schema(description = "机构ID")
	private String instId;
}