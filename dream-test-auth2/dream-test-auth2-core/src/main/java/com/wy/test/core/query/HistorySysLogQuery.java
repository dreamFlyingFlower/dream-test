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
 * 系统操作日志查询
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
@Schema(description = "系统操作日志查询")
public class HistorySysLogQuery extends AbstractQuery {

	private static final long serialVersionUID = 1L;

	@Schema(description = "主题")
	private String topic;

	@Schema(description = "消息")
	private String message;

	@Schema(description = "消息类型")
	private String messageAction;

	@Schema(description = "消息结果")
	private String messageResult;

	@Schema(description = "用户ID")
	private String userId;

	@Schema(description = "用户名")
	private String username;

	@Schema(description = "显示名")
	private String displayName;

	@Schema(description = "执行时间")
	private Date executeTime;

	@Schema(description = "机构ID")
	private String instId;
}