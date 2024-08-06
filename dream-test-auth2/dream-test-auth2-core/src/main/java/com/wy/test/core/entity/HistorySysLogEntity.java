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
 * 系统操作日志
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
@TableName("auth_history_sys_log")
public class HistorySysLogEntity extends AbstractStringEntity {

	private static final long serialVersionUID = 6560201093784960493L;

	private String topic;

	private String message;

	private String messageAction;

	private String messageResult;

	private String userId;

	private String username;

	private String displayName;

	private Date executeTime;

	private String instId;

	private String jsonCotent;
}