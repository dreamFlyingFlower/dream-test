package dream.flying.flower.framework.web.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Data;

/**
 * 加密参数
 *
 * @author 飞花梦影
 * @date 2022-12-20 14:50:34
 * @git {@link https://github.com/dreamFlyingFlower }
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "dream.crypto")
public class CryptoProperties {

	private boolean enabled = true;

	private String secretKey = "1234567890qazwsx";
}