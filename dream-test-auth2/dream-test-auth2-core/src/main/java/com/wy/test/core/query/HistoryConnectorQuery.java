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
 * 历史连接器查询
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
@Schema(description = "历史连接器查询")
public class HistoryConnectorQuery extends AbstractQuery {

	private static final long serialVersionUID = 1L;

	@Schema(description = "连接名")
	private String connName;

	@Schema(description = "资源ID")
	private String sourceId;

	@Schema(description = "资源名")
	private String sourceName;

	@Schema(description = "对象ID")
	private String objectId;

	@Schema(description = "对象名")
	private String objectName;

	@Schema(description = "同步时间")
	private String syncTime;

	@Schema(description = "同步结果")
	private String result;

	@Schema(description = "主题")
	private String topic;

	@Schema(description = "动作类型")
	private String actionType;

	@Schema(description = "机构ID")
	private String instId;

	@Schema(description = "备注")
	private String remark;
}