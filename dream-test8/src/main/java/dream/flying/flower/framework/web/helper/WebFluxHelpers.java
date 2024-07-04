package dream.flying.flower.framework.web.helper;

import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpResponse;

import com.alibaba.fastjson2.JSON;

import dream.flying.flower.enums.TipEnum;
import dream.flying.flower.result.Result;
import reactor.core.publisher.Mono;

/**
 * WebFlux 工具类
 *
 * @author 飞花梦影
 * @date 2022-09-06 17:32:24
 * @git {@link https://github.com/dreamFlyingFlower }
 */
public class WebFluxHelpers {

	/**
	 * Webflux失败响应
	 *
	 * @param response ServerHttpResponse
	 * @return Mono<Void>
	 */
	public static Mono<Void> error(ServerHttpResponse response) {
		return write(response, MediaType.APPLICATION_JSON_VALUE, HttpStatus.BAD_REQUEST, 0, TipEnum.TIP_FAIL.getMsg(),
				null);
	}

	/**
	 * Webflux失败响应
	 *
	 * @param response ServerHttpResponse
	 * @param msg 响应消息
	 * @return Mono<Void>
	 */
	public static Mono<Void> error(ServerHttpResponse response, String msg) {
		return write(response, MediaType.APPLICATION_JSON_VALUE, HttpStatus.BAD_REQUEST, 0, msg, null);
	}

	/**
	 * Webflux成功响应
	 *
	 * @param response ServerHttpResponse
	 * @param data 响应内容
	 * @return Mono<Void>
	 */
	public static Mono<Void> ok(ServerHttpResponse response, Object data) {
		return write(response, MediaType.APPLICATION_JSON_VALUE, HttpStatus.OK, 1, TipEnum.TIP_SUCCESS.getMsg(), data);
	}

	/**
	 * 设置webflux模型响应
	 *
	 * @param response ServerHttpResponse
	 * @param contentType content-type
	 * @param status http状态码
	 * @param code 响应状态码
	 * @param msg 响应信息
	 * @param data 响应内容
	 * @return Mono<Void>
	 */
	public static Mono<Void> write(ServerHttpResponse response, String contentType, HttpStatus status, int code,
			String msg, Object data) {
		response.setStatusCode(status);
		response.getHeaders().add(HttpHeaders.CONTENT_TYPE, contentType);
		DataBuffer dataBuffer =
				response.bufferFactory().wrap(JSON.toJSONString(Result.result(code, msg, data)).getBytes());
		return response.writeWith(Mono.just(dataBuffer));
	}
}