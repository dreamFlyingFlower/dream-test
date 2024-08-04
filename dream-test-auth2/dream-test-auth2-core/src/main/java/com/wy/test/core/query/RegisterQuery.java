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
 * 用户注册查询
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
@Schema(description = "用户注册查询")
public class RegisterQuery extends AbstractQuery {

	private static final long serialVersionUID = 1L;

	@Schema(description = "显示名")
	private String displayName;

	@Schema(description = "工作邮箱")
	private String workEmail;

	@Schema(description = "工作电话")
	private String workPhone;

	@Schema(description = "员工ID")
	private Integer employees;

	@Schema(description = "机构ID")
	private String instId;

	@Schema(description = "机构名称")
	private String instName;

	@Schema(description = "状态")
	private Integer status;
}