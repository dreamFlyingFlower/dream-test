package com.wy.test.core.web.freemarker;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 自定义Freemarker标签
 *
 * @author 飞花梦影
 * @date 2024-09-09 23:32:38
 * @git {@link https://github.com/dreamFlyingFlower}
 */
@Documented
@Target(value = ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface FreemarkerTag {

	String value() default "";
}