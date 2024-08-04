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
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 第三方登录用户
 *
 * @author 飞花梦影
 * @date 2024-08-01
 * @git {@link https://github.com/dreamFlyingFlower}
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "第三方登录用户")
public class SocialAssociateVO implements Serializable, TransPojo {

	private static final long serialVersionUID = 1L;

	@Schema(description = "主键")
	@NotNull(message = "id不能为空", groups = { ValidEdit.class })
	private String id;

	@Schema(description = "用户ID")
	@NotBlank(message = "用户ID不能为空", groups = { ValidAdd.class })
	@Size(max = 64, message = "用户ID最大长度不能超过64", groups = { ValidAdd.class, ValidEdit.class })
	private String userId;

	@Schema(description = "用户名")
	@NotBlank(message = "用户名不能为空", groups = { ValidAdd.class })
	@Size(max = 32, message = "用户名最大长度不能超过32", groups = { ValidAdd.class, ValidEdit.class })
	private String username;

	@Schema(description = "提供者")
	@NotBlank(message = "提供者不能为空", groups = { ValidAdd.class })
	@Size(max = 64, message = "提供者最大长度不能超过64", groups = { ValidAdd.class, ValidEdit.class })
	private String provider;

	@Schema(description = "社交用户信息")
	@Size(max = 65535, message = "社交用户信息最大长度不能超过65535", groups = { ValidAdd.class, ValidEdit.class })
	private String socialUserInfo;

	@Schema(description = "社交用户ID")
	@NotBlank(message = "社交用户ID不能为空", groups = { ValidAdd.class })
	@Size(max = 100, message = "社交用户ID最大长度不能超过100", groups = { ValidAdd.class, ValidEdit.class })
	private String socialUserId;

	@Schema(description = "扩展属性")
	@Size(max = 65535, message = "扩展属性最大长度不能超过65535", groups = { ValidAdd.class, ValidEdit.class })
	private String extAttr;

	@Schema(description = "Token")
	@Size(max = 65535, message = "Token最大长度不能超过65535", groups = { ValidAdd.class, ValidEdit.class })
	private String accessToken;

	@Schema(description = "转义")
	@Size(max = 32, message = "转义最大长度不能超过32", groups = { ValidAdd.class, ValidEdit.class })
	private String transMission;

	@Schema(description = "机构ID")
	@NotNull(message = "机构ID不能为空", groups = { ValidAdd.class })
	private String instId;

	@Schema(description = "机构名称")
	private String instName;

	@Schema(description = "提供者名称")
	private String providerName;

	@Schema(description = "图标")
	private String icon;
}