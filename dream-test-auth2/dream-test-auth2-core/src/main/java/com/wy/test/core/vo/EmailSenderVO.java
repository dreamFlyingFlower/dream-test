package com.wy.test.core.vo;

import java.io.Serializable;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fhs.core.trans.vo.TransPojo;

import dream.flying.flower.framework.web.valid.ValidAdd;
import dream.flying.flower.framework.web.valid.ValidEdit;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 邮件
 *
 * @author 飞花梦影
 * @date 2024-08-01
 * @git {@link https://github.com/dreamFlyingFlower}
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "邮件")
public class EmailSenderVO implements Serializable, TransPojo {

	private static final long serialVersionUID = 1L;

	@Schema(description = "主键")
	@NotNull(message = "id不能为空", groups = { ValidEdit.class })
	private String id;

	@Schema(description = "域名")
	@Size(max = 32, message = "域名最大长度不能超过32", groups = { ValidAdd.class, ValidEdit.class })
	private String smtpHost;

	@Schema(description = "端口")
	private Integer port;

	@Schema(description = "帐号")
	@Size(max = 32, message = "帐号最大长度不能超过32", groups = { ValidAdd.class, ValidEdit.class })
	private String account;

	@Schema(description = "密码")
	@Size(max = 512, message = "密码最大长度不能超过512", groups = { ValidAdd.class, ValidEdit.class })
	private String credentials;

	@Schema(description = "是否使用SSL")
	private Integer sslSwitch;

	@Schema(description = "发送者")
	@Size(max = 32, message = "发送者最大长度不能超过32", groups = { ValidAdd.class, ValidEdit.class })
	private String sender;

	@Schema(description = "协议")
	@Size(max = 32, message = "协议最大长度不能超过32", groups = { ValidAdd.class, ValidEdit.class })
	private String protocol;

	@Schema(description = "编码")
	@Size(max = 32, message = "编码最大长度不能超过32", groups = { ValidAdd.class, ValidEdit.class })
	private String encoding;

	@Schema(description = "机构ID")
	private String instId;

	@Schema(description = "机构名称")
	private String instName;

	@Schema(description = "备注")
	@Size(max = 256, message = "备注最大长度不能超过256", groups = { ValidAdd.class, ValidEdit.class })
	private String remark;

	@Schema(description = "状态")
	private Integer status;
}