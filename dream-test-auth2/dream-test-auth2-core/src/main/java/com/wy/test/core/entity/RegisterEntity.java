package com.wy.test.core.entity;

import com.baomidou.mybatisplus.annotation.TableName;

import dream.flying.flower.framework.mybatis.plus.entity.AbstractEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

/**
 * 用户注册
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
@TableName("auth_register")
public class RegisterEntity extends AbstractEntity {

	private static final long serialVersionUID = 1L;

	/**
	 * 显示名
	 */
	private String displayName;

	/**
	 * 工作邮箱
	 */
	private String workEmail;

	/**
	 * 工作电话
	 */
	private String workPhone;

	/**
	 * 员工ID
	 */
	private Integer employees;

	/**
	 * 机构ID
	 */
	private String instId;

	/**
	 * 机构名称
	 */
	private String instName;

	/**
	 * 状态
	 */
	private Integer status;
}