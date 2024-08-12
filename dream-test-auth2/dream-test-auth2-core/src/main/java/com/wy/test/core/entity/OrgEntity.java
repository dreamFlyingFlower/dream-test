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
 * 组织机构,根code:id = instId or id = parentId or parentId = -1 or parentId = 0
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
@TableName("auth_org")
public class OrgEntity extends AbstractStringEntity {

	private static final long serialVersionUID = -7477102351005000328L;

	/**
	 * 组织机构编码
	 */
	private String orgCode;

	/**
	 * 组织机构简称
	 */
	private String orgName;

	/**
	 * 组织机构全称
	 */
	private String fullName;

	/**
	 * 组织机构类型:1-entity;2-virtual
	 */
	private String type;

	/**
	 * 组织机构等级
	 */
	private Integer level;

	/**
	 * 上级组织机构
	 */
	private String parentId;

	/**
	 * 上级组织机构编码
	 */
	private String parentCode;

	/**
	 * 上级组织机构名称
	 */
	private String parentName;

	/**
	 * 组织机构编码路径
	 */
	private String codePath;

	/**
	 * 组织机构名称路径
	 */
	private String namePath;

	/**
	 * 组织机构地址
	 */
	private String address;

	/**
	 * 邮政编码
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
	 * 排序
	 */
	private Integer sortIndex;

	/**
	 * 分部
	 */
	private String division;

	/**
	 * 国家
	 */
	private String country;

	/**
	 * 区
	 */
	private String region;

	/**
	 * 地区
	 */
	private String locality;

	/**
	 * 街道
	 */
	private String street;

	/**
	 * 是否有下级
	 */
	private String hasChild;

	/**
	 * 联系人姓名
	 */
	private String contact;

	/**
	 * 邮箱
	 */
	private String email;

	/**
	 * LDAP DN
	 */
	private String ldapDn;

	/**
	 * 机构ID
	 */
	private String instId;

	/**
	 * 描述
	 */
	private String remark;

	/**
	 * 状态
	 */
	private Integer status;
}