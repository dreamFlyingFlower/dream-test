package dream.flying.flower.framework.web.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import dream.flying.flower.framework.web.xss.XssValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

/**
 * 自定义xss校验注解
 *
 * @author 飞花梦影
 * @date 2022-11-12 15:02:00
 * @git {@link https://github.com/dreamFlyingFlower }
 */
@Documented
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target(value = { ElementType.METHOD, ElementType.FIELD, ElementType.CONSTRUCTOR, ElementType.PARAMETER })
@Constraint(validatedBy = { XssValidator.class })
public @interface Xss {

	String message() default "不允许任何脚本运行";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};
}