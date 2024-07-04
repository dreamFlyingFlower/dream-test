package dream.flying.flower.framework.web.helper;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Map;

import org.springframework.core.MethodIntrospector;
import org.springframework.core.annotation.AnnotatedElementUtils;

/**
 * 注解工具类
 *
 * @author 飞花梦影
 * @date 2024-05-08 16:39:28
 * @git {@link https://github.com/dreamFlyingFlower}
 */
public class AnnotaionHelpers {

	/**
	 * 获得类中带有指定注解的方法
	 * 
	 * @param <T> 注解
	 * @param clazz 类
	 * @param metadataClass 注解类
	 * @return 被注解修饰的方法->注解
	 */
	public static <T extends Annotation> Map<Method, T> findAnnotationMethod(Class<?> clazz, Class<T> metadataClass) {
		return MethodIntrospector.selectMethods(clazz, new MethodIntrospector.MetadataLookup<T>() {

			@Override
			public T inspect(Method method) {
				return AnnotatedElementUtils.findMergedAnnotation(method, metadataClass);
			}
		});
	}
}