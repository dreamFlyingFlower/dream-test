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
 * 同步关联
 *
 * @author 飞花梦影
 * @date 2024-08-01
 * @git {@link https://github.com/dreamFlyingFlower}
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "同步关联")
public class SyncRelatedVO implements Serializable, TransPojo {

	private static final long serialVersionUID = 1L;

	@Schema(description = "主键")
	@NotNull(message = "id不能为空", groups = { ValidEdit.class })
	private String id;

	@Schema(description = "对象ID")
	@Size(max = 32, message = "对象ID最大长度不能超过32", groups = { ValidAdd.class, ValidEdit.class })
	private String objectId;

	@Schema(description = "对象名称")
	@Size(max = 32, message = "对象名称最大长度不能超过32", groups = { ValidAdd.class, ValidEdit.class })
	private String objectName;

	@Schema(description = "对象展示名称")
	@Size(max = 32, message = "对象展示名称最大长度不能超过32", groups = { ValidAdd.class, ValidEdit.class })
	private String objectDisplayName;

	@Schema(description = "对象类型")
	@Size(max = 32, message = "对象类型最大长度不能超过32", groups = { ValidAdd.class, ValidEdit.class })
	private String objectType;

	@Schema(description = "同步ID")
	private String syncId;

	@Schema(description = "同步名称")
	@Size(max = 200, message = "同步名称最大长度不能超过200", groups = { ValidAdd.class, ValidEdit.class })
	private String syncName;

	@Schema(description = "原始ID")
	@Size(max = 1000, message = "原始ID最大长度不能超过1,000", groups = { ValidAdd.class, ValidEdit.class })
	private String originId;

	@Schema(description = "原始ID2")
	@Size(max = 200, message = "原始ID2最大长度不能超过200", groups = { ValidAdd.class, ValidEdit.class })
	private String originId2;

	@Schema(description = "原始ID3")
	@Size(max = 200, message = "原始ID3最大长度不能超过200", groups = { ValidAdd.class, ValidEdit.class })
	private String originId3;

	@Schema(description = "同步时间")
	@Size(max = 32, message = "同步时间最大长度不能超过32", groups = { ValidAdd.class, ValidEdit.class })
	private String syncTime;

	@Schema(description = "原始名称")
	@Size(max = 500, message = "原始名称最大长度不能超过500", groups = { ValidAdd.class, ValidEdit.class })
	private String originName;

	@Schema(description = "机构ID")
	private String instId;

	@Schema(description = "机构名称")
	private String instName;
}