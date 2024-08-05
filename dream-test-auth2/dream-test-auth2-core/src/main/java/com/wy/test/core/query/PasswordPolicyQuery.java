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
 * 密码策略查询
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
@Schema(description = "密码策略查询")
public class PasswordPolicyQuery extends AbstractQuery {

	private static final long serialVersionUID = 1L;

	@Schema(description = "最小长度")
	private Integer minLength;

	@Schema(description = "最大长度")
	private Integer maxLength;

	@Schema(description = "小写")
	private Integer lowerCase;

	@Schema(description = "大写")
	private Integer upperCase;

	@Schema(description = "数字")
	private Integer digits;

	@Schema(description = "特殊单词")
	private Integer specialChar;

	@Schema(description = "登录是否锁定")
	private Integer attempts;

	@Schema(description = "锁定时长")
	private Integer duration;

	@Schema(description = "密码有效期")
	private Integer expiration;

	@Schema(description = "用户名")
	private Integer username;

	@Schema(description = "历史密码")
	private Integer history;

	@Schema(description = "字典")
	private Integer dictionary;

	@Schema(description = "字母")
	private Integer alphabetical;

	@Schema(description = "数字")
	private Integer numerical;

	@Schema(description = "标准键盘")
	private Integer qwerty;

	@Schema(description = "事件")
	private Integer occurances;

	@Schema(description = "机构ID")
	private String instId;
}