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
 * token详情
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
@Schema(description = "token详情")
public class AppTokenDetailVO extends AppVO implements Serializable, TransPojo {

	private static final long serialVersionUID = 1L;

	@Schema(description = "主键")
	@NotNull(message = "id不能为空", groups = { ValidEdit.class })
	private String id;

	@Schema(description = "加密算法")
	@NotBlank(message = "加密算法不能为空", groups = { ValidAdd.class })
	@Size(max = 32, message = "加密算法最大长度不能超过32", groups = { ValidAdd.class, ValidEdit.class })
	private String algorithm;

	@Schema(description = "秘钥")
	@NotBlank(message = "秘钥不能为空", groups = { ValidAdd.class })
	@Size(max = 512, message = "秘钥最大长度不能超过512", groups = { ValidAdd.class, ValidEdit.class })
	private String algorithmKey;

	@Schema(description = "过期时间,单位秒")
	private Integer expires;

	@Schema(description = "重定向URI")
	@NotBlank(message = "重定向URI不能为空", groups = { ValidAdd.class })
	@Size(max = 256, message = "重定向URI最大长度不能超过256", groups = { ValidAdd.class, ValidEdit.class })
	private String redirectUri;

	@Schema(description = "cookie名称")
	@Size(max = 32, message = "cookie名称最大长度不能超过32", groups = { ValidAdd.class, ValidEdit.class })
	private String cookieName;

	@Schema(description = "token类型")
	@Size(max = 20, message = "token类型最大长度不能超过20", groups = { ValidAdd.class, ValidEdit.class })
	private String tokenType;

	@Schema(description = "机构id")
	@NotNull(message = "机构id不能为空", groups = { ValidAdd.class })
	private String instId;

	@Schema(description = "机构名称")
	private String instName;
}