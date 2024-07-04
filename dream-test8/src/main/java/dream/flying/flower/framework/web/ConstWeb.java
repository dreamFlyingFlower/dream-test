package dream.flying.flower.framework.web;

/**
 * Web相关参数
 *
 * @author 飞花梦影
 * @date 2023-01-04 11:13:28
 * @git {@link https://github.com/dreamFlyingFlower }
 */
public interface ConstWeb {

	/** 请求头中的token字段 */
	String HEADER_TOKEN = "token";

	/** 请求头中的Authorization属性 */
	String HEADER_AUTHORIZATION = "Authorization";

	/** 请求头中Authorization属性的值前缀 */
	String HEADER_AUTHORIZATION_BEAR = "Bear ";

	/** 幂等相关 */
	String HEADER_IDEMPOTENT_CODE = "idempotentCode";
}