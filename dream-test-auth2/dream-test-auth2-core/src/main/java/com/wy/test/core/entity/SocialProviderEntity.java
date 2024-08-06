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
 * 第三方登录提供者
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
@TableName("auth_social_provider")
public class SocialProviderEntity extends AbstractStringEntity {

	private static final long serialVersionUID = 1L;

	private String provider;

	private String providerName;

	private String icon;

	private String clientId;

	private String clientSecret;

	private String agentId;

	private String display;

	private Integer sortIndex;

	private String scanCode;

	private String status;

	private String instId;

	public SocialProviderEntity(SocialProviderEntity copy) {
		this.clientId = copy.getClientId();
		this.id = copy.getId();
		this.provider = copy.getProvider();
		this.providerName = copy.getProviderName();
		this.agentId = copy.getAgentId();
		this.icon = copy.getIcon();
		this.scanCode = copy.getScanCode();
	}
}