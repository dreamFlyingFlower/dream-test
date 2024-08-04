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
 * 系统操作日志
 *
 * @author 飞花梦影
 * @date 2024-08-01
 * @git {@link https://github.com/dreamFlyingFlower}
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "系统操作日志")
public class HistorySysLogVO implements Serializable, TransPojo {

	private static final long serialVersionUID = 1L;

	@Schema(description = "主键")
	@NotNull(message = "id不能为空", groups = { ValidEdit.class })
	private String id;

	@Schema(description = "主题")
	@Size(max = 32, message = "主题最大长度不能超过32", groups = { ValidAdd.class, ValidEdit.class })
	private String topic;

	@Schema(description = "消息")
	@Size(max = 256, message = "消息最大长度不能超过256", groups = { ValidAdd.class, ValidEdit.class })
	private String message;

	@Schema(description = "消息类型")
	@Size(max = 32, message = "消息类型最大长度不能超过32", groups = { ValidAdd.class, ValidEdit.class })
	private String messageAction;

	@Schema(description = "消息结果")
	@Size(max = 32, message = "消息结果最大长度不能超过32", groups = { ValidAdd.class, ValidEdit.class })
	private String messageResult;

	@Schema(description = "用户ID")
	private String userId;

	@Schema(description = "用户名")
	@Size(max = 32, message = "用户名最大长度不能超过32", groups = { ValidAdd.class, ValidEdit.class })
	private String username;

	@Schema(description = "显示名")
	@Size(max = 32, message = "显示名最大长度不能超过32", groups = { ValidAdd.class, ValidEdit.class })
	private String displayName;

	@Schema(description = "执行时间")
	@JsonFormat(pattern = ConstDate.DATETIME)
	@NotNull(message = "执行时间不能为空", groups = { ValidAdd.class })
	private Date executeTime;

	@Schema(description = "JSON信息")
	private String jsonCotent;

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