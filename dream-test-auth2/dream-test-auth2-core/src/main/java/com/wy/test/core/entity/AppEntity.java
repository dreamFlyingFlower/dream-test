package com.wy.test.core.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;

import dream.flying.flower.framework.mybatis.plus.entity.AbstractStringEntity;
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
@TableName("auth_app")
public class AppEntity extends AbstractStringEntity {

	private static final long serialVersionUID = 1773969303999177999L;

	public static final class CREDENTIALS {

		public static final String USER_DEFINED = "user_defined";

		public static final String SHARED = "shared";

		public static final String SYSTEM = "system";

		public static final String NONE = "none";
	}

	public static final class VISIBLE {

		public static final int HIDDEN = 0;

		public static final int ALL = 1;

		public static final int INTERNET = 2;

		public static final int INTRANET = 3;
	}

	/**
	 * 应用名称
	 */
	private String appName;

	/**
	 * 图标
	 */
	private byte[] icon;

	/**
	 * 应用登录地址
	 */
	private String loginUrl;

	/**
	 * 单点登录协议
	 */
	private String protocol;

	/**
	 * 应用类型
	 */
	private String category;

	/**
	 * 应用密钥
	 */
	private String secret;

	/**
	 * 供应商
	 */
	private String vendor;

	/**
	 * 供应商地址
	 */
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

	private String userPropertys;

	/**
	 * Signature for client verify create by SignaturePublicKey & SignaturePrivateKey issuer is domain name subject is
	 * app id append domain name
	 */
	private Integer isSignature;

	@JsonFormat(shape = JsonFormat.Shape.STRING)
	private Integer isAdapter;

	private String adapterId;

	private String adapterName;

	private String adapter;

	/**
	 * 获取第三方token凭证
	 */
	private String principal;

	private String credentials;

	/**
	 * 是否可见
	 */
	@JsonFormat(shape = JsonFormat.Shape.STRING)
	private Integer visible;

	// 引导方式 IDP OR SP,default is IDP
	private String inducer;

	private String logoutUrl;

	@JsonFormat(shape = JsonFormat.Shape.STRING)
	private Integer logoutType;

	private String frequently;

	private Integer sortIndex;

	private String remark;

	private String instId;

	private String status;
}