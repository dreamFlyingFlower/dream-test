package com.wy.test.core.query;

import dream.flying.flower.db.annotation.Query;
import dream.flying.flower.db.enums.QueryType;
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
	@Query(type = QueryType.LIKE)
	private String name;

	@Schema(description = "服务")
	@Query
	private String service;

	@Schema(description = "资源类型")
	@Query
	private String sourceType;

	@Schema(description = "定时任务")
	@Query
	private String scheduler;

	@Schema(description = "提供者URL")
	@Query
	private String providerUrl;

	@Schema(description = "数据库驱动类")
	@Query
	private String driverClass;

	@Schema(description = "用户名")
	@Query
	private String principal;

	@Schema(description = "密码")
	@Query
	private String credentials;

	@Schema(description = "恢复时间")
	@Query
	private String resumeTime;

	@Schema(description = "暂停时间")
	@Query
	private String suspendTime;

	@Schema(description = "用户基础信息")
	@Query
	private String userBasedn;

	@Schema(description = "用户拦截")
	@Query
	private String userFilters;

	@Schema(description = "组织机构基础信息")
	@Query
	private String orgBasedn;

	@Schema(description = "组织机构拦截")
	@Query
	private String orgFilters;

	@Schema(description = "MSAD域名")
	@Query
	private String msadDomain;

	@Schema(description = "是否开启SSL")
	@Query
	private Integer sslSwitch;

	@Schema(description = "信任STORE")
	@Query
	private String trustStore;

	@Schema(description = "信任STORE密码")
	@Query
	private String trustStorePassword;

	@Schema(description = "同步时间范围,单位天")
	@Query
	private Integer syncStartTime;

	@Schema(description = "应用标识")
	@Query
	private String appId;

	@Schema(description = "应用名称")
	@Query
	private String appName;

	@Schema(description = "机构ID")
	@Query
	private String instId;

	@Schema(description = "备注")
	@Query
	private String remark;

	@Schema(description = "状态")
	@Query
	private Integer status;
}