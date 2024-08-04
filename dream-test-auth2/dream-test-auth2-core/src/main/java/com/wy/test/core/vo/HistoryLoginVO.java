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
 * 登录历史
 *
 * @author 飞花梦影
 * @date 2024-08-01
 * @git {@link https://github.com/dreamFlyingFlower}
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "登录历史")
public class HistoryLoginVO implements Serializable, TransPojo {

	private static final long serialVersionUID = 1L;

	@Schema(description = "ID")
	@NotNull(message = "id不能为空", groups = { ValidEdit.class })
	private String id;

	@Schema(description = "SESSIONID")
	@Size(max = 32, message = "SESSIONID最大长度不能超过32", groups = { ValidAdd.class, ValidEdit.class })
	private String sessionId;

	@Schema(description = "SESSION状态")
	private Integer sessionStatus;

	@Schema(description = "用户ID")
	@NotBlank(message = "用户ID不能为空", groups = { ValidAdd.class })
	@Size(max = 32, message = "用户ID最大长度不能超过32", groups = { ValidAdd.class, ValidEdit.class })
	private String userId;

	@Schema(description = "用户名")
	@NotBlank(message = "用户名不能为空", groups = { ValidAdd.class })
	@Size(max = 64, message = "用户名最大长度不能超过64", groups = { ValidAdd.class, ValidEdit.class })
	private String username;

	@Schema(description = "显示名")
	@Size(max = 32, message = "显示名最大长度不能超过32", groups = { ValidAdd.class, ValidEdit.class })
	private String displayName;

	@Schema(description = "消息")
	@Size(max = 256, message = "消息最大长度不能超过256", groups = { ValidAdd.class, ValidEdit.class })
	private String message;

	@Schema(description = "登录IP")
	@Size(max = 32, message = "登录IP最大长度不能超过32", groups = { ValidAdd.class, ValidEdit.class })
	private String sourceIp;

	@Schema(description = "IP所属省")
	@Size(max = 256, message = "IP所属省最大长度不能超过256", groups = { ValidAdd.class, ValidEdit.class })
	private String ipRegion;

	@Schema(description = "IP所属市")
	@Size(max = 256, message = "IP所属市最大长度不能超过256", groups = { ValidAdd.class, ValidEdit.class })
	private String ipLocation;

	@Schema(description = "登录类型")
	@Size(max = 32, message = "登录类型最大长度不能超过32", groups = { ValidAdd.class, ValidEdit.class })
	private String loginType;

	@Schema(description = "验证码")
	@Size(max = 32, message = "验证码最大长度不能超过32", groups = { ValidAdd.class, ValidEdit.class })
	private String code;

	@Schema(description = "提供者")
	@Size(max = 32, message = "提供者最大长度不能超过32", groups = { ValidAdd.class, ValidEdit.class })
	private String provider;

	@Schema(description = "浏览器")
	@Size(max = 32, message = "浏览器最大长度不能超过32", groups = { ValidAdd.class, ValidEdit.class })
	private String browser;

	@Schema(description = "平台")
	@Size(max = 32, message = "平台最大长度不能超过32", groups = { ValidAdd.class, ValidEdit.class })
	private String platform;

	@Schema(description = "应用")
	@Size(max = 32, message = "应用最大长度不能超过32", groups = { ValidAdd.class, ValidEdit.class })
	private String application;

	@Schema(description = "登录地址")
	@Size(max = 256, message = "登录地址最大长度不能超过256", groups = { ValidAdd.class, ValidEdit.class })
	private String loginUrl;

	@Schema(description = "登录时间")
	@JsonFormat(pattern = ConstDate.DATETIME)
	@NotNull(message = "登录时间不能为空", groups = { ValidAdd.class })
	private Date loginTime;

	@Schema(description = "登出时间")
	@NotBlank(message = "登出时间不能为空", groups = { ValidAdd.class })
	@Size(max = 50, message = "登出时间最大长度不能超过50", groups = { ValidAdd.class, ValidEdit.class })
	private Date logoutTime;

	@Schema(description = "机构ID")
	private String instId;

	@Schema(description = "机构名称")
	private String instName;

	@Schema(description = "同步开始日期")
	private String startDate;

	@Schema(description = "同步结束日期")
	private String endDate;
}