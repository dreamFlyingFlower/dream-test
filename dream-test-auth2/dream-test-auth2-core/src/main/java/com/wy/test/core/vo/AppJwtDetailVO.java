package com.wy.test.core.vo;

import java.io.Serializable;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fhs.core.trans.vo.TransPojo;

import dream.flying.flower.framework.web.valid.ValidAdd;
import dream.flying.flower.framework.web.valid.ValidEdit;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

/**
 * JWT详情
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
@Schema(description = "JWT详情")
public class AppJwtDetailVO extends AppVO implements Serializable, TransPojo {

	private static final long serialVersionUID = 1L;

	@Schema(description = "主键")
	@NotNull(message = "id不能为空", groups = { ValidEdit.class })
	private String id;

	@Schema(description = "发布者")
	@Size(max = 256, message = "发布者最大长度不能超过256", groups = { ValidAdd.class, ValidEdit.class })
	private String issuer;

	@Schema(description = "主题")
	@Size(max = 64, message = "主题最大长度不能超过64", groups = { ValidAdd.class, ValidEdit.class })
	private String subject;

	@Schema(description = "授权者")
	@Size(max = 256, message = "授权者最大长度不能超过256", groups = { ValidAdd.class, ValidEdit.class })
	private String audience;

	@Schema(description = "秘钥")
	@NotBlank(message = "秘钥不能为空", groups = { ValidAdd.class })
	@Size(max = 65535, message = "秘钥最大长度不能超过65,535", groups = { ValidAdd.class, ValidEdit.class })
	private String algorithmKey;

	@Schema(description = "加密算法")
	@NotBlank(message = "加密算法不能为空", groups = { ValidAdd.class })
	@Size(max = 32, message = "加密算法最大长度不能超过32", groups = { ValidAdd.class, ValidEdit.class })
	private String algorithm;

	@Schema(description = "加密类型")
	@Size(max = 32, message = "加密类型最大长度不能超过32", groups = { ValidAdd.class, ValidEdit.class })
	private String encryptionType;

	@Schema(description = "签名算法")
	@Size(max = 32, message = "签名算法最大长度不能超过32", groups = { ValidAdd.class, ValidEdit.class })
	private String signature;

	@Schema(description = "签名密钥")
	@Size(max = 65535, message = "签名密钥最大长度不能超过65,535", groups = { ValidAdd.class, ValidEdit.class })
	private String signatureKey;

	@Schema(description = "过期时间,单位秒")
	private Integer expires;

	@Schema(description = "重定向URI")
	@NotBlank(message = "重定向URI不能为空", groups = { ValidAdd.class })
	@Size(max = 256, message = "重定向URI最大长度不能超过256", groups = { ValidAdd.class, ValidEdit.class })
	private String redirectUri;

	@Schema(description = "Jwt名称")
	@Size(max = 32, message = "Jwt名称最大长度不能超过32", groups = { ValidAdd.class, ValidEdit.class })
	private String jwtName;

	@Schema(description = "Token类型")
	@Size(max = 20, message = "Token类型最大长度不能超过20", groups = { ValidAdd.class, ValidEdit.class })
	private String tokenType;

	@Schema(description = "机构ID")
	@NotNull(message = "机构ID不能为空", groups = { ValidAdd.class })
	private String instId;

	@Schema(description = "机构名称")
	private String instName;
}