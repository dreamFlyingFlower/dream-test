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
 * 角色
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
@TableName("auth_role")
public class RoleEntity extends AbstractEntity {

	private static final long serialVersionUID = 1L;

	public class Category {

		public static final String DYNAMIC = "dynamic";

		public static final String STATIC = "static";

		public static final String APP = "app";
	}

	/**
	 * 角色编码
	 */
	private String roleCode;

	/**
	 * 角色名称
	 */
	private String roleName;

	/**
	 * 动态用户组，dynamic动态组 static静态组app应用账号组
	 */
	private String category;

	/**
	 * 过滤条件SQL
	 */
	private String filters;

	/**
	 * 机构列表
	 */
	private String orgIdsList;

	/**
	 * 恢复时间
	 */
	private String resumeTime;

	/**
	 * 暂停时间
	 */
	private String suspendTime;

	/**
	 * 是否默认
	 */
	private Integer isDefault;

	/**
	 * 机构ID
	 */
	private String instId;

	/**
	 * 备注
	 */
	private String remark;

	/**
	 * 状态
	 */
	private Integer status;

	public RoleEntity(String id) {
		this.id = id;
	}

	public RoleEntity(String id, String roleCode, String roleName, int isDefault) {
		super();
		this.id = id;
		this.roleCode = roleCode;
		this.roleName = roleName;
		this.isDefault = isDefault;
	}

	/**
	 * ROLE_ALL_USER must be 1, dynamic 2, all orgIdsList 3, not filters
	 */
	public void setDefaultAllUser() {
		this.category = "dynamic";
		this.orgIdsList = "";
		this.filters = "";
	}
}