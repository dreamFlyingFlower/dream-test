package com.wy.test.core.vo;

import java.io.Serializable;

import javax.validation.constraints.NotBlank;
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
 * 第三方登录提供者
 *
 * @author 飞花梦影
 * @date 2024-08-01
 * @git {@link https://github.com/dreamFlyingFlower}
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "第三方登录提供者")
public class SocialProviderVO implements Serializable, TransPojo {

	private static final long serialVersionUID = 1L;

	@NotBlank(message = "id不能为空", groups = { ValidEdit.class })
	private String id;

	@Size(max = 45, message = "provider最大长度不能超过45", groups = { ValidAdd.class, ValidEdit.class })
	private String provider;

	@Size(max = 45, message = "providerName最大长度不能超过45", groups = { ValidAdd.class, ValidEdit.class })
	private String providerName;

	@Size(max = 45, message = "icon最大长度不能超过45", groups = { ValidAdd.class, ValidEdit.class })
	private String icon;

	@Size(max = 100, message = "clientId最大长度不能超过100", groups = { ValidAdd.class, ValidEdit.class })
	private String clientId;

	@Size(max = 500, message = "clientSecret最大长度不能超过500", groups = { ValidAdd.class, ValidEdit.class })
	private String clientSecret;

	@Size(max = 45, message = "agentId最大长度不能超过45", groups = { ValidAdd.class, ValidEdit.class })
	private String agentId;

	@Size(max = 45, message = "display最大长度不能超过45", groups = { ValidAdd.class, ValidEdit.class })
	private String display;

	private Integer sortIndex;

	@Size(max = 45, message = "scanCode最大长度不能超过45", groups = { ValidAdd.class, ValidEdit.class })
	private String scanCode;

	private String status;

	@NotBlank(message = "instId不能为空", groups = { ValidAdd.class })
	@Size(max = 45, message = "instId最大长度不能超过45", groups = { ValidAdd.class, ValidEdit.class })
	private String instId;

	private String redirectUri;

	private String accountId;

	private String bindTime;

	private String unBindTime;

	private String lastLoginTime;

	private String state;

	private boolean userBind;
}