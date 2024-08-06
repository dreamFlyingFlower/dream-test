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
 * 机构
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
@TableName("auth_institution")
public class InstitutionEntity extends AbstractStringEntity {

	private static final long serialVersionUID = -2375872012431214098L;

	/**
	 * 机构名称
	 */
	private String name;

	/**
	 * 机构全名
	 */
	private String fullName;

	/**
	 * 分区
	 */
	private String division;

	/**
	 * 国家
	 */
	private String country;

	/**
	 * 省
	 */
	private String region;

	/**
	 * 城市
	 */
	private String locality;

	/**
	 * 街道
	 */
	private String street;

	/**
	 * 地址
	 */
	private String address;

	/**
	 * 联系人
	 */
	private String contact;

	/**
	 * 邮编
	 */
	private String postalCode;

	/**
	 * 电话
	 */
	private String phone;

	/**
	 * 传真
	 */
	private String fax;

	/**
	 * 邮箱
	 */
	private String email;

	/**
	 * logo
	 */
	private String logo;

	/**
	 * 域名
	 */
	private String domain;

	/**
	 * 前端标题
	 */
	private String frontTitle;

	/**
	 * 控制台域名
	 */
	private String consoleDomain;

	/**
	 * 控制台标题
	 */
	private String consoleTitle;

	/**
	 * 验证码
	 */
	private String captcha;

	/**
	 * 默认URI
	 */
	private String defaultUri;

	/**
	 * 描述
	 */
	private String remark;

	/**
	 * 排序
	 */
	private Integer sortIndex;

	/**
	 * 机构ID
	 */
	private String instId;

	/**
	 * 状态
	 */
	private Integer status;
}