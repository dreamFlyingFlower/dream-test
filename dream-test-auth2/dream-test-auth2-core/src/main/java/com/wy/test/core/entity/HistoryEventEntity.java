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
 * 历史事件
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
@TableName("auth_history_event")
public class HistoryEventEntity extends AbstractStringEntity {

	private static final long serialVersionUID = 1L;

	/**
	 * 事件名
	 */
	private String eventName;

	/**
	 * 数据类型
	 */
	private String dataType;

	/**
	 * 数据数量
	 */
	private Integer dataCount;

	/**
	 * 执行时间
	 */
	private Date executeTime;

	/**
	 * 机构ID
	 */
	private Long instId;
}