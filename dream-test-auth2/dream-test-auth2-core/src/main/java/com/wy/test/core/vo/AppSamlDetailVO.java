package com.wy.test.core.vo;

import java.io.Serializable;
import java.security.cert.X509Certificate;

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
 * SAML详情
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
@Schema(description = "SAML详情")
public class AppSamlDetailVO extends AppVO implements Serializable, TransPojo {

	private static final long serialVersionUID = 1L;

	@Schema(description = "主键")
	@NotNull(message = "id不能为空", groups = { ValidEdit.class })
	private String id;

	@Schema(description = "证书发布者")
	@Size(max = 256, message = "证书发布者最大长度不能超过256", groups = { ValidAdd.class, ValidEdit.class })
	private String certIssuer;

	@Schema(description = "证书主题")
	@Size(max = 256, message = "证书主题最大长度不能超过256", groups = { ValidAdd.class, ValidEdit.class })
	private String certSubject;

	@Schema(description = "过期时间")
	@Size(max = 32, message = "过期时间最大长度不能超过32", groups = { ValidAdd.class, ValidEdit.class })
	private String certExpiration;

	@Schema(description = "密钥存储")
	@Size(max = 65535, message = "密钥存储最大长度不能超过65,535", groups = { ValidAdd.class, ValidEdit.class })
	private Object keystore;

	@Schema(description = "地址")
	@NotBlank(message = "地址不能为空", groups = { ValidAdd.class })
	@Size(max = 256, message = "地址最大长度不能超过256", groups = { ValidAdd.class, ValidEdit.class })
	private String spacsUrl;

	@Schema(description = "发布者")
	@Size(max = 256, message = "发布者最大长度不能超过256", groups = { ValidAdd.class, ValidEdit.class })
	private String issuer;

	@Schema(description = "实体标识")
	@Size(max = 256, message = "实体标识最大长度不能超过256", groups = { ValidAdd.class, ValidEdit.class })
	private String entityId;

	@Schema(description = "有效期")
	private Integer validityInterval;

	@Schema(description = "名称id格式")
	@Size(max = 32, message = "名称id格式最大长度不能超过32", groups = { ValidAdd.class, ValidEdit.class })
	private String nameIdFormat;

	@Schema(description = "名称id转换")
	@Size(max = 32, message = "名称id转换最大长度不能超过32", groups = { ValidAdd.class, ValidEdit.class })
	private String nameIdConvert;

	@Schema(description = "名称id后缀")
	@Size(max = 32, message = "名称id后缀最大长度不能超过32", groups = { ValidAdd.class, ValidEdit.class })
	private String nameIdSuffix;

	@Schema(description = "授权者")
	@Size(max = 256, message = "授权者最大长度不能超过256", groups = { ValidAdd.class, ValidEdit.class })
	private String audience;

	@Schema(description = "是否加密")
	@Size(max = 16, message = "是否加密最大长度不能超过16", groups = { ValidAdd.class, ValidEdit.class })
	private String encrypted;

	@Schema(description = "绑定方式")
	@Size(max = 32, message = "绑定方式最大长度不能超过32", groups = { ValidAdd.class, ValidEdit.class })
	private String binding;

	@Schema(description = "签名")
	@Size(max = 32, message = "签名最大长度不能超过32", groups = { ValidAdd.class, ValidEdit.class })
	private String signature;

	@Schema(description = "加密类型")
	@Size(max = 32, message = "加密类型最大长度不能超过32", groups = { ValidAdd.class, ValidEdit.class })
	private String digestMethod;

	@Schema(description = "元数据URL")
	@Size(max = 256, message = "元数据URL最大长度不能超过256", groups = { ValidAdd.class, ValidEdit.class })
	private String metaUrl;

	@Schema(description = "机构ID")
	@NotNull(message = "机构ID不能为空", groups = { ValidAdd.class })
	private String instId;

	/**
	 * metadata_file metadata_url or certificate
	 */
	private String fileType;

	private String metaFileId;

	private X509Certificate trustCert;

	private String instName;
}