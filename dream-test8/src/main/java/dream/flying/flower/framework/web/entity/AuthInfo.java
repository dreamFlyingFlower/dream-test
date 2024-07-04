package dream.flying.flower.framework.web.entity;

import java.io.Serializable;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 认证信息
 *
 * @author 飞花梦影
 * @date 2022-11-12 15:23:44
 * @git {@link https://github.com/dreamFlyingFlower }
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuthInfo implements Serializable {

	private static final long serialVersionUID = -1626901429784302276L;

	@Schema(description = "令牌")
	private String accessToken;

	@Schema(description = "令牌类型")
	private String tokenType;

	@Schema(description = "头像URL地址")
	private String avatar;

	@Schema(description = "角色名")
	private String authority;

	@Schema(description = "用户名")
	private String username;

	@Schema(description = "账号名")
	private String account;

	@Schema(description = "过期时间")
	private Long expiresIn;

	@Schema(description = "许可证")
	private String license;
}