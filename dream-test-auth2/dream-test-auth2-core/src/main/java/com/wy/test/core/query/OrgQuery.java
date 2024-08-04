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
 * 组织机构查询
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
@Schema(description = "组织机构查询")
public class OrgQuery extends AbstractQuery {

	private static final long serialVersionUID = 1L;

	@Schema(description = "组织机构编码")
	private String orgCode;

	@Schema(description = "组织机构简称")
	private String orgName;

	@Schema(description = "组织机构全称")
	private String fullName;

	@Schema(description = "组织机构类型")
	private String type;

	@Schema(description = "组织机构等级")
	private Integer level;

	@Schema(description = "上级组织机构")
	private String parentId;

	@Schema(description = "上级组织机构编码")
	private String parentCode;

	@Schema(description = "上级组织机构名称")
	private String parentName;

	@Schema(description = "组织机构编码路径")
	private String codePath;

	@Schema(description = "组织机构名称路径")
	private String namePath;

	@Schema(description = "组织机构地址")
	private String address;

	@Schema(description = "邮政编码")
	private String postalCode;

	@Schema(description = "电话")
	private String phone;

	@Schema(description = "传真")
	private String fax;

	@Schema(description = "排序")
	private Integer sortIndex;

	@Schema(description = "分部")
	private String division;

	@Schema(description = "国家")
	private String country;

	@Schema(description = "区")
	private String region;

	@Schema(description = "地区")
	private String locality;

	@Schema(description = "街道")
	private String street;

	@Schema(description = "是否有下级")
	private String hasChild;

	@Schema(description = "联系人姓名")
	private String contact;

	@Schema(description = "邮箱")
	private String email;

	@Schema(description = "LDAP DN")
	private String ldapDn;

	@Schema(description = "机构ID")
	private String instId;

	@Schema(description = "描述")
	private String remark;

	@Schema(description = "状态")
	private Integer status;
}