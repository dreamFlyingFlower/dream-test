package com.wy.test.core.vo;

import java.io.Serializable;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fhs.core.trans.vo.TransPojo;

import dream.flying.flower.framework.web.valid.ValidAdd;
import dream.flying.flower.framework.web.valid.ValidEdit;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 组织机构
 *
 * @author 飞花梦影
 * @date 2024-08-01
 * @git {@link https://github.com/dreamFlyingFlower}
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "组织机构")
public class OrgVO implements Serializable, TransPojo {

	private static final long serialVersionUID = 1L;

	@Schema(description = "主键")
	@NotNull(message = "id不能为空", groups = { ValidEdit.class })
	private String id;

	@Schema(description = "组织机构编码")
	@NotBlank(message = "组织机构编码不能为空", groups = { ValidAdd.class })
	@Size(max = 32, message = "组织机构编码最大长度不能超过32", groups = { ValidAdd.class, ValidEdit.class })
	private String orgCode;

	@Schema(description = "组织机构简称")
	@NotBlank(message = "组织机构简称不能为空", groups = { ValidAdd.class })
	@Size(max = 64, message = "组织机构简称最大长度不能超过64", groups = { ValidAdd.class, ValidEdit.class })
	private String orgName;

	@Schema(description = "组织机构全称")
	@Size(max = 128, message = "组织机构全称最大长度不能超过128", groups = { ValidAdd.class, ValidEdit.class })
	private String fullName;

	@Schema(description = "组织机构类型")
	@Size(max = 45, message = "组织机构类型最大长度不能超过45", groups = { ValidAdd.class, ValidEdit.class })
	private String type;

	@Schema(description = "组织机构等级")
	private Integer level;

	@Schema(description = "上级组织机构")
	private String parentId;

	@Schema(description = "上级组织机构编码")
	@Size(max = 32, message = "上级组织机构编码最大长度不能超过32", groups = { ValidAdd.class, ValidEdit.class })
	private String parentCode;

	@Schema(description = "上级组织机构名称")
	@Size(max = 128, message = "上级组织机构名称最大长度不能超过128", groups = { ValidAdd.class, ValidEdit.class })
	private String parentName;

	@Schema(description = "组织机构编码路径")
	@Size(max = 256, message = "组织机构编码路径最大长度不能超过256", groups = { ValidAdd.class, ValidEdit.class })
	private String codePath;

	@Schema(description = "组织机构名称路径")
	@Size(max = 512, message = "组织机构名称路径最大长度不能超过512", groups = { ValidAdd.class, ValidEdit.class })
	private String namePath;

	@Schema(description = "组织机构地址")
	@Size(max = 256, message = "组织机构地址最大长度不能超过256", groups = { ValidAdd.class, ValidEdit.class })
	private String address;

	@Schema(description = "邮政编码")
	@Size(max = 45, message = "邮政编码最大长度不能超过45", groups = { ValidAdd.class, ValidEdit.class })
	private String postalCode;

	@Schema(description = "电话")
	@Size(max = 256, message = "电话最大长度不能超过256", groups = { ValidAdd.class, ValidEdit.class })
	private String phone;

	@Schema(description = "传真")
	@Size(max = 128, message = "传真最大长度不能超过128", groups = { ValidAdd.class, ValidEdit.class })
	private String fax;

	@Schema(description = "排序")
	private Integer sortIndex;

	@Schema(description = "分部")
	@Size(max = 45, message = "分部最大长度不能超过45", groups = { ValidAdd.class, ValidEdit.class })
	private String division;

	@Schema(description = "国家")
	@Size(max = 45, message = "国家最大长度不能超过45", groups = { ValidAdd.class, ValidEdit.class })
	private String country;

	@Schema(description = "区")
	@Size(max = 45, message = "区最大长度不能超过45", groups = { ValidAdd.class, ValidEdit.class })
	private String region;

	@Schema(description = "地区")
	@Size(max = 45, message = "地区最大长度不能超过45", groups = { ValidAdd.class, ValidEdit.class })
	private String locality;

	@Schema(description = "街道")
	@Size(max = 45, message = "街道最大长度不能超过45", groups = { ValidAdd.class, ValidEdit.class })
	private String street;

	@Schema(description = "是否有下级")
	@Size(max = 45, message = "是否有下级最大长度不能超过45", groups = { ValidAdd.class, ValidEdit.class })
	private String hasChild;

	@Schema(description = "联系人姓名")
	@Size(max = 45, message = "联系人姓名最大长度不能超过45", groups = { ValidAdd.class, ValidEdit.class })
	private String contact;

	@Schema(description = "邮箱")
	@Size(max = 64, message = "邮箱最大长度不能超过64", groups = { ValidAdd.class, ValidEdit.class })
	private String email;

	@Schema(description = "LDAP DN")
	@Size(max = 1000, message = "LDAP DN最大长度不能超过1,000", groups = { ValidAdd.class, ValidEdit.class })
	private String ldapDn;

	@Schema(description = "机构ID")
	@NotNull(message = "机构ID不能为空", groups = { ValidAdd.class })
	@Size(max = 45, message = "机构ID最大长度不能超过45", groups = { ValidAdd.class, ValidEdit.class })
	private String instId;

	@Schema(description = "描述")
	@Size(max = 200, message = "描述最大长度不能超过200", groups = { ValidAdd.class, ValidEdit.class })
	private String remark;

	@Schema(description = "状态")
	private Integer status;

	@Schema(description = "机构名称")
	private String instName;

	@Schema(description = "同步ID")
	String syncId;

	@Schema(description = "同步名称")
	String syncName;

	@Schema(description = "原始ID1")
	String originId;

	@Schema(description = "原始ID2")
	String originId2;

	@Schema(description = "是否为主要数据:0-否;1-shi")
	@Builder.Default
	private int isPrimary = 0;

	@Schema(description = "是否覆盖原机构名称路径")
	private boolean reorgNamePath;
}