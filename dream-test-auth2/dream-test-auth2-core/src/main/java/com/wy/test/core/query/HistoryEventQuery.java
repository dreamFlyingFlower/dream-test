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
 * 历史事件查询
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
@Schema(description = "历史事件查询")
public class HistoryEventQuery extends AbstractQuery {

	private static final long serialVersionUID = 1L;

	@Schema(description = "事件名")
	private String eventName;

	@Schema(description = "数据类型")
	private String dataType;

	@Schema(description = "数据数量")
	private Integer dataCount;

	@Schema(description = "执行时间")
	private Date executeTime;

	@Schema(description = "机构ID")
	private Long instId;
}