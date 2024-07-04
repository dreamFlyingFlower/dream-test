package dream.flying.flower.framework.web.helper;

import java.util.List;

import org.springframework.util.AntPathMatcher;

import dream.flying.flower.collection.ListHelper;
import dream.flying.flower.lang.StrHelper;

/**
 * URL匹配
 *
 * @author 飞花梦影
 * @date 2022-09-08 14:24:17
 * @git {@link https://github.com/dreamFlyingFlower }
 */
public class PathHelpers {

	/**
	 * 查找指定字符串是否匹配指定字符串列表中的任意一个字符串
	 * 
	 * @param str 指定字符串
	 * @param strs 需要检查的字符串数组
	 * @return 是否匹配
	 */
	public static boolean matches(String str, List<String> strs) {
		if (StrHelper.isBlank(str) || ListHelper.isEmpty(strs)) {
			return false;
		}
		for (String pattern : strs) {
			if (isMatch(pattern, str)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 判断url是否与规则配置: ? 表示单个字符; * 表示一层路径内的任意字符串,不可跨层级; ** 表示任意层路径;
	 * 
	 * @param pattern 匹配规则
	 * @param url 需要匹配的url
	 * @return
	 */
	public static boolean isMatch(String pattern, String url) {
		AntPathMatcher matcher = new AntPathMatcher();
		return matcher.match(pattern, url);
	}
}