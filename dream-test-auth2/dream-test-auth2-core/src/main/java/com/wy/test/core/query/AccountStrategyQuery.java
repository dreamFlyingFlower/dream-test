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
 * 用户账号策略
 *
 * @author 飞花梦影
 * @date 2024-08-03 16:06:56
 * @git {@link https://github.com/dreamFlyingFlower}
 */
@Getter
@Setter
@ToString
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "用户账号策略查询")
public class AccountStrategyQuery extends AbstractQuery {

	private static final long serialVersionUID = -8743329570694948718L;

	@Schema(description = "策略名称")
	private String name;

	@Schema(description = "属性")
	private String mapping;

	@Schema(description = "拦截器")
	private String filters;

	@Schema(description = "组织机构ID列表")
	private String orgIds;

	@Schema(description = "后缀")
	private String suffixes;

	@Schema(description = "应用ID")
	private String appId;

	@Schema(description = "应用名称")
	private String appName;

	@Schema(description = "创建类型")
	private String createType;

	@Schema(description = "机构ID")
	private String instId;

	@Schema(description = "备注")
	private String remark;

	@Schema(description = "状态")
	private Integer status;
}