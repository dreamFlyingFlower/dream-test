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
 * 短信记录
 *
 * @author 飞花梦影
 * @date 2024-08-01
 * @git {@link https://github.com/dreamFlyingFlower}
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "短信记录")
public class SmsProviderVO implements Serializable, TransPojo {

	private static final long serialVersionUID = 1L;

	@Schema(description = "主键")
	@NotNull(message = "id不能为空", groups = { ValidEdit.class })
	private String id;

	@Schema(description = "提供者")
	@Size(max = 100, message = "提供者最大长度不能超过100", groups = { ValidAdd.class, ValidEdit.class })
	private String provider;

	@Schema(description = "消息")
	@Size(max = 256, message = "消息最大长度不能超过256", groups = { ValidAdd.class, ValidEdit.class })
	private String message;

	@Schema(description = "APP KEY")
	@Size(max = 128, message = "APP KEY最大长度不能超过128", groups = { ValidAdd.class, ValidEdit.class })
	private String appKey;

	@Schema(description = "APP密钥")
	@Size(max = 256, message = "APP密钥最大长度不能超过256", groups = { ValidAdd.class, ValidEdit.class })
	private String appSecret;

	@Schema(description = "模板ID")
	@Size(max = 32, message = "模板ID最大长度不能超过32", groups = { ValidAdd.class, ValidEdit.class })
	private String templateId;

	@Schema(description = "签名")
	@Size(max = 32, message = "签名最大长度不能超过32", groups = { ValidAdd.class, ValidEdit.class })
	private String signName;

	@Schema(description = "短信SDK ID")
	@Size(max = 32, message = "短信SDK ID最大长度不能超过32", groups = { ValidAdd.class, ValidEdit.class })
	private String smsSdkAppId;

	@Schema(description = "机构ID")
	private String instId;

	@Schema(description = "备注")
	@Size(max = 256, message = "备注最大长度不能超过256", groups = { ValidAdd.class, ValidEdit.class })
	private String remark;

	@Schema(description = "状态")
	private Integer status;

	@Schema(description = "机构名称")
	private String instName;

	@Schema(description = "提供者名称")
	private String providerName;
}