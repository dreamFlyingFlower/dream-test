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
 * 第三方登录用户查询
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
@Schema(description = "第三方登录用户查询")
public class SocialAssociateQuery extends AbstractQuery {

	private static final long serialVersionUID = 1L;

	@Schema(description = "用户ID")
	private String userId;

	@Schema(description = "用户名")
	private String username;

	@Schema(description = "提供者")
	private String provider;

	@Schema(description = "社交用户信息")
	private String socialUserInfo;

	@Schema(description = "社交用户ID")
	private String socialUserId;

	@Schema(description = "扩展属性")
	private String extAttr;

	@Schema(description = "Token")
	private String accessToken;

	@Schema(description = "转义")
	private String transMission;

	@Schema(description = "机构ID")
	private String instId;
}