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
 * 机构映射表
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
@TableName("auth_org_cast")
public class OrgCastEntity extends AbstractStringEntity {

	private static final long serialVersionUID = -273110777391507134L;

	/**
	 * 机构编码
	 */
	private String code;

	/**
	 * 机构名称
	 */
	private String name;

	/**
	 * 机构全名
	 */
	private String fullName;

	/**
	 * 父级ID
	 */
	private String parentId;

	/**
	 * 父级名称
	 */
	private String parentName;

	/**
	 * CODE路径
	 */
	private String codePath;

	/**
	 * 名称路径
	 */
	private String namePath;

	/**
	 * 机构提供者
	 */
	private String provider;

	/**
	 * 机构ID
	 */
	private String orgId;

	/**
	 * 机构PARENTID
	 */
	private String orgParentId;

	/**
	 * 应用ID
	 */
	private String appId;

	/**
	 * 应用名称
	 */
	private String appName;

	/**
	 * 机构ID
	 */
	private String instId;

	/**
	 * 排序
	 */
	private Integer sortIndex;

	/**
	 * 状态
	 */
	private Integer status;
}