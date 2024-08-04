package com.wy.test.core.vo;

import java.io.Serializable;
import java.util.Date;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fhs.core.trans.vo.TransPojo;

import dream.flying.flower.ConstDate;
import dream.flying.flower.framework.web.valid.ValidAdd;
import dream.flying.flower.framework.web.valid.ValidEdit;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 同步日志
 *
 * @author 飞花梦影
 * @date 2024-08-01
 * @git {@link https://github.com/dreamFlyingFlower}
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "同步日志")
public class HistorySyncVO implements Serializable, TransPojo {

	private static final long serialVersionUID = 1L;

	@Schema(description = "主键")
	@NotNull(message = "id不能为空", groups = { ValidEdit.class })
	private String id;

	@Schema(description = "SESSION ID")
	private String sessionId;

	@Schema(description = "同步ID")
	@NotBlank(message = "同步ID不能为空", groups = { ValidAdd.class })
	@Size(max = 32, message = "同步ID最大长度不能超过32", groups = { ValidAdd.class, ValidEdit.class })
	private String syncId;

	@Schema(description = "同步名")
	@Size(max = 32, message = "同步名最大长度不能超过32", groups = { ValidAdd.class, ValidEdit.class })
	private String syncName;

	@Schema(description = "对象ID")
	@Size(max = 32, message = "对象ID最大长度不能超过32", groups = { ValidAdd.class, ValidEdit.class })
	private String objectId;

	@Schema(description = "对象名")
	@Size(max = 32, message = "对象名最大长度不能超过32", groups = { ValidAdd.class, ValidEdit.class })
	private String objectName;

	@Schema(description = "对象类型")
	@Size(max = 32, message = "对象类型最大长度不能超过32", groups = { ValidAdd.class, ValidEdit.class })
	private String objectType;

	@Schema(description = "同步时间")
	@JsonFormat(pattern = ConstDate.DATETIME)
	@NotNull(message = "同步时间不能为空", groups = { ValidAdd.class })
	private Date syncTime;

	@Schema(description = "结果")
	@Size(max = 32, message = "结果最大长度不能超过32", groups = { ValidAdd.class, ValidEdit.class })
	private String result;

	@Schema(description = "机构ID")
	@NotNull(message = "机构ID不能为空", groups = { ValidAdd.class })
	private String instId;

	@Schema(description = "机构名称")
	private String instName;

	@Schema(description = "同步开始日期")
	private String startDate;

	@Schema(description = "同步结束日期")
	private String endDate;
}