package com.wy.test.core.query;

import com.fasterxml.jackson.annotation.JsonFormat;

import dream.flying.flower.framework.web.query.AbstractQuery;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

/**
 * 应用表查询
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
@Schema(description = "应用表查询")
public class AppQuery extends AbstractQuery {

	private static final long serialVersionUID = 1L;

	@Schema(description = "应用名称")
	private String appName;

	@Schema(description = "应用登录地址")
	private String loginUrl;

	@Schema(description = "应用类型")
	private String category;

	@Schema(description = "应用密钥")
	private String secret;

	@Schema(description = "单点登录协议")
	private String protocol;

	@Schema(description = "供应商")
	private String vendor;

	@Schema(description = "供应商地址")
	private String vendorUrl;

	private String credential;

	private String sharedUsername;

	private String sharedPassword;

	private String sysUserAttr;

	@JsonFormat(shape = JsonFormat.Shape.STRING)
	private Integer isExtendAttr;

	private String extendAttr;

	/**
	 * 默认0.Signature for client verify create by SignaturePublicKey &
	 * SignaturePrivateKey issuer is domain name subject is app id append domain
	 * name
	 */
	private Integer isSignature;

	private Integer visible;

	private Integer isAdapter;

	private String adapterId;

	private String adapterName;

	private String adapterClass;

	@Schema(description = "第三方凭证")
	private String principal;

	private String credentials;

	// 引导方式 IDP OR SP,default is IDP
	private String inducer;

	private String logoutUrl;

	private Integer logoutType;

	private String userPropertys;

	private String remark;

	private String frequently;

	private String instId;

	private Integer status;

	private String iconBase64;

	private String iconId;

	private String instName;

	private String loginDateTime;

	private String onlineTicket;
}