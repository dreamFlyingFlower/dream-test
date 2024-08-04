package com.wy.test.core.vo;

import java.io.Serializable;
import java.util.Date;

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
 * 历史事件
 *
 * @author 飞花梦影
 * @date 2024-08-01
 * @git {@link https://github.com/dreamFlyingFlower}
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "历史事件")
public class HistoryEventVO implements Serializable, TransPojo {

	private static final long serialVersionUID = 1L;

	@Schema(description = "主键")
	@NotNull(message = "id不能为空", groups = { ValidEdit.class })
	private Long id;

	@Schema(description = "事件名")
	@Size(max = 32, message = "事件名最大长度不能超过32", groups = { ValidAdd.class, ValidEdit.class })
	private String eventName;

	@Schema(description = "数据类型")
	@Size(max = 32, message = "数据类型最大长度不能超过32", groups = { ValidAdd.class, ValidEdit.class })
	private String dataType;

	@Schema(description = "数据数量")
	private Integer dataCount;

	@Schema(description = "执行时间")
	@JsonFormat(pattern = ConstDate.DATETIME)
	private Date executeTime;

	@Schema(description = "机构ID")
	@NotNull(message = "机构ID不能为空", groups = { ValidAdd.class })
	private Long instId;
}