package dream.flying.flower.framework.web.xss;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.util.StringUtils;

import dream.flying.flower.framework.web.annotation.Xss;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

/**
 * 自定义xss校验注解实现
 *
 * @author 飞花梦影
 * @date 2022-11-12 15:03:19
 * @git {@link https://github.com/dreamFlyingFlower }
 */
public class XssValidator implements ConstraintValidator<Xss, String> {

	private static final String HTML_PATTERN = "<(\\S*?)[^>]*>.*?|<.*? />";

	@Override
	public boolean isValid(String value, ConstraintValidatorContext constraintValidatorContext) {
		if (!StringUtils.hasText(value)) {
			return true;
		}
		return !containsHtml(value);
	}

	public static boolean containsHtml(String value) {
		Pattern pattern = Pattern.compile(HTML_PATTERN);
		Matcher matcher = pattern.matcher(value);
		return matcher.matches();
	}
}