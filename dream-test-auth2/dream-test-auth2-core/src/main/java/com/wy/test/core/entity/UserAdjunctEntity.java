package com.wy.test.core.entity;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.TableName;

import dream.flying.flower.framework.mybatis.plus.entity.AbstractStringEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

/**
 * 用户扩展信息
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
@TableName("auth_user_adjunct")
public class UserAdjunctEntity extends AbstractStringEntity {

	private static final long serialVersionUID = 1L;

	/**
	 * 用户编号
	 */
	private String userId;

	/**
	 * 机构名称
	 */
	private String orgName;

	/**
	 * 部门编号
	 */
	private String departmentId;

	/**
	 * 部门
	 */
	private String department;

	/**
	 * 职务
	 */
	private String jobTitle;

	/**
	 * 工作职级
	 */
	private String jobLevel;

	/**
	 * 经理编号
	 */
	private String managerId;

	/**
	 * 经理名字
	 */
	private String manager;

	/**
	 * 助理编号
	 */
	private String assistantId;

	/**
	 * 助理名字
	 */
	private String assistant;

	/**
	 * 入司日期
	 */
	private Date entryDate;

	/**
	 * 开始工作日期
	 */
	private Date startWorkDate;

	/**
	 * 离职日期
	 */
	private Date quitDate;

	/**
	 * 部门内排序
	 */
	private Integer sortOrder;

	/**
	 * 工作-邮件
	 */
	private String workEmail;

	/**
	 * 工作-电话
	 */
	private String workPhoneNumber;

	/**
	 * 工作-国家
	 */
	private String workCountry;

	/**
	 * 分区
	 */
	private String workDivision;

	/**
	 * 工作-省/市
	 */
	private String workRegion;

	/**
	 * 工作-城市
	 */
	private String workLocality;

	/**
	 * 工作-街道
	 */
	private String workStreetAddress;

	/**
	 * 工作-地址全称
	 */
	private String workAddressFormatted;

	/**
	 * 工作-邮编
	 */
	private String workPostalCode;

	/**
	 * 工作-传真
	 */
	private String workFax;

	/**
	 * 中心
	 */
	private String costCenter;

	/**
	 * 备注
	 */
	private String remark;

	/**
	 * 机构ID
	 */
	private String instId;
}