package com.wy.test.core.entity;

import com.baomidou.mybatisplus.annotation.TableName;

import dream.flying.flower.framework.mybatis.plus.entity.AbstractStringEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

/**
 * 扩展应用表
 *
 * @author 飞花梦影
 * @date 2024-08-01
 * @git {@link https://github.com/dreamFlyingFlower}
 */
@Getter
@Setter
@ToString
@SuperBuilder
@AllArgsConstructor
@TableName("auth_app_extend_detail")
public class AppExtendDetailEntity extends AbstractStringEntity {

	private static final long serialVersionUID = 2839361625399070453L;

}