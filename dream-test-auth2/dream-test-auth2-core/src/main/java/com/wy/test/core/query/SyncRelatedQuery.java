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
 * 同步关联查询
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
@Schema(description = "同步关联查询")
public class SyncRelatedQuery extends AbstractQuery {

	private static final long serialVersionUID = 1L;

	@Schema(description = "对象ID")
	private String objectId;

	@Schema(description = "对象名称")
	private String objectName;

	@Schema(description = "对象展示名称")
	private String objectDisplayName;

	@Schema(description = "对象类型")
	private String objectType;

	@Schema(description = "同步ID")
	private String syncId;

	@Schema(description = "同步名称")
	private String syncName;

	@Schema(description = "原始ID")
	private String originId;

	@Schema(description = "原始ID2")
	private String originId2;

	@Schema(description = "原始ID3")
	private String originId3;

	@Schema(description = "同步时间")
	private String syncTime;

	@Schema(description = "原始名称")
	private String originName;

	@Schema(description = "机构ID")
	private String instId;
}