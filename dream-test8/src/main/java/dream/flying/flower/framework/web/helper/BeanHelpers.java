package dream.flying.flower.framework.web.helper;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeansException;

/**
 * Bean转换工具类
 *
 * @author 飞花梦影
 * @date 2022-11-14 13:44:53
 * @git {@link https://github.com/dreamFlyingFlower }
 */
public class BeanHelpers {

	public static <T, U> void copy(T source, U target) {
		BeanUtils.copyProperties(source, target);
	}

	public static <T, U> U copy(T source, Class<U> targetClass) {
		try {
			U target = targetClass.getConstructor().newInstance();
			BeanUtils.copyProperties(source, target);
			return target;
		} catch (BeansException | InstantiationException | IllegalAccessException | IllegalArgumentException
				| InvocationTargetException | NoSuchMethodException | SecurityException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static <T, U> List<U> copy(List<T> sources, Class<U> targetClass) {
		List<U> targets = new ArrayList<>();
		sources.forEach(t -> {
			targets.add(copy(t, targetClass));
		});
		return targets;
	}
}