package com.wy.test.core.query;

import java.util.Date;

import dream.flying.flower.framework.web.query.AbstractQuery;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

/**
 * APP登录历史记录查询
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
@Schema(description = "APP登录历史记录查询")
public class HistoryLoginAppQuery extends AbstractQuery {

	private static final long serialVersionUID = 1L;

	@Schema(description = "SESSIONID")
	private String sessionId;

	@Schema(description = "登录时间")
	private Date loginTime;

	@Schema(description = "应用ID")
	private String appId;

	@Schema(description = "应用名称")
	private String appName;

	@Schema(description = "用户ID")
	private String userId;

	@Schema(description = "用户名")
	private String username;

	@Schema(description = "显示名")
	private String displayName;

	@Schema(description = "机构ID")
	private String instId;
}