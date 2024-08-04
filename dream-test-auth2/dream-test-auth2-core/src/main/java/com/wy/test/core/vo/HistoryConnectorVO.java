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
 * 历史连接器
 *
 * @author 飞花梦影
 * @date 2024-08-01
 * @git {@link https://github.com/dreamFlyingFlower}
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "历史连接器")
public class HistoryConnectorVO implements Serializable, TransPojo {

	private static final long serialVersionUID = 1L;

	@Schema(description = "主键")
	@NotNull(message = "id不能为空", groups = { ValidEdit.class })
	private String id;

	@Schema(description = "连接名")
	@Size(max = 256, message = "连接名最大长度不能超过256", groups = { ValidAdd.class, ValidEdit.class })
	private String connName;

	@Schema(description = "资源ID")
	@Size(max = 32, message = "资源ID最大长度不能超过32", groups = { ValidAdd.class, ValidEdit.class })
	private String sourceId;

	@Schema(description = "资源名")
	@Size(max = 256, message = "资源名最大长度不能超过256", groups = { ValidAdd.class, ValidEdit.class })
	private String sourceName;

	@Schema(description = "对象ID")
	@Size(max = 32, message = "对象ID最大长度不能超过32", groups = { ValidAdd.class, ValidEdit.class })
	private String objectId;

	@Schema(description = "对象名")
	@Size(max = 256, message = "对象名最大长度不能超过256", groups = { ValidAdd.class, ValidEdit.class })
	private String objectName;

	@Schema(description = "同步时间")
	@Size(max = 32, message = "同步时间最大长度不能超过32", groups = { ValidAdd.class, ValidEdit.class })
	private String syncTime;

	@Schema(description = "同步结果")
	@Size(max = 32, message = "同步结果最大长度不能超过32", groups = { ValidAdd.class, ValidEdit.class })
	private String result;

	@Schema(description = "主题")
	@Size(max = 32, message = "主题最大长度不能超过32", groups = { ValidAdd.class, ValidEdit.class })
	private String topic;

	@Schema(description = "动作类型")
	@Size(max = 32, message = "动作类型最大长度不能超过32", groups = { ValidAdd.class, ValidEdit.class })
	private String actionType;

	@Schema(description = "机构ID")
	@NotNull(message = "机构ID不能为空", groups = { ValidAdd.class })
	private String instId;

	@Schema(description = "备注")
	@Size(max = 256, message = "备注最大长度不能超过256", groups = { ValidAdd.class, ValidEdit.class })
	private String remark;

	@Schema(description = "机构名称")
	private String instName;

	@Schema(description = "同步开始日期")
	private String startDate;

	@Schema(description = "同步结束日期")
	private String endDate;
}