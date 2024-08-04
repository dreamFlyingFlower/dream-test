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
 * 同步器
 *
 * @author 飞花梦影
 * @date 2024-08-01
 * @git {@link https://github.com/dreamFlyingFlower}
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "同步器")
public class SyncVO implements Serializable, TransPojo {

	private static final long serialVersionUID = 1L;

	@Schema(description = "主键")
	@NotNull(message = "id不能为空", groups = { ValidEdit.class })
	private String id;

	@Schema(description = "名称")
	@Size(max = 64, message = "名称最大长度不能超过64", groups = { ValidAdd.class, ValidEdit.class })
	private String name;

	@Schema(description = "服务")
	@Size(max = 64, message = "服务最大长度不能超过64", groups = { ValidAdd.class, ValidEdit.class })
	private String service;

	@Schema(description = "资源类型")
	@Size(max = 32, message = "资源类型最大长度不能超过32", groups = { ValidAdd.class, ValidEdit.class })
	private String sourceType;

	@Schema(description = "定时任务")
	@Size(max = 32, message = "定时任务最大长度不能超过32", groups = { ValidAdd.class, ValidEdit.class })
	private String scheduler;

	@Schema(description = "提供者URL")
	@Size(max = 256, message = "提供者URL最大长度不能超过256", groups = { ValidAdd.class, ValidEdit.class })
	private String providerUrl;

	@Schema(description = "数据库驱动类")
	@Size(max = 128, message = "数据库驱动类最大长度不能超过128", groups = { ValidAdd.class, ValidEdit.class })
	private String driverClass;

	@Schema(description = "用户名")
	@Size(max = 64, message = "用户名最大长度不能超过64", groups = { ValidAdd.class, ValidEdit.class })
	private String principal;

	@Schema(description = "密码")
	@Size(max = 512, message = "密码最大长度不能超过512", groups = { ValidAdd.class, ValidEdit.class })
	private String credentials;

	@Schema(description = "恢复时间")
	@Size(max = 32, message = "恢复时间最大长度不能超过32", groups = { ValidAdd.class, ValidEdit.class })
	private String resumeTime;

	@Schema(description = "暂停时间")
	@Size(max = 32, message = "暂停时间最大长度不能超过32", groups = { ValidAdd.class, ValidEdit.class })
	private String suspendTime;

	@Schema(description = "用户基础信息")
	@Size(max = 200, message = "用户基础信息最大长度不能超过200", groups = { ValidAdd.class, ValidEdit.class })
	private String userBasedn;

	@Schema(description = "用户拦截")
	@Size(max = 4000, message = "用户拦截最大长度不能超过4,000", groups = { ValidAdd.class, ValidEdit.class })
	private String userFilters;

	@Schema(description = "组织机构基础信息")
	@Size(max = 200, message = "组织机构基础信息最大长度不能超过200", groups = { ValidAdd.class, ValidEdit.class })
	private String orgBasedn;

	@Schema(description = "组织机构拦截")
	@Size(max = 4000, message = "组织机构拦截最大长度不能超过4000", groups = { ValidAdd.class, ValidEdit.class })
	private String orgFilters;

	@Schema(description = "MSAD域名")
	@Size(max = 32, message = "MSAD域名最大长度不能超过32", groups = { ValidAdd.class, ValidEdit.class })
	private String msadDomain;

	@Schema(description = "是否开启SSL")
	private Integer sslSwitch;

	@Schema(description = "信任STORE")
	@Size(max = 64, message = "信任STORE最大长度不能超过64", groups = { ValidAdd.class, ValidEdit.class })
	private String trustStore;

	@Schema(description = "信任STORE密码")
	@Size(max = 64, message = "信任STORE密码最大长度不能超过64", groups = { ValidAdd.class, ValidEdit.class })
	private String trustStorePassword;

	@Schema(description = "同步时间范围（单位天）")
	private Integer syncStartTime;

	@Schema(description = "APP ID")
	@Size(max = 64, message = "APP ID最大长度不能超过64", groups = { ValidAdd.class, ValidEdit.class })
	private String appId;

	@Schema(description = "APP名称")
	@Size(max = 64, message = "APP名称最大长度不能超过64", groups = { ValidAdd.class, ValidEdit.class })
	private String appName;

	@Schema(description = "机构ID")
	@NotNull(message = "机构ID不能为空", groups = { ValidAdd.class })
	private String instId;

	@Schema(description = "备注")
	@Size(max = 256, message = "备注最大长度不能超过256", groups = { ValidAdd.class, ValidEdit.class })
	private String remark;

	@Schema(description = "状态")
	private Integer status;

	@Schema(description = "机构名称")
	private String instName;
}