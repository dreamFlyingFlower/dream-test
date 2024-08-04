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
 * 邮件查询
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
@Schema(description = "邮件查询")
public class EmailSenderQuery extends AbstractQuery {

	private static final long serialVersionUID = 1L;

	@Schema(description = "域名")
	private String smtpHost;

	@Schema(description = "端口")
	private Integer port;

	@Schema(description = "帐号")
	private String account;

	@Schema(description = "密码")
	private String credentials;

	@Schema(description = "是否使用SSL")
	private Integer sslSwitch;

	@Schema(description = "发送者")
	private String sender;

	@Schema(description = "协议")
	private String protocol;

	@Schema(description = "编码")
	private String encoding;

	@Schema(description = "机构ID")
	private String instId;

	@Schema(description = "备注")
	private String remark;

	@Schema(description = "状态")
	private Integer status;
}