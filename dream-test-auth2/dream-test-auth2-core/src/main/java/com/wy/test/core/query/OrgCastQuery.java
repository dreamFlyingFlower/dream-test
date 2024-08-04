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
 * 机构映射表查询
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
@Schema(description = "机构映射表查询")
public class OrgCastQuery extends AbstractQuery {

	private static final long serialVersionUID = 1L;

	@Schema(description = "机构编码")
	private String code;

	@Schema(description = "机构名称")
	private String name;

	@Schema(description = "机构全名")
	private String fullName;

	@Schema(description = "父级ID")
	private String parentId;

	@Schema(description = "父级名称")
	private String parentName;

	@Schema(description = "CODE路径")
	private String codePath;

	@Schema(description = "名称路径")
	private String namePath;

	@Schema(description = "机构提供者")
	private String provider;

	@Schema(description = "机构ID")
	private String orgId;

	@Schema(description = "机构PARENTID")
	private String orgParentId;

	@Schema(description = "机构ID")
	private String instId;

	@Schema(description = "排序")
	private Integer sortIndex;

	@Schema(description = "状态")
	private Integer status;
}