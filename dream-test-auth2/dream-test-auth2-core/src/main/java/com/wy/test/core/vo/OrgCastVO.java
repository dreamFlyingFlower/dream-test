package com.wy.test.core.vo;

import java.io.Serializable;

import javax.validation.constraints.NotBlank;
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
 * 机构映射表
 *
 * @author 飞花梦影
 * @date 2024-08-01
 * @git {@link https://github.com/dreamFlyingFlower}
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "机构映射表")
public class OrgCastVO implements Serializable, TransPojo {

	private static final long serialVersionUID = 1L;

	@Schema(description = "ID")
	@NotNull(message = "id不能为空", groups = { ValidEdit.class })
	private String id;

	@Schema(description = "机构编码")
	@Size(max = 32, message = "机构编码最大长度不能超过32", groups = { ValidAdd.class, ValidEdit.class })
	private String code;

	@Schema(description = "机构名称")
	@NotBlank(message = "机构名称不能为空", groups = { ValidAdd.class })
	@Size(max = 64, message = "机构名称最大长度不能超过64", groups = { ValidAdd.class, ValidEdit.class })
	private String name;

	@Schema(description = "机构全名")
	@Size(max = 128, message = "机构全名最大长度不能超过128", groups = { ValidAdd.class, ValidEdit.class })
	private String fullName;

	@Schema(description = "父级ID")
	private String parentId;

	@Schema(description = "父级名称")
	@Size(max = 64, message = "父级名称最大长度不能超过64", groups = { ValidAdd.class, ValidEdit.class })
	private String parentName;

	@Schema(description = "CODE路径")
	@Size(max = 256, message = "CODE路径最大长度不能超过256", groups = { ValidAdd.class, ValidEdit.class })
	private String codePath;

	@Schema(description = "名称路径")
	@Size(max = 512, message = "名称路径最大长度不能超过512", groups = { ValidAdd.class, ValidEdit.class })
	private String namePath;

	@Schema(description = "机构提供者")
	@Size(max = 45, message = "机构提供者最大长度不能超过45", groups = { ValidAdd.class, ValidEdit.class })
	private String provider;

	@Schema(description = "机构ID")
	private String orgId;

	@Schema(description = "机构PARENTID")
	private String orgParentId;

	@Schema(description = "机构ID")
	@NotNull(message = "机构ID不能为空", groups = { ValidAdd.class })
	private String instId;

	@Schema(description = "排序")
	private Integer sortIndex;

	@Schema(description = "状态")
	private Integer status;

	@Schema(description = "应用ID")
	private String appId;

	@Schema(description = "应用名称")
	private String appName;

	@Schema(description = "机构名称")
	private String instName;

	@Schema(description = "重组标志")
	boolean reorgNamePath;
}