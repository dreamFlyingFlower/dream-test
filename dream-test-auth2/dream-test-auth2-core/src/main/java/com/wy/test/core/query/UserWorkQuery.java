package com.wy.test.core.query;

import java.util.Date;

import dream.flying.flower.framework.web.query.AbstractQuery;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

/**
 * 用户信息查询
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
@Schema(description = "用户信息查询")
public class UserWorkQuery extends AbstractQuery {

	private static final long serialVersionUID = 1L;

	@Schema(description = "用户编号")
	private Long userId;

	@Schema(description = "工号")
	private String employeeNumber;

	@Schema(description = "机构")
	private String organization;

	@Schema(description = "部门编号")
	private Long departmentId;

	@Schema(description = "部门")
	private String department;

	@Schema(description = "职务")
	private String jobTitle;

	@Schema(description = "工作职级")
	private String jobLevel;

	@Schema(description = "经理编号")
	private String managerId;

	@Schema(description = "经理名字")
	private String manager;

	@Schema(description = "助理编号")
	private String assistantId;

	@Schema(description = "助理名字")
	private String assistant;

	@Schema(description = "入司时间")
	private Date entryDate;

	@Schema(description = "开始工作时间")
	private Date startWorkDate;

	@Schema(description = "离职日期")
	private Date quitDate;

	@Schema(description = "工作-邮件")
	private String workEmail;

	@Schema(description = "工作-电话")
	private String workPhoneNumber;

	@Schema(description = "工作-国家")
	private String workCountry;

	@Schema(description = "工作-省/市")
	private String workRegion;

	@Schema(description = "工作-城市")
	private String workLocality;

	@Schema(description = "工作-街道")
	private String workStreetAddress;

	@Schema(description = "工作-地址全称")
	private String workAddressFormatted;

	@Schema(description = "工作-邮编")
	private String workPostalCode;

	@Schema(description = "工作-传真")
	private String workFax;

	@Schema(description = "工作-公司名称")
	private String workOfficeName;

	@Schema(description = "备注")
	private String remark;
}