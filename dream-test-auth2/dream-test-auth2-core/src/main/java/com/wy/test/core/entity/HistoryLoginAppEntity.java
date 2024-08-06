package com.wy.test.core.entity;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.TableName;

import dream.flying.flower.framework.mybatis.plus.entity.AbstractStringEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

/**
 * APP登录历史记录
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
@TableName("auth_history_login_app")
public class HistoryLoginAppEntity extends AbstractStringEntity {

	private static final long serialVersionUID = 5085201575292304749L;

	private String id;

	private String sessionId;

	private String appId;

	private String appName;

	private String userId;

	private String username;

	private String displayName;

	private Date loginTime;

	private String instId;

	public HistoryLoginAppEntity(String sessionId, String appId) {
		super();
		this.sessionId = sessionId;
		this.appId = appId;
	}
}