package com.wy.test.core.vo;

import java.io.Serializable;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fhs.core.trans.vo.TransPojo;

import dream.flying.flower.framework.web.valid.ValidAdd;
import dream.flying.flower.framework.web.valid.ValidEdit;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 用户注册
 *
 * @author 飞花梦影
 * @date 2024-08-01
 * @git {@link https://github.com/dreamFlyingFlower}
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "用户注册")
public class RegisterVO implements Serializable, TransPojo {

	private static final long serialVersionUID = 1L;

	@Schema(description = "主键")
	@NotNull(message = "id不能为空", groups = { ValidEdit.class })
	private String id;

	@Schema(description = "显示名")
	@Size(max = 32, message = "显示名最大长度不能超过32", groups = { ValidAdd.class, ValidEdit.class })
	private String displayName;

	@Schema(description = "工作邮箱")
	@Size(max = 32, message = "工作邮箱最大长度不能超过32", groups = { ValidAdd.class, ValidEdit.class })
	private String workEmail;

	@Schema(description = "工作电话")
	@Size(max = 32, message = "工作电话最大长度不能超过32", groups = { ValidAdd.class, ValidEdit.class })
	private String workPhone;

	@Schema(description = "员工ID")
	private Integer employees;

	@Schema(description = "机构名称")
	@NotNull(message = "机构ID不能为空", groups = { ValidAdd.class })
	private String instId;

	@Schema(description = "状态")
	private Integer status;

	@Schema(description = "机构名称")
	private String instName;
}