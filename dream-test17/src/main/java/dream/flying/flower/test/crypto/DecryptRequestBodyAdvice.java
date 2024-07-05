package dream.flying.flower.test.crypto;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.RequestBodyAdvice;

import dream.flying.flower.framework.core.annotation.DecryptRequest;
import dream.flying.flower.framework.core.json.JsonHelpers;
import dream.flying.flower.framework.core.strategy.CryptContext;
import dream.flying.flower.framework.web.annotation.SecurityController;
import dream.flying.flower.framework.web.properties.DecryptRequestProperties;
import dream.flying.flower.io.IOHelper;
import dream.flying.flower.lang.StrHelper;
import lombok.extern.slf4j.Slf4j;

/**
 * 请求自动解密,只能对RequestBody进行处理,只拦截含有SecurityController注解的Controller
 *
 * @author 飞花梦影
 * @date 2022-12-20 14:57:47
 * @git {@link https://github.com/dreamFlyingFlower }
 */
@ControllerAdvice(annotations = SecurityController.class)
@EnableConfigurationProperties(DecryptRequestProperties.class)
@ConditionalOnMissingClass
@ConditionalOnProperty(prefix = "dream.decrypt-request", value = "enabled", matchIfMissing = true)
@Slf4j
public class DecryptRequestBodyAdvice implements RequestBodyAdvice {

	private final DecryptRequestProperties decryptRequestProperties;

	public DecryptRequestBodyAdvice(DecryptRequestProperties decryptRequestProperties) {
		this.decryptRequestProperties = decryptRequestProperties;
	}

	/**
	 * 方法上有Decrypto注解的,进入此拦截器
	 * 
	 * @param methodParameter 方法参数对象
	 * @param targetType 参数的类型
	 * @param converterType 消息转换器
	 * @return true,进入,false,跳过
	 */
	@Override
	public boolean supports(MethodParameter methodParameter, Type targetType,
			Class<? extends HttpMessageConverter<?>> converterType) {
		return methodParameter.getMethod().isAnnotationPresent(DecryptRequest.class);
	}

	/**
	 * 
	 * @param inputMessage
	 * @param parameter
	 * @param targetType
	 * @param converterType
	 * @return
	 * @throws IOException
	 */
	@Override
	public HttpInputMessage beforeBodyRead(HttpInputMessage inputMessage, MethodParameter parameter, Type targetType,
			Class<? extends HttpMessageConverter<?>> converterType) throws IOException {
		DecryptRequest decryptRequest = parameter.getMethod().getAnnotation(DecryptRequest.class);
		String secretKey = StrHelper.getDefault(decryptRequest.value(), decryptRequestProperties.getSecretKey());
		if (StrHelper.isBlank(secretKey)) {
			log.error("@@@未配置加密密钥,不进行加密!");
			return inputMessage;
		}

		// 获取数据
		String body = IOHelper.copyToString(inputMessage.getBody());
		log.info("@@@解密前数据:{}", body);
		if (StrHelper.isBlank(body)) {
			return inputMessage;
		}

		final String decryptData =
				new CryptContext(decryptRequest.cryptType()).decrypt(secretKey, JsonHelpers.toJson(body));

		return new DecryptInputMessage(inputMessage.getHeaders(), IOHelper.toInputStream(decryptData));

		// 强制所有实体类必须继承BaseRequestEntity,设置时间戳
		// if (result instanceof BaseRequestEntity) {
		// Long currentTimeMillis = ((BaseRequestEntity) result).getRequestTime();
		// // 有效期 60秒
		// long effective = 60 * 1000;
		//
		// long expire = System.currentTimeMillis() - currentTimeMillis;
		//
		// // 是否在有效期内
		// if (Math.abs(expire) > effective) {
		// throw new ResultException("时间戳不合法");
		// }

		// } else {
		// throw new ResultException(
		// String.format("请求参数类型:%s 未继承:%s", result.getClass().getName(),
		// BaseRequestEntity.class.getName()));
		// }
	}

	/**
	 * 转换之后,执行此方法,解密,赋值
	 * 
	 * @param body spring解析完的参数
	 * @param inputMessage 输入参数
	 * @param parameter 参数对象
	 * @param targetType 参数类型
	 * @param converterType 消息转换类型
	 * @return 真实的参数
	 */
	@Override
	public Object afterBodyRead(Object body, HttpInputMessage inputMessage, MethodParameter parameter, Type targetType,
			Class<? extends HttpMessageConverter<?>> converterType) {
		return body;
	}

	/**
	 * 如果body为空,直接转发
	 * 
	 * @param body spring解析完的参数
	 * @param inputMessage 输入参数
	 * @param parameter 参数对象
	 * @param targetType 参数类型
	 * @param converterType 消息转换类型
	 * @return 真实的参数
	 */
	@Override
	public Object handleEmptyBody(Object body, HttpInputMessage inputMessage, MethodParameter parameter,
			Type targetType, Class<? extends HttpMessageConverter<?>> converterType) {
		return body;
	}

	private static class DecryptInputMessage implements HttpInputMessage {

		private HttpHeaders httpHeaders;

		private InputStream inputStream;

		public DecryptInputMessage(HttpHeaders httpHeaders, InputStream inputStream) {
			this.httpHeaders = httpHeaders;
			this.inputStream = inputStream;
		}

		@Override
		public HttpHeaders getHeaders() {
			return httpHeaders;
		}

		@Override
		public InputStream getBody() throws IOException {
			return inputStream;
		}
	}
}