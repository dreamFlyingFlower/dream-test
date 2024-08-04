package com.wy.test.core.query;

import dream.flying.flower.framework.web.query.AbstractQuery;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

/**
 * 第三方登录提供者查询
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
@Schema(description = "第三方登录提供者查询")
public class SocialProviderQuery extends AbstractQuery {

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
}