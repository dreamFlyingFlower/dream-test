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
 * LDAP上下文查询
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
@Schema(description = "LDAP上下文查询")
public class LdapContextQuery extends AbstractQuery {

	private static final long serialVersionUID = 1L;

	@Schema(description = "产品")
	private String product;

	@Schema(description = "是否使用SSL")
	private String sslSwitch;

	@Schema(description = "提供者URL")
	private String providerUrl;

	@Schema(description = "用户名")
	private String principal;

	@Schema(description = "密码")
	private String credentials;

	@Schema(description = "基础DN")
	private String basedn;

	@Schema(description = "拦截")
	private String filters;

	@Schema(description = "信任")
	private String trustStore;

	@Schema(description = "信任密码")
	private String trustStorePassword;

	@Schema(description = "MSAD域名")
	private String msadDomain;

	@Schema(description = "帐号")
	private String accountMapping;

	@Schema(description = "机构ID")
	private String instId;

	@Schema(description = "备注")
	private String remark;

	@Schema(description = "状态")
	private Integer status;
}