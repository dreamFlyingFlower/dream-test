package com.wy.test.core.entity;

import com.baomidou.mybatisplus.annotation.TableName;

import dream.flying.flower.framework.mybatis.plus.entity.AbstractStringEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

/**
 * 登录历史
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
@TableName("auth_history_login")
public class HistoryLoginEntity extends AbstractStringEntity {

	private static final long serialVersionUID = -1321470643357719383L;

	private String sessionId;

	private Integer sessionStatus;

	private String userId;

	private String username;

	private String displayName;

	private String loginType;

	private String message;

	private String code;

	private String provider;

	private String sourceIp;

	private String ipRegion;

	private String ipLocation;

	private String browser;

	private String platform;

	private String application;

	private String loginUrl;

	private String loginTime;

	private String logoutTime;

	private String instId;
}