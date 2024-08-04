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
 * 连接器
 *
 * @author 飞花梦影
 * @date 2024-08-01
 * @git {@link https://github.com/dreamFlyingFlower}
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "连接器")
public class ConnectorVO implements Serializable, TransPojo {

	private static final long serialVersionUID = 1L;

	@Schema(description = "主键")
	@NotNull(message = "id不能为空", groups = { ValidEdit.class })
	private String id;

	@Schema(description = "连接名")
	@Size(max = 256, message = "连接名最大长度不能超过256", groups = { ValidAdd.class, ValidEdit.class })
	private String connName;

	@Schema(description = "是否正在连接")
	private Integer justInTime;

	@Schema(description = "定时")
	@Size(max = 32, message = "定时最大长度不能超过32", groups = { ValidAdd.class, ValidEdit.class })
	private String scheduler;

	@Schema(description = "提供者URL")
	@Size(max = 256, message = "提供者URL最大长度不能超过256", groups = { ValidAdd.class, ValidEdit.class })
	private String providerUrl;

	@Schema(description = "用户名")
	@Size(max = 200, message = "用户名最大长度不能超过200", groups = { ValidAdd.class, ValidEdit.class })
	private String principal;

	@Schema(description = "密码")
	@Size(max = 512, message = "密码最大长度不能超过512", groups = { ValidAdd.class, ValidEdit.class })
	private String credentials;

	@Schema(description = "拦截")
	@Size(max = 400, message = "拦截最大长度不能超过400", groups = { ValidAdd.class, ValidEdit.class })
	private String filters;

	@Schema(description = "备注")
	@Size(max = 256, message = "备注最大长度不能超过256", groups = { ValidAdd.class, ValidEdit.class })
	private String remark;

	@Schema(description = "APP ID")
	@Size(max = 32, message = "APP ID最大长度不能超过32", groups = { ValidAdd.class, ValidEdit.class })
	private String appId;

	@Schema(description = "APP名称")
	@Size(max = 32, message = "APP名称最大长度不能超过32", groups = { ValidAdd.class, ValidEdit.class })
	private String appName;

	@Schema(description = "机构ID")
	@NotNull(message = "机构ID不能为空", groups = { ValidAdd.class })
	private Long instId;

	@Schema(description = "机构名称")
	private String instName;

	@Schema(description = "状态")
	private Integer status;
}