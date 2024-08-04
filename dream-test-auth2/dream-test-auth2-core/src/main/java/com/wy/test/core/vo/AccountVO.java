package com.wy.test.core.vo;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fhs.core.trans.vo.TransPojo;
import com.wy.test.core.entity.OrgCastEntity;

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
public class AccountVO implements Serializable, TransPojo {

	private static final long serialVersionUID = 1L;

	@Schema(description = "主键")
	@NotNull(message = "id不能为空", groups = { ValidEdit.class })
	private String id;

	@Schema(description = "用户ID")
	@Size(max = 32, message = "用户ID最大长度不能超过32", groups = { ValidAdd.class, ValidEdit.class })
	private String userId;

	@Schema(description = "用户名")
	@Size(max = 32, message = "用户名最大长度不能超过32", groups = { ValidAdd.class, ValidEdit.class })
	private String username;

	@Schema(description = "用户显示名")
	@Size(max = 64, message = "用户显示名最大长度不能超过64", groups = { ValidAdd.class, ValidEdit.class })
	private String displayName;

	@Schema(description = "策略名称")
	@Size(max = 32, message = "策略名称最大长度不能超过32", groups = { ValidAdd.class, ValidEdit.class })
	private String strategyName;

	@Schema(description = "ID策略")
	@Size(max = 32, message = "ID策略最大长度不能超过32", groups = { ValidAdd.class, ValidEdit.class })
	private String strategyId;

	@Schema(description = "应用ID")
	@Size(max = 64, message = "应用ID最大长度不能超过64", groups = { ValidAdd.class, ValidEdit.class })
	private String appId;

	@Schema(description = "应用名称")
	@Size(max = 32, message = "应用名称最大长度不能超过32", groups = { ValidAdd.class, ValidEdit.class })
	private String appName;

	@Schema(description = "用户名")
	@Size(max = 64, message = "用户名最大长度不能超过64", groups = { ValidAdd.class, ValidEdit.class })
	private String relatedUsername;

	@Schema(description = "密码")
	@Size(max = 256, message = "密码最大长度不能超过256", groups = { ValidAdd.class, ValidEdit.class })
	private String relatedPassword;

	@Schema(description = "创建类型")
	@Size(max = 32, message = "创建类型最大长度不能超过32", groups = { ValidAdd.class, ValidEdit.class })
	private String createType;

	@Schema(description = "机构ID")
	@NotNull(message = "机构ID不能为空", groups = { ValidAdd.class })
	private Long instId;

	@Schema(description = "用户状态")
	@NotNull(message = "用户状态不能为空", groups = { ValidAdd.class })
	private Integer status;

	private String instName;

	private UserVO userInfo;

	@JsonIgnore
	@Builder.Default
	private HashMap<String, OrgCastEntity> orgCast = new HashMap<String, OrgCastEntity>();

	public HashMap<String, OrgCastEntity> getOrgCast() {
		return orgCast;
	}

	public void setOrgCast(HashMap<String, OrgCastEntity> orgCast) {
		this.orgCast = orgCast;
	}

	public void setOrgCast(List<OrgCastEntity> listOrgCast) {
		for (OrgCastEntity cast : listOrgCast) {
			this.orgCast.put(cast.getProvider(), cast);
		}
	}
}