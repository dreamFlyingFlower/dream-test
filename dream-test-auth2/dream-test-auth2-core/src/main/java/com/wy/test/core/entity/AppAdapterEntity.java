package com.wy.test.core.entity;

import com.baomidou.mybatisplus.annotation.TableName;

import dream.flying.flower.framework.mybatis.plus.entity.AbstractEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

/**
 * 应用适配
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
@TableName("auth_app_adapter")
public class AppAdapterEntity extends AbstractEntity {

	private static final long serialVersionUID = -2267351538562702645L;

	/**
	 * 名称
	 */
	private String name;

	/**
	 * 协议
	 */
	private String protocol;

	/**
	 * 适配器全限定类名
	 */
	private String adapter;

	/**
	 * 排序
	 */
	private Integer sortIndex;

	/**
	 * 备注
	 */
	private String remark;
}