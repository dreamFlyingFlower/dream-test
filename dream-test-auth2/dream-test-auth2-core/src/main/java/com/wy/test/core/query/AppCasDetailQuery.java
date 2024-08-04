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
 * CAS详情查询
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
@Schema(description = "CAS详情查询")
public class AppCasDetailQuery extends AbstractQuery {

	private static final long serialVersionUID = 1L;

	@Schema(description = "服务地址")
	private String service;

	@Schema(description = "回调地址")
	private String callbackUrl;

	@Schema(description = "过期时间,单位秒")
	private Integer expires;

	@Schema(description = "机构ID")
	private String instId;

	@Schema(description = "单点登录用户")
	private String casUser;
}