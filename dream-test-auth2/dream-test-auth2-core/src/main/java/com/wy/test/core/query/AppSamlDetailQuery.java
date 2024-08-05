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
 * SAML详情查询
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
@Schema(description = "SAML详情查询")
public class AppSamlDetailQuery extends AbstractQuery {

	private static final long serialVersionUID = 1L;

	@Schema(description = "证书发布者")
	private String certIssuer;

	@Schema(description = "证书主题")
	private String certSubject;

	@Schema(description = "过期时间")
	private String certExpiration;

	@Schema(description = "密钥存储")
	private Object keystore;

	@Schema(description = "地址")
	private String spAcsUrl;

	@Schema(description = "发布者")
	private String issuer;

	@Schema(description = "实体标识")
	private String entityId;

	@Schema(description = "有效期")
	private Integer validityInterval;

	@Schema(description = "名称id格式")
	private String nameIdFormat;

	@Schema(description = "名称id转换")
	private String nameIdConvert;

	@Schema(description = "名称id后缀")
	private String nameIdSuffix;

	@Schema(description = "授权者")
	private String audience;

	@Schema(description = "是否加密")
	private String encrypted;

	@Schema(description = "绑定方式")
	private String binding;

	@Schema(description = "签名")
	private String signature;

	@Schema(description = "加密类型")
	private String digestMethod;

	@Schema(description = "元数据URL")
	private String metaUrl;

	@Schema(description = "机构ID")
	private String instId;
}