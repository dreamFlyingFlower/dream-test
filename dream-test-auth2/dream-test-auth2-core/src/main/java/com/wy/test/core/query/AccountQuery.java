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
 * 用户账号表查询
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
@Schema(description = "用户账号表查询")
public class AccountQuery extends AbstractQuery {

	private static final long serialVersionUID = 1L;

	@Schema(description = "用户ID")
	private String userId;

	@Schema(description = "用户名")
	private String username;

	@Schema(description = "用户显示名")
	private String displayName;

	@Schema(description = "策略名称")
	private String strategyName;

	@Schema(description = "ID策略")
	private String strategyId;

	@Schema(description = "应用ID")
	private String appId;

	@Schema(description = "应用名称")
	private String appName;

	@Schema(description = "用户名")
	private String relatedUsername;

	@Schema(description = "密码")
	private String relatedPassword;

	@Schema(description = "创建类型")
	private String createType;

	@Schema(description = "机构ID")
	private String instId;

	@Schema(description = "用户状态")
	private Integer status;
}