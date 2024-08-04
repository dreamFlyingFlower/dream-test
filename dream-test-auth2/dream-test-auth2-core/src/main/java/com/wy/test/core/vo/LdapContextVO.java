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
 * LDAP上下文
 *
 * @author 飞花梦影
 * @date 2024-08-01
 * @git {@link https://github.com/dreamFlyingFlower}
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "LDAP上下文")
public class LdapContextVO implements Serializable, TransPojo {

	private static final long serialVersionUID = 1L;

	@Schema(description = "主键")
	@NotNull(message = "id不能为空", groups = { ValidEdit.class })
	private String id;

	@Schema(description = "产品")
	@Size(max = 32, message = "产品最大长度不能超过32", groups = { ValidAdd.class, ValidEdit.class })
	private String product;

	@Schema(description = "是否使用SSL")
	@Size(max = 4, message = "是否使用SSL最大长度不能超过4", groups = { ValidAdd.class, ValidEdit.class })
	private String sslSwitch;

	@Schema(description = "提供者URL")
	@Size(max = 128, message = "提供者URL最大长度不能超过128", groups = { ValidAdd.class, ValidEdit.class })
	private String providerUrl;

	@Schema(description = "用户名")
	@Size(max = 64, message = "用户名最大长度不能超过64", groups = { ValidAdd.class, ValidEdit.class })
	private String principal;

	@Schema(description = "密码")
	@Size(max = 512, message = "密码最大长度不能超过512", groups = { ValidAdd.class, ValidEdit.class })
	private String credentials;

	@Schema(description = "基础DN")
	@Size(max = 512, message = "基础DN最大长度不能超过512", groups = { ValidAdd.class, ValidEdit.class })
	private String basedn;

	@Schema(description = "拦截")
	@Size(max = 512, message = "拦截最大长度不能超过512", groups = { ValidAdd.class, ValidEdit.class })
	private String filters;

	@Schema(description = "信任")
	@Size(max = 512, message = "信任最大长度不能超过512", groups = { ValidAdd.class, ValidEdit.class })
	private String trustStore;

	@Schema(description = "信任密码")
	@Size(max = 128, message = "信任密码最大长度不能超过128", groups = { ValidAdd.class, ValidEdit.class })
	private String trustStorePassword;

	@Schema(description = "MSAD域名")
	@Size(max = 128, message = "MSAD域名最大长度不能超过128", groups = { ValidAdd.class, ValidEdit.class })
	private String msadDomain;

	@Schema(description = "帐号")
	@Size(max = 32, message = "帐号最大长度不能超过32", groups = { ValidAdd.class, ValidEdit.class })
	private String accountMapping;

	@Schema(description = "机构ID")
	private String instId;

	@Schema(description = "备注")
	@Size(max = 256, message = "备注最大长度不能超过256", groups = { ValidAdd.class, ValidEdit.class })
	private String remark;

	@Schema(description = "状态")
	private Integer status;

	@Schema(description = "机构名称")
	private String instName;
}