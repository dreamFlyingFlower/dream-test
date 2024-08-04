package com.wy.test.core.vo;

import java.io.Serializable;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fhs.core.trans.vo.TransPojo;

import dream.flying.flower.framework.web.valid.ValidAdd;
import dream.flying.flower.framework.web.valid.ValidEdit;
import dream.flying.flower.helper.ImageHelper;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

/**
 * 应用表
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
@Schema(description = "应用表")
public class AppVO implements Serializable, TransPojo {

	private static final long serialVersionUID = 1L;

	@Schema(description = "主键")
	@NotNull(message = "id不能为空", groups = { ValidEdit.class })
	private String id;

	@Schema(description = "应用名称")
	@NotBlank(message = "应用名称不能为空", groups = { ValidAdd.class })
	@Size(max = 64, message = "应用名称最大长度不能超过64", groups = { ValidAdd.class, ValidEdit.class })
	private String appName;

	@Schema(description = "应用登录地址")
	@NotBlank(message = "应用登录地址不能为空", groups = { ValidAdd.class })
	@Size(max = 256, message = "应用登录地址最大长度不能超过256", groups = { ValidAdd.class, ValidEdit.class })
	private String loginUrl;

	@Schema(description = "应用类型")
	@Size(max = 32, message = "应用类型最大长度不能超过32", groups = { ValidAdd.class, ValidEdit.class })
	private String category;

	@Schema(description = "应用密钥")
	@Size(max = 256, message = "应用密钥最大长度不能超过256", groups = { ValidAdd.class, ValidEdit.class })
	private String secret;

	@Schema(description = "单点登录协议")
	@Size(max = 256, message = "单点登录协议最大长度不能超过256", groups = { ValidAdd.class, ValidEdit.class })
	private String protocol;

	@Schema(description = "图标")
	private byte[] icon;

	@Schema(description = "供应商")
	private String vendor;

	@Schema(description = "供应商地址")
	private String vendorUrl;

	/*
	 * CREDENTIAL VALUES USER-DEFINED SYSTEM SHARED NONE
	 */
	private String credential;

	private String sharedUsername;

	private String sharedPassword;

	private String sysUserAttr;

	@JsonFormat(shape = JsonFormat.Shape.STRING)
	private Integer isExtendAttr;

	private String extendAttr;

	private Integer sortIndex;

	/**
	 * 默认0.Signature for client verify create by SignaturePublicKey &
	 * SignaturePrivateKey issuer is domain name subject is app id append domain
	 * name
	 */
	private Integer isSignature;

	@JsonFormat(shape = JsonFormat.Shape.STRING)
	private Integer visible;

	@JsonFormat(shape = JsonFormat.Shape.STRING)
	private Integer isAdapter;

	private String adapterId;

	private String adapterName;

	private String adapter;

	@Schema(description = "第三方凭证")
	private String principal;

	private String credentials;

	// 引导方式 IDP OR SP,default is IDP
	private String inducer;

	private String logoutUrl;

	@JsonFormat(shape = JsonFormat.Shape.STRING)
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

	public void transIconBase64() {
		if (icon != null) {
			this.iconBase64 = ImageHelper.encodeImage(icon);
		}
	}
}