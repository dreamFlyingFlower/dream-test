package com.wy.test.core.query;

import dream.flying.flower.framework.web.query.AbstractQuery;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

/**
 * 同步器查询
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
@Schema(description = "同步器查询")
public class SyncQuery extends AbstractQuery {

	private static final long serialVersionUID = 1L;

	@Schema(description = "名称")
	private String name;

	@Schema(description = "服务")
	private String service;

	@Schema(description = "资源类型")
	private String sourceType;

	@Schema(description = "定时任务")
	private String scheduler;

	@Schema(description = "提供者URL")
	private String providerUrl;

	@Schema(description = "数据库驱动类")
	private String driverClass;

	@Schema(description = "用户名")
	private String principal;

	@Schema(description = "密码")
	private String credentials;

	@Schema(description = "恢复时间")
	private String resumeTime;

	@Schema(description = "暂停时间")
	private String suspendTime;

	@Schema(description = "用户基础信息")
	private String userBasedn;

	@Schema(description = "用户拦截")
	private String userFilters;

	@Schema(description = "组织机构基础信息")
	private String orgBasedn;

	@Schema(description = "组织机构拦截")
	private String orgFilters;

	@Schema(description = "MSAD域名")
	private String msadDomain;

	@Schema(description = "是否开启SSL")
	private Integer sslSwitch;

	@Schema(description = "信任STORE")
	private String trustStore;

	@Schema(description = "信任STORE密码")
	private String trustStorePassword;

	@Schema(description = "同步时间范围（单位天）")
	private Integer syncStartTime;

	@Schema(description = "APP ID")
	private String appId;

	@Schema(description = "APP名称")
	private String appName;

	@Schema(description = "机构ID")
	private String instId;

	@Schema(description = "备注")
	private String remark;

	@Schema(description = "状态")
	private Integer status;
}