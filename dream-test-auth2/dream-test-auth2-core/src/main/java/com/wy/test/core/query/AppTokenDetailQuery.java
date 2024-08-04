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
 * token详情查询
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
@Schema(description = "token详情查询")
public class AppTokenDetailQuery extends AbstractQuery {

	private static final long serialVersionUID = 1L;

	@Schema(description = "加密算法")
	private String algorithm;

	@Schema(description = "秘钥")
	private String algorithmKey;

	@Schema(description = "过期时间,单位秒")
	private Integer expires;

	@Schema(description = "重定向URI")
	private String redirectUri;

	@Schema(description = "cookie名称")
	private String cookieName;

	@Schema(description = "token类型")
	private String tokenType;

	@Schema(description = "机构id")
	private Long instId;
}