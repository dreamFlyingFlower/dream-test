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
 * 短信记录
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
@TableName("auth_sms_provider")
public class SmsProviderEntity extends AbstractStringEntity {

	private static final long serialVersionUID = 1L;

	/**
	 * 提供者
	 */
	private String provider;

	/**
	 * 消息
	 */
	private String message;

	/**
	 * APP KEY
	 */
	private String appKey;

	/**
	 * APP密钥
	 */
	private String appSecret;

	/**
	 * 模板ID
	 */
	private String templateId;

	/**
	 * 签名
	 */
	private String signName;

	/**
	 * 短信SDK ID
	 */
	private String smsSdkAppId;

	/**
	 * 机构ID
	 */
	private String instId;

	/**
	 * 备注
	 */
	private String remark;

	/**
	 * 状态
	 */
	private Integer status;
}