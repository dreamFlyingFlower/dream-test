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
 * 机构
 *
 * @author 飞花梦影
 * @date 2024-08-01
 * @git {@link https://github.com/dreamFlyingFlower}
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "机构")
public class InstitutionVO implements Serializable, TransPojo {

	private static final long serialVersionUID = 1L;

	@Schema(description = "主键")
	@NotNull(message = "id不能为空", groups = { ValidEdit.class })
	private String id;

	@Schema(description = "机构名称")
	@NotBlank(message = "机构名称不能为空", groups = { ValidAdd.class })
	@Size(max = 64, message = "机构名称最大长度不能超过64", groups = { ValidAdd.class, ValidEdit.class })
	private String name;

	@Schema(description = "机构全名")
	@Size(max = 128, message = "机构全名最大长度不能超过128", groups = { ValidAdd.class, ValidEdit.class })
	private String fullName;

	@Schema(description = "分区")
	@Size(max = 64, message = "分区最大长度不能超过64", groups = { ValidAdd.class, ValidEdit.class })
	private String division;

	@Schema(description = "国家")
	@Size(max = 64, message = "国家最大长度不能超过64", groups = { ValidAdd.class, ValidEdit.class })
	private String country;

	@Schema(description = "省")
	@Size(max = 32, message = "省最大长度不能超过32", groups = { ValidAdd.class, ValidEdit.class })
	private String region;

	@Schema(description = "城市")
	@Size(max = 32, message = "城市最大长度不能超过32", groups = { ValidAdd.class, ValidEdit.class })
	private String locality;

	@Schema(description = "街道")
	@Size(max = 32, message = "街道最大长度不能超过32", groups = { ValidAdd.class, ValidEdit.class })
	private String street;

	@Schema(description = "地址")
	@Size(max = 128, message = "地址最大长度不能超过128", groups = { ValidAdd.class, ValidEdit.class })
	private String address;

	@Schema(description = "联系人")
	@Size(max = 32, message = "联系人最大长度不能超过32", groups = { ValidAdd.class, ValidEdit.class })
	private String contact;

	@Schema(description = "邮编")
	@Size(max = 32, message = "邮编最大长度不能超过32", groups = { ValidAdd.class, ValidEdit.class })
	private String postalCode;

	@Schema(description = "电话")
	@Size(max = 32, message = "电话最大长度不能超过32", groups = { ValidAdd.class, ValidEdit.class })
	private String phone;

	@Schema(description = "传真")
	@Size(max = 32, message = "传真最大长度不能超过32", groups = { ValidAdd.class, ValidEdit.class })
	private String fax;

	@Schema(description = "邮箱")
	@Size(max = 32, message = "邮箱最大长度不能超过32", groups = { ValidAdd.class, ValidEdit.class })
	private String email;

	@Schema(description = "logo")
	@Size(max = 128, message = "logo最大长度不能超过128", groups = { ValidAdd.class, ValidEdit.class })
	private String logo;

	@Schema(description = "域名")
	@Size(max = 128, message = "域名最大长度不能超过128", groups = { ValidAdd.class, ValidEdit.class })
	private String domain;

	@Schema(description = "前端标题")
	@Size(max = 128, message = "前端标题最大长度不能超过128", groups = { ValidAdd.class, ValidEdit.class })
	private String frontTitle;

	@Schema(description = "控制台域名")
	@Size(max = 32, message = "控制台域名最大长度不能超过32", groups = { ValidAdd.class, ValidEdit.class })
	private String consoleDomain;

	@Schema(description = "控制台标题")
	@Size(max = 128, message = "控制台标题最大长度不能超过128", groups = { ValidAdd.class, ValidEdit.class })
	private String consoleTitle;

	@Schema(description = "验证码")
	@Size(max = 32, message = "验证码最大长度不能超过32", groups = { ValidAdd.class, ValidEdit.class })
	private String captcha;

	@Schema(description = "默认URI")
	@Size(max = 256, message = "默认URI最大长度不能超过256", groups = { ValidAdd.class, ValidEdit.class })
	private String defaultUri;

	@Schema(description = "描述")
	@Size(max = 256, message = "描述最大长度不能超过256", groups = { ValidAdd.class, ValidEdit.class })
	private String remark;

	@Schema(description = "排序")
	private Integer sortIndex;

	@Schema(description = "机构ID")
	private String instId;

	@Schema(description = "状态")
	@NotNull(message = "状态不能为空", groups = { ValidAdd.class })
	private Integer status;
}