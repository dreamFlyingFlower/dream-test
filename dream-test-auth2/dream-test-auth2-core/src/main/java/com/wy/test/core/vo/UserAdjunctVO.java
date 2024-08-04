package com.wy.test.core.vo;

import java.io.Serializable;
import java.util.Date;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fhs.core.trans.vo.TransPojo;

import dream.flying.flower.ConstDate;
import dream.flying.flower.framework.web.valid.ValidAdd;
import dream.flying.flower.framework.web.valid.ValidEdit;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 用户扩展信息
 *
 * @author 飞花梦影
 * @date 2024-08-01
 * @git {@link https://github.com/dreamFlyingFlower}
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "用户扩展信息")
public class UserAdjunctVO implements Serializable, TransPojo {

	private static final long serialVersionUID = 1L;

	@Schema(description = "编号")
	@NotNull(message = "id不能为空", groups = { ValidEdit.class })
	private String id;

	@Schema(description = "用户编号")
	@NotNull(message = "用户编号不能为空", groups = { ValidAdd.class })
	private String userId;

	@Schema(description = "机构名称")
	@Size(max = 64, message = "机构名称最大长度不能超过64", groups = { ValidAdd.class, ValidEdit.class })
	private String orgName;

	@Schema(description = "部门编号")
	@Size(max = 64, message = "部门编号最大长度不能超过64", groups = { ValidAdd.class, ValidEdit.class })
	private String departmentId;

	@Schema(description = "部门")
	@Size(max = 64, message = "部门最大长度不能超过64", groups = { ValidAdd.class, ValidEdit.class })
	private String department;

	@Schema(description = "职务")
	@Size(max = 64, message = "职务最大长度不能超过64", groups = { ValidAdd.class, ValidEdit.class })
	private String jobTitle;

	@Schema(description = "工作职级")
	@Size(max = 64, message = "工作职级最大长度不能超过64", groups = { ValidAdd.class, ValidEdit.class })
	private String jobLevel;

	@Schema(description = "经理编号")
	@Size(max = 64, message = "经理编号最大长度不能超过64", groups = { ValidAdd.class, ValidEdit.class })
	private String managerId;

	@Schema(description = "经理名字")
	@Size(max = 64, message = "经理名字最大长度不能超过64", groups = { ValidAdd.class, ValidEdit.class })
	private String manager;

	@Schema(description = "助理编号")
	@Size(max = 64, message = "助理编号最大长度不能超过64", groups = { ValidAdd.class, ValidEdit.class })
	private String assistantId;

	@Schema(description = "助理名字")
	@Size(max = 32, message = "助理名字最大长度不能超过32", groups = { ValidAdd.class, ValidEdit.class })
	private String assistant;

	@Schema(description = "入司日期")
	@JsonFormat(pattern = ConstDate.DATETIME)
	private Date entryDate;

	@Schema(description = "开始工作日期")
	@JsonFormat(pattern = ConstDate.DATETIME)
	private Date startWorkDate;

	@Schema(description = "离职日期")
	@JsonFormat(pattern = ConstDate.DATETIME)
	private Date quitDate;

	@Schema(description = "部门内排序")
	private Integer sortOrder;

	@Schema(description = "工作-邮件")
	@Size(max = 32, message = "工作-邮件最大长度不能超过32", groups = { ValidAdd.class, ValidEdit.class })
	private String workEmail;

	@Schema(description = "工作-电话")
	@Size(max = 32, message = "工作-电话最大长度不能超过32", groups = { ValidAdd.class, ValidEdit.class })
	private String workPhoneNumber;

	@Schema(description = "工作-国家")
	@Size(max = 32, message = "工作-国家最大长度不能超过32", groups = { ValidAdd.class, ValidEdit.class })
	private String workCountry;

	@Schema(description = "分区")
	@Size(max = 32, message = "分区最大长度不能超过32", groups = { ValidAdd.class, ValidEdit.class })
	private String workDivision;

	@Schema(description = "工作-省/市")
	@Size(max = 32, message = "工作-省/市最大长度不能超过32", groups = { ValidAdd.class, ValidEdit.class })
	private String workRegion;

	@Schema(description = "工作-城市")
	@Size(max = 32, message = "工作-城市最大长度不能超过32", groups = { ValidAdd.class, ValidEdit.class })
	private String workLocality;

	@Schema(description = "工作-街道")
	@Size(max = 32, message = "工作-街道最大长度不能超过32", groups = { ValidAdd.class, ValidEdit.class })
	private String workStreetAddress;

	@Schema(description = "工作-地址全称")
	@Size(max = 128, message = "工作-地址全称最大长度不能超过128", groups = { ValidAdd.class, ValidEdit.class })
	private String workAddressFormatted;

	@Schema(description = "工作-邮编")
	@Size(max = 32, message = "工作-邮编最大长度不能超过32", groups = { ValidAdd.class, ValidEdit.class })
	private String workPostalCode;

	@Schema(description = "工作-传真")
	@Size(max = 32, message = "工作-传真最大长度不能超过32", groups = { ValidAdd.class, ValidEdit.class })
	private String workFax;

	@Schema(description = "中心")
	@Size(max = 32, message = "中心最大长度不能超过32", groups = { ValidAdd.class, ValidEdit.class })
	private String costCenter;

	@Schema(description = "备注")
	@Size(max = 256, message = "备注最大长度不能超过256", groups = { ValidAdd.class, ValidEdit.class })
	private String remark;

	@Schema(description = "机构ID")
	@NotNull(message = "机构ID不能为空", groups = { ValidAdd.class })
	private String instId;

	@Schema(description = "机构名称")
	private String instName;

	@Schema(description = "显示名称")
	private String displayName;
}