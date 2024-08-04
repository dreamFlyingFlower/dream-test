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
 * CAS详情
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
@Schema(description = "CAS详情")
public class AppCasDetailVO extends AppVO implements Serializable, TransPojo {

	private static final long serialVersionUID = 1L;

	@Schema(description = "主键")
	@NotNull(message = "id不能为空", groups = { ValidEdit.class })
	private String id;

	@Schema(description = "服务地址")
	@NotBlank(message = "服务地址不能为空", groups = { ValidAdd.class })
	@Size(max = 256, message = "服务地址最大长度不能超过256", groups = { ValidAdd.class, ValidEdit.class })
	private String service;

	@Schema(description = "回调地址")
	@NotBlank(message = "回调地址不能为空", groups = { ValidAdd.class })
	@Size(max = 256, message = "回调地址最大长度不能超过256", groups = { ValidAdd.class, ValidEdit.class })
	private String callbackUrl;

	@Schema(description = "过期时间,单位秒")
	private Integer expires;

	@Schema(description = "机构ID")
	@NotNull(message = "机构ID不能为空", groups = { ValidAdd.class })
	private String instId;

	@Schema(description = "单点登录用户")
	@Size(max = 45, message = "单点登录用户最大长度不能超过45", groups = { ValidAdd.class, ValidEdit.class })
	private String casUser;

	@Schema(description = "机构名称")
	private String instName;
}