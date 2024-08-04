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
 * 短信记录查询
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
@Schema(description = "短信记录查询")
public class SmsProviderQuery extends AbstractQuery {

	private static final long serialVersionUID = 1L;

	@Schema(description = "提供者")
	private String provider;

	@Schema(description = "消息")
	private String message;

	@Schema(description = "APP KEY")
	private String appKey;

	@Schema(description = "APP密钥")
	private String appSecret;

	@Schema(description = "模板ID")
	private String templateId;

	@Schema(description = "签名")
	private String signName;

	@Schema(description = "短信SDK ID")
	private String smsSdkAppId;

	@Schema(description = "机构ID")
	private String instId;

	@Schema(description = "备注")
	private String remark;

	@Schema(description = "状态")
	private Integer status;
}