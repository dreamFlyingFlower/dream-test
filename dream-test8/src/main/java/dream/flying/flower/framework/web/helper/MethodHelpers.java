package dream.flying.flower.framework.web.helper;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.Objects;

import org.springframework.context.expression.CachedExpressionEvaluator;
import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.core.MethodParameter;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

import dream.flying.flower.lang.StrHelper;

/**
 * Spring MethodParameter工具类
 *
 * @author 飞花梦影
 * @date 2024-05-29 09:25:03
 * @git {@link https://github.com/dreamFlyingFlower}
 */
public class MethodHelpers {

	/** 该类为spring特有类,可以拿到反射中方法的形参名,也可以使用LocalVariableTableParameterNameDiscoverer */
	private static DefaultParameterNameDiscoverer defaultParameterNameDiscoverer;

	static {
		defaultParameterNameDiscoverer = new DefaultParameterNameDiscoverer();
	}

	/**
	 * 获得指定方法的指定参数
	 * 
	 * @param method 方法
	 * @param parameterIndex 参数索引.-1表示方法的返回值
	 * @return MethodParameter
	 */
	public static MethodParameter getMethodParameter(Method method, int parameterIndex) {
		return new MethodParameter(method, parameterIndex);
	}

	/**
	 * 获得方法的参数列表
	 * 
	 * @param constructor 构造反射
	 * @return 参数列表
	 */
	public static String[] getParameterNames(Constructor<?> constructor) {
		return defaultParameterNameDiscoverer.getParameterNames(constructor);
	}

	/**
	 * 获得方法的参数列表
	 * 
	 * @param method 方法反射
	 * @return 参数列表
	 */
	public static String[] getParameterNames(Method method) {
		return defaultParameterNameDiscoverer.getParameterNames(method);
	}

	/**
	 * 获得类中方法的参数列表
	 * 
	 * @param <T> 类泛型
	 * @param clazz 类
	 * @param methodName 方法名
	 * @param args 方法参数字节码
	 * @return 参数列表
	 * @throws NoSuchMethodException
	 * @throws SecurityException
	 */
	public static <T> String[] getParameterNames(Class<T> clazz, String methodName, Class<?>... args)
			throws NoSuchMethodException, SecurityException {
		Method method = clazz.getMethod(methodName, args);
		return defaultParameterNameDiscoverer.getParameterNames(method);
	}

	/**
	 * EL表达式解析,参照#CacheOperationExpressionEvaluator 或{@link CachedExpressionEvaluator}
	 *
	 * @param expression EL表达式
	 * @param method 方法
	 * @param args 参数
	 * @return Spel表达式解析后的值
	 */
	public static Object parseSpel(String expression, Method method, Object[] args) {
		if (StrHelper.isBlank(expression)) {
			return null;
		}
		String[] parameterNames = defaultParameterNameDiscoverer.getParameterNames(method);
		// SPEL解析
		ExpressionParser parser = new SpelExpressionParser();
		StandardEvaluationContext context = new StandardEvaluationContext();
		for (int i = 0; i < Objects.requireNonNull(parameterNames).length; i++) {
			context.setVariable(parameterNames[i], args[i]);
		}
		return parser.parseExpression(expression).getValue(context);
	}
}