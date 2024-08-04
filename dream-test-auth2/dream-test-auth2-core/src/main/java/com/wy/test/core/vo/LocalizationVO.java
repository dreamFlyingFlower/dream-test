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
 * 国际化
 *
 * @author 飞花梦影
 * @date 2024-08-01
 * @git {@link https://github.com/dreamFlyingFlower}
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "国际化")
public class LocalizationVO implements Serializable, TransPojo {

	private static final long serialVersionUID = 1L;

	@Schema(description = "主键")
	@NotNull(message = "id不能为空", groups = { ValidEdit.class })
	private String id;

	@Schema(description = "属性")
	@Size(max = 200, message = "属性最大长度不能超过200", groups = { ValidAdd.class, ValidEdit.class })
	private String property;

	@Schema(description = "中文")
	@Size(max = 500, message = "中文最大长度不能超过500", groups = { ValidAdd.class, ValidEdit.class })
	private String langZh;

	@Schema(description = "英文")
	@Size(max = 500, message = "英文最大长度不能超过500", groups = { ValidAdd.class, ValidEdit.class })
	private String langEn;

	@Schema(description = "机构ID")
	@Size(max = 45, message = "机构ID最大长度不能超过45", groups = { ValidAdd.class, ValidEdit.class })
	private String instId;

	@Schema(description = "备注")
	@Size(max = 500, message = "备注最大长度不能超过500", groups = { ValidAdd.class, ValidEdit.class })
	private String remark;

	@Schema(description = "状态")
	private Integer status;
}