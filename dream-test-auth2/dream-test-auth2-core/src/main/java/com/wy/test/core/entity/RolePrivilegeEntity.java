package com.wy.test.core.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.wy.test.core.web.WebContext;

import dream.flying.flower.framework.mybatis.plus.entity.AbstractEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

/**
 * 角色权限
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
@TableName("auth_role_privilege")
public class RolePrivilegeEntity extends AbstractEntity {

	private static final long serialVersionUID = 1L;

	/**
	 * APP ID
	 */
	private String appId;

	/**
	 * 角色ID
	 */
	private String roleId;

	/**
	 * 资源ID
	 */
	private String resourceId;

	/**
	 * 机构ID
	 */
	private String instId;

	/**
	 * 状态
	 */
	private Integer status;

	public RolePrivilegeEntity(String appId, String roleId, String instId) {
		this.appId = appId;
		this.roleId = roleId;
		this.instId = instId;
	}

	public RolePrivilegeEntity(String appId, String roleId, String resourceId, String instId) {
		this.setId(WebContext.genId());
		this.appId = appId;
		this.roleId = roleId;
		this.resourceId = resourceId;
		this.instId = instId;
	}

	public String getUniqueId() {
		return appId + "_" + roleId + "_" + resourceId;
	}
}