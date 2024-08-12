package com.wy.test.core.entity;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Saml20Metadata implements Serializable {

	private static final long serialVersionUID = -3992005168513078437L;

	/**
	 * 机构名称
	 */
	private String orgName;

	/**
	 * 机构显示名称
	 */
	private String orgDisplayName;

	/**
	 * 机构地址
	 */
	private String orgURL;

	/**
	 * 联系方式
	 */
	private String contactType;

	/**
	 * 公司名称
	 */
	private String company;

	/**
	 * 名
	 */
	private String givenName;

	/**
	 * 姓
	 */
	private String surName;

	/**
	 * 邮件
	 */
	private String emailAddress;

	/**
	 * 手机号
	 */
	private String phoneNumber;
}