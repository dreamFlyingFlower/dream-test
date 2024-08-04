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
 * 应用适配
 *
 * @author 飞花梦影
 * @date 2024-08-01
 * @git {@link https://github.com/dreamFlyingFlower}
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "应用适配")
public class AppAdapterVO implements Serializable, TransPojo {

	private static final long serialVersionUID = 1L;

	@Schema(description = "主键")
	@NotNull(message = "id不能为空", groups = { ValidEdit.class })
	private String id;

	@Schema(description = "名称")
	@Size(max = 32, message = "名称最大长度不能超过32", groups = { ValidAdd.class, ValidEdit.class })
	private String name;

	@Schema(description = "协议")
	@Size(max = 256, message = "协议最大长度不能超过256", groups = { ValidAdd.class, ValidEdit.class })
	private String protocol;

	@Schema(description = "适配器全限定类名")
	@Size(max = 500, message = "适配器全限定类名最大长度不能超过500", groups = { ValidAdd.class, ValidEdit.class })
	private String adapterClass;

	@Schema(description = "排序")
	private Integer sortIndex;

	@Schema(description = "备注")
	@Size(max = 256, message = "备注最大长度不能超过256", groups = { ValidAdd.class, ValidEdit.class })
	private String remark;
}