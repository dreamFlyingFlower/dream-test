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
 * 同步器
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
@TableName("auth_sync")
public class SyncEntity extends AbstractEntity {

	private static final long serialVersionUID = 1L;

	/**
	 * 名称
	 */
	private String name;

	/**
	 * 服务
	 */
	private String service;

	/**
	 * 资源类型
	 */
	private String sourceType;

	/**
	 * 定时任务
	 */
	private String scheduler;

	/**
	 * 提供者URL
	 */
	private String providerUrl;

	/**
	 * 数据库驱动类
	 */
	private String driverClass;

	/**
	 * 用户名
	 */
	private String principal;

	/**
	 * 密码
	 */
	private String credentials;

	/**
	 * 恢复时间
	 */
	private String resumeTime;

	/**
	 * 暂停时间
	 */
	private String suspendTime;

	/**
	 * 用户基础信息
	 */
	private String userBasedn;

	/**
	 * 用户拦截
	 */
	private String userFilters;

	/**
	 * 组织机构基础信息
	 */
	private String orgBasedn;

	/**
	 * 组织机构拦截
	 */
	private String orgFilters;

	/**
	 * MSAD域名
	 */
	private String msadDomain;

	/**
	 * 是否开启SSL
	 */
	private Integer sslSwitch;

	/**
	 * 信任STORE
	 */
	private String trustStore;

	/**
	 * 信任STORE密码
	 */
	private String trustStorePassword;

	/**
	 * 同步时间范围（单位天）
	 */
	private Integer syncStartTime;

	/**
	 * APP ID
	 */
	private String appId;

	/**
	 * APP名称
	 */
	private String appName;

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
}