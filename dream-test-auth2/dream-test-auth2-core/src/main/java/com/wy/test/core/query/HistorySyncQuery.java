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
 * 同步日志查询
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
@Schema(description = "同步日志查询")
public class HistorySyncQuery extends AbstractQuery {

	private static final long serialVersionUID = 1L;

	@Schema(description = "同步ID")
	private String syncId;

	@Schema(description = "同步名")
	private String syncName;

	@Schema(description = "对象ID")
	private String objectId;

	@Schema(description = "对象名")
	private String objectName;

	@Schema(description = "对象类型")
	private String objectType;

	@Schema(description = "同步时间")
	private Date syncTime;

	@Schema(description = "结果")
	private String result;

	@Schema(description = "机构ID")
	private String instId;
}