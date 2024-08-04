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
 * JWT详情查询
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
@Schema(description = "JWT详情查询")
public class AppJwtDetailQuery extends AbstractQuery {

	private static final long serialVersionUID = 1L;

	@Schema(description = "发布者")
	private String issuer;

	@Schema(description = "主题")
	private String subject;

	@Schema(description = "授权者")
	private String audience;

	@Schema(description = "秘钥")
	private String algorithmKey;

	@Schema(description = "加密算法")
	private String algorithm;

	@Schema(description = "加密类型")
	private String encryptionType;

	@Schema(description = "签名算法")
	private String signature;

	@Schema(description = "签名密钥")
	private String signatureKey;

	@Schema(description = "过期时间,单位秒")
	private Integer expires;

	@Schema(description = "重定向URI")
	private String redirectUri;

	@Schema(description = "Jwt名称")
	private String jwtName;

	@Schema(description = "Token类型")
	private String tokenType;

	@Schema(description = "机构ID")
	private String instId;
}