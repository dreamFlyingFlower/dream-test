package com.wy.test.core.query;

import dream.flying.flower.framework.web.query.AbstractQuery;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

/**
 * 机构查询
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
@Schema(description = "机构查询")
public class InstitutionQuery extends AbstractQuery {

	private static final long serialVersionUID = 1L;

	@Schema(description = "机构名称")
	private String name;

	@Schema(description = "机构全名")
	private String fullName;

	@Schema(description = "分区")
	private String division;

	@Schema(description = "国家")
	private String country;

	@Schema(description = "省")
	private String region;

	@Schema(description = "城市")
	private String locality;

	@Schema(description = "街道")
	private String street;

	@Schema(description = "地址")
	private String address;

	@Schema(description = "联系人")
	private String contact;

	@Schema(description = "邮编")
	private String postalCode;

	@Schema(description = "电话")
	private String phone;

	@Schema(description = "传真")
	private String fax;

	@Schema(description = "邮箱")
	private String email;

	@Schema(description = "logo")
	private String logo;

	@Schema(description = "域名")
	private String domain;

	@Schema(description = "前端标题")
	private String frontTitle;

	@Schema(description = "控制台域名")
	private String consoleDomain;

	@Schema(description = "控制台标题")
	private String consoleTitle;

	@Schema(description = "验证码")
	private String captcha;

	@Schema(description = "默认URI")
	private String defaultUri;

	@Schema(description = "描述")
	private String remark;

	@Schema(description = "排序")
	private Integer sortIndex;

	@Schema(description = "机构ID")
	private String instId;

	@Schema(description = "状态")
	private Integer status;
}