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
 * 连接器查询
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
@Schema(description = "连接器查询")
public class ConnectorQuery extends AbstractQuery {

	private static final long serialVersionUID = 1L;

	@Schema(description = "连接名")
	private String connName;

	@Schema(description = "是否正在连接")
	private Integer justInTime;

	@Schema(description = "定时")
	private String scheduler;

	@Schema(description = "提供者URL")
	private String providerUrl;

	@Schema(description = "用户名")
	private String principal;

	@Schema(description = "密码")
	private String credentials;

	@Schema(description = "拦截")
	private String filters;

	@Schema(description = "备注")
	private String remark;

	@Schema(description = "APP ID")
	private String appId;

	@Schema(description = "APP名称")
	private String appName;

	@Schema(description = "机构ID")
	private String instId;

	@Schema(description = "状态")
	private Integer status;
}