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
 * 角色查询
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
@Schema(description = "角色查询")
public class RoleQuery extends AbstractQuery {

	private static final long serialVersionUID = 1L;

	@Schema(description = "角色编码")
	private String roleCode;

	@Schema(description = "角色名称")
	private String roleName;

	@Schema(description = "动态用户组，dynamic动态组 static静态组app应用账号组")
	private String category;

	@Schema(description = "过滤条件SQL")
	private String filters;

	@Schema(description = "机构列表")
	private String orgIdsList;

	@Schema(description = "恢复时间")
	private String resumeTime;

	@Schema(description = "暂停时间")
	private String suspendTime;

	@Schema(description = "是否默认")
	private Integer isDefault;

	@Schema(description = "机构ID")
	private String instId;

	@Schema(description = "备注")
	private String remark;

	@Schema(description = "状态")
	private Integer status;
}