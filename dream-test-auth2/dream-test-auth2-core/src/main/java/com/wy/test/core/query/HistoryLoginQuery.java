package com.wy.test.core.query;

import java.util.Date;

import dream.flying.flower.framework.web.query.AbstractQuery;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

/**
 * 登录历史查询
 *
 * @author 飞花梦影
 * @date 2024-08-01
 * @git {@link https://github.com/dreamFlyingFlower}
 */
@Getter
@Setter
@ToString
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "登录历史查询")
public class HistoryLoginQuery extends AbstractQuery {

	private static final long serialVersionUID = 1L;

	@Schema(description = "SESSIONID")
	private String sessionId;

	@Schema(description = "用户ID")
	private String userId;

	@Schema(description = "用户名")
	private String username;

	@Schema(description = "显示名")
	private String displayName;

	@Schema(description = "消息")
	private String message;

	@Schema(description = "登录IP")
	private String sourceIp;

	@Schema(description = "IP所属省")
	private String ipRegion;

	@Schema(description = "IP所属市")
	private String ipLocation;

	@Schema(description = "登录类型")
	private String loginType;

	@Schema(description = "验证码")
	private String code;

	@Schema(description = "提供者")
	private String provider;

	@Schema(description = "浏览器")
	private String browser;

	@Schema(description = "平台")
	private String platform;

	@Schema(description = "应用")
	private String application;

	@Schema(description = "登录地址")
	private String loginUrl;

	@Schema(description = "登录时间")
	private Date loginTime;

	@Schema(description = "登出时间")
	private Date logoutTime;

	@Schema(description = "SESSION状态")
	private Integer sessionStatus;

	@Schema(description = "机构ID")
	private String instId;
}