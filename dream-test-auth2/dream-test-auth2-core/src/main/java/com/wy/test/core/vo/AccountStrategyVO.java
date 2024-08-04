package com.wy.test.core.vo;

import java.io.Serializable;
import java.util.Base64;

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
 * 用户账号表
 *
 * @author 飞花梦影
 * @date 2024-08-01
 * @git {@link https://github.com/dreamFlyingFlower}
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "用户账号表")
public class AccountStrategyVO implements Serializable, TransPojo {

	private static final long serialVersionUID = 1L;

	@Schema(description = "主键")
	@NotNull(message = "id不能为空", groups = { ValidEdit.class })
	private String id;

	@Schema(description = "策略名称")
	@Size(max = 32, message = "策略名称最大长度不能超过32", groups = { ValidAdd.class, ValidEdit.class })
	private String name;

	@Schema(description = "属性")
	@Size(max = 32, message = "属性最大长度不能超过32", groups = { ValidAdd.class, ValidEdit.class })
	private String mapping;

	@Schema(description = "拦截器")
	@Size(max = 64, message = "拦截器最大长度不能超过64", groups = { ValidAdd.class, ValidEdit.class })
	private String filters;

	@Schema(description = "组织机构ID列表")
	@Size(max = 32, message = "组织机构ID列表最大长度不能超过32", groups = { ValidAdd.class, ValidEdit.class })
	private String orgIds;

	@Schema(description = "后缀")
	@Size(max = 32, message = "后缀最大长度不能超过32", groups = { ValidAdd.class, ValidEdit.class })
	private String suffixes;

	@Schema(description = "应用ID")
	@Size(max = 64, message = "应用ID最大长度不能超过64", groups = { ValidAdd.class, ValidEdit.class })
	private String appId;

	@Schema(description = "应用名称")
	@Size(max = 32, message = "应用名称最大长度不能超过32", groups = { ValidAdd.class, ValidEdit.class })
	private String appName;

	@Schema(description = "创建类型")
	@Size(max = 32, message = "创建类型最大长度不能超过32", groups = { ValidAdd.class, ValidEdit.class })
	private String createType;

	@Schema(description = "机构ID")
	@NotNull(message = "机构ID不能为空", groups = { ValidAdd.class })
	private Long instId;

	@Schema(description = "用户名")
	@Size(max = 64, message = "用户名最大长度不能超过64", groups = { ValidAdd.class, ValidEdit.class })
	private String remark;

	@Schema(description = "用户状态")
	@NotNull(message = "用户状态不能为空", groups = { ValidAdd.class })
	private Integer status;

	private String instName;

	private byte[] appIcon;

	private String appIconBase64;

	public void transIconBase64() {
		if (this.appIcon != null) {
			this.appIconBase64 = "data:image/png;base64," + Base64.getEncoder().encodeToString(appIcon);
		}
	}
}