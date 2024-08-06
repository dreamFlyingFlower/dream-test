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
 * LDAP上下文
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
@TableName("auth_ldap_context")
public class LdapContextEntity extends AbstractStringEntity {

	private static final long serialVersionUID = -5726518661912505397L;

	/**
	 * 产品
	 */
	private String product;

	/**
	 * 是否使用SSL
	 */
	private String sslSwitch;

	/**
	 * 提供者URL
	 */
	private String providerUrl;

	/**
	 * 用户名
	 */
	private String principal;

	/**
	 * 密码
	 */
	private String credentials;

	/**
	 * 基础DN
	 */
	private String basedn;

	/**
	 * 拦截
	 */
	private String filters;

	/**
	 * 信任
	 */
	private String trustStore;

	/**
	 * 信任密码
	 */
	private String trustStorePassword;

	/**
	 * MSAD域名
	 */
	private String msadDomain;

	/**
	 * 帐号
	 */
	private String accountMapping;

	/**
	 * 机构ID
	 */
	private String instId;

	/**
	 * 备注
	 */
	private String remark;

	/**
	 * 状态
	 */
	private Integer status;
}