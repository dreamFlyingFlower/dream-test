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
 * APP登录历史记录
 *
 * @author 飞花梦影
 * @date 2024-08-01
 * @git {@link https://github.com/dreamFlyingFlower}
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "APP登录历史记录")
public class HistoryLoginAppVO implements Serializable, TransPojo {

	private static final long serialVersionUID = 1L;

	@Schema(description = "ID")
	@NotNull(message = "id不能为空", groups = { ValidEdit.class })
	private String id;

	@Schema(description = "SESSIONID")
	@Size(max = 32, message = "SESSIONID最大长度不能超过32", groups = { ValidAdd.class, ValidEdit.class })
	private String sessionId;

	@Schema(description = "登录时间")
	@JsonFormat(pattern = ConstDate.DATETIME)
	@NotNull(message = "登录时间不能为空", groups = { ValidAdd.class })
	private Date loginTime;

	@Schema(description = "APPID")
	@NotBlank(message = "APPID不能为空", groups = { ValidAdd.class })
	@Size(max = 64, message = "APPID最大长度不能超过64", groups = { ValidAdd.class, ValidEdit.class })
	private String appId;

	@Schema(description = "APP名称")
	@Size(max = 32, message = "APP名称最大长度不能超过32", groups = { ValidAdd.class, ValidEdit.class })
	private String appName;

	@Schema(description = "用户ID")
	@Size(max = 32, message = "用户ID最大长度不能超过32", groups = { ValidAdd.class, ValidEdit.class })
	private String userId;

	@Schema(description = "用户名")
	@Size(max = 32, message = "用户名最大长度不能超过32", groups = { ValidAdd.class, ValidEdit.class })
	private String username;

	@Schema(description = "显示名")
	@Size(max = 32, message = "显示名最大长度不能超过32", groups = { ValidAdd.class, ValidEdit.class })
	private String displayName;

	@NotNull(message = "instId不能为空", groups = { ValidAdd.class })
	private String instId;

	@Schema(description = "机构名称")
	private String instName;

	@Schema(description = "同步开始日期")
	private String startDate;

	@Schema(description = "同步结束日期")
	private String endDate;
}