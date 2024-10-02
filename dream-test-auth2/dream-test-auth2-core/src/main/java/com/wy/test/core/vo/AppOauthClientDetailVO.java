package com.wy.test.core.vo;

import java.io.Serializable;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.springframework.util.StringUtils;

import com.fhs.core.trans.vo.TransPojo;
import com.wy.test.core.entity.oauth2.client.BaseClientDetails;

import dream.flying.flower.framework.web.valid.ValidAdd;
import dream.flying.flower.framework.web.valid.ValidEdit;
import io.swagger.v3.oas.annotations.media.Schema;
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
@Schema(description = "oauth_client_details")
public class AppOauthClientDetailVO extends AppVO implements Serializable, TransPojo {

	private static final long serialVersionUID = 1L;

	@Schema(description = "主键")
	@NotNull(message = "id不能为空", groups = { ValidEdit.class })
	private String id;

	@Schema(description = "客户端ID")
	@NotBlank(message = "clientId不能为空", groups = { ValidEdit.class })
	@Size(max = 64, message = "客户端ID最大长度不能超过64", groups = { ValidAdd.class, ValidEdit.class })
	private String clientId;

	@Schema(description = "客户端密钥")
	@Size(max = 512, message = "客户端密钥最大长度不能超过512", groups = { ValidAdd.class, ValidEdit.class })
	private String clientSecret;

	@Schema(description = "资源ID")
	@Size(max = 256, message = "资源ID最大长度不能超过256", groups = { ValidAdd.class, ValidEdit.class })
	private String resourceIds;

	@Schema(description = "授权域")
	@Size(max = 256, message = "授权域最大长度不能超过256", groups = { ValidAdd.class, ValidEdit.class })
	private String scope;

	@Schema(description = "授权类型")
	@Size(max = 128, message = "授权类型最大长度不能超过128", groups = { ValidAdd.class, ValidEdit.class })
	private String authorizedGrantTypes;

	@Schema(description = "认证返回地址")
	@Size(max = 256, message = "认证返回地址最大长度不能超过256", groups = { ValidAdd.class, ValidEdit.class })
	private String webServerRedirectUri;

	@Schema(description = "权限")
	@Size(max = 256, message = "权限最大长度不能超过256", groups = { ValidAdd.class, ValidEdit.class })
	private String authorities;

	@Schema(description = "token有效时间,单位秒")
	private Integer accessTokenValidity;

	@Schema(description = "token刷新有效时间,单位秒")
	private Integer refreshTokenValidity;

	@Schema(description = "扩展信息")
	@Size(max = 4096, message = "扩展信息最大长度不能超过4,096", groups = { ValidAdd.class, ValidEdit.class })
	private String additionalInformation;

	@Schema(description = "审批提示")
	@Size(max = 32, message = "审批提示最大长度不能超过32", groups = { ValidAdd.class, ValidEdit.class })
	private String approvalPrompt;

	@Schema(description = "自动通过")
	@Size(max = 256, message = "自动通过最大长度不能超过256", groups = { ValidAdd.class, ValidEdit.class })
	private String autoApprove;

	@Schema(description = "pkce验证")
	@Size(max = 32, message = "pkce验证最大长度不能超过32", groups = { ValidAdd.class, ValidEdit.class })
	private String pkce;

	@Schema(description = "协议地址")
	@Size(max = 32, message = "协议地址最大长度不能超过32", groups = { ValidAdd.class, ValidEdit.class })
	private String protocol;

	@Schema(description = "发布者")
	@Size(max = 256, message = "发布者最大长度不能超过256", groups = { ValidAdd.class, ValidEdit.class })
	private String issuer;

	@Schema(description = "授权者")
	@Size(max = 256, message = "授权者最大长度不能超过256", groups = { ValidAdd.class, ValidEdit.class })
	private String audience;

	@Schema(description = "算法类型")
	@Size(max = 32, message = "算法类型最大长度不能超过32", groups = { ValidAdd.class, ValidEdit.class })
	private String algorithm;

	@Schema(description = "算法密钥")
	@Size(max = 65535, message = "算法密钥最大长度不能超过65,535", groups = { ValidAdd.class, ValidEdit.class })
	private String algorithmKey;

	@Schema(description = "加密类型")
	@Size(max = 32, message = "加密类型最大长度不能超过32", groups = { ValidAdd.class, ValidEdit.class })
	private String encryptionType;

	@Schema(description = "签名类型")
	@Size(max = 32, message = "签名类型最大长度不能超过32", groups = { ValidAdd.class, ValidEdit.class })
	private String signature;

	@Schema(description = "签名密钥")
	@Size(max = 65535, message = "签名密钥最大长度不能超过65,535", groups = { ValidAdd.class, ValidEdit.class })
	private String signatureKey;

	@Schema(description = "用户信息响应类型:normal,signing,encryption, 如果同时执行签名和加密,则必须先签名,然后再加密")
	@Size(max = 32, message = "用户信息响应类型:normal,signing,encryption, 如果同时执行签名和加密,则必须先签名,然后再加密最大长度不能超过32",
			groups = { ValidAdd.class, ValidEdit.class })
	private String userInfoResponse;

	@Schema(description = "主题")
	@Size(max = 64, message = "主题最大长度不能超过64", groups = { ValidAdd.class, ValidEdit.class })
	private String subject;

	@Schema(description = "机构ID")
	@NotNull(message = "机构ID不能为空", groups = { ValidAdd.class })
	private String instId;

	private String registeredRedirectUris;

	private Integer accessTokenValiditySeconds;

	private Integer refreshTokenValiditySeconds;

	private String instName;

	public AppOauthClientDetailVO(AppVO application, BaseClientDetails baseClientDetails) {
		super();
		this.id = application.getId();
		this.setAppName(application.getAppName());
		this.setLoginUrl(application.getLoginUrl());
		this.setLogoutUrl(application.getLogoutUrl());
		this.setLogoutType(application.getLogoutType());
		this.setCategory(application.getCategory());
		this.setProtocol(application.getProtocol());
		this.setIcon(application.getIcon());
		this.clientId = application.getId();

		this.setSortIndex(application.getSortIndex());
		this.setVendor(application.getVendor());
		this.setVendorUrl(application.getVendorUrl());
		this.setVisible(application.getVisible());

		this.setIsAdapter(application.getIsAdapter());
		this.setAdapterClass(application.getAdapterClass());
		this.setAdapterId(application.getAdapterId());
		this.setAdapterName(application.getAdapterName());
		this.setFrequently(application.getFrequently());
		this.setStatus(application.getStatus());
		this.setInducer(application.getInducer());

		this.clientSecret = baseClientDetails.getClientSecret();
		this.scope = StringUtils.collectionToCommaDelimitedString(baseClientDetails.getScope());
		this.resourceIds = StringUtils.collectionToCommaDelimitedString(baseClientDetails.getResourceIds());
		this.authorizedGrantTypes =
				StringUtils.collectionToCommaDelimitedString(baseClientDetails.getAuthorizedGrantTypes());
		this.registeredRedirectUris =
				StringUtils.collectionToCommaDelimitedString(baseClientDetails.getRegisteredRedirectUri());
		this.authorities = StringUtils.collectionToCommaDelimitedString(baseClientDetails.getAuthorities());
		this.accessTokenValiditySeconds = baseClientDetails.getAccessTokenValiditySeconds();
		this.refreshTokenValiditySeconds = baseClientDetails.getRefreshTokenValiditySeconds();
		this.approvalPrompt = baseClientDetails.isAutoApprove("all") + "";

		this.audience = baseClientDetails.getAudience();
		this.issuer = baseClientDetails.getIssuer();

		this.algorithm = baseClientDetails.getAlgorithm();
		this.algorithmKey = baseClientDetails.getAlgorithmKey();
		this.encryptionType = baseClientDetails.getEncryptionMethod();

		this.signature = baseClientDetails.getSignature();
		this.signatureKey = baseClientDetails.getSignatureKey();

		this.approvalPrompt = baseClientDetails.getApprovalPrompt();

		this.pkce = baseClientDetails.getPkce();
		this.instId = baseClientDetails.getInstId();
		this.subject = baseClientDetails.getSubject();
		this.userInfoResponse = baseClientDetails.getUserInfoResponse();
	}

	public BaseClientDetails clientDetailsRowMapper() {
		BaseClientDetails baseClientDetails = new BaseClientDetails(this.getClientId(), this.getClientId(),
				this.getScope(), this.getAuthorizedGrantTypes(), "ROLE_CLIENT, ROLE_TRUSTED_CLIENT",
				this.getRegisteredRedirectUris());
		baseClientDetails.setAccessTokenValiditySeconds(this.getAccessTokenValiditySeconds());
		baseClientDetails.setRefreshTokenValiditySeconds(this.getRefreshTokenValiditySeconds());
		baseClientDetails.setClientSecret(this.getClientSecret());
		baseClientDetails.setAutoApproveScopes(baseClientDetails.getScope());

		baseClientDetails.setAudience(this.getAudience());
		baseClientDetails.setIssuer(this.getIssuer());

		baseClientDetails.setAlgorithm(this.getAlgorithm());
		baseClientDetails.setAlgorithmKey(this.getAlgorithmKey());
		baseClientDetails.setEncryptionMethod(this.getEncryptionType());

		baseClientDetails.setSignature(this.getSignature());
		baseClientDetails.setSignatureKey(this.getSignatureKey());

		baseClientDetails.setSubject(this.getSubject());
		baseClientDetails.setUserInfoResponse(this.userInfoResponse);

		baseClientDetails.setApprovalPrompt(this.getApprovalPrompt());
		baseClientDetails.setPkce(this.getPkce());
		baseClientDetails.setProtocol(this.getProtocol());
		baseClientDetails.setInstId(instId);
		return baseClientDetails;
	}
}