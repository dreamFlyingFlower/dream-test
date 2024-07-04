package dream.flying.flower.framework.web.helper;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Enumeration;
import java.util.Map;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.util.LinkedCaseInsensitiveMap;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.servlet.HandlerMapping;
import org.springframework.web.util.WebUtils;

import dream.flying.flower.ConstLang;
import dream.flying.flower.framework.core.json.JsonHelpers;
import dream.flying.flower.helper.ConvertHepler;
import dream.flying.flower.lang.StrHelper;
import dream.flying.flower.result.Result;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;

/**
 * Web 工具类,可参考 {@link WebUtils}
 *
 * @author 飞花梦影
 * @date 2022-09-06 17:32:24
 * @git {@link https://github.com/dreamFlyingFlower }
 */
@Slf4j
public class WebHelpers {

	/**
	 * 获取Boolean参数
	 */
	public static Boolean getBoolean(String name) {
		return ConvertHepler.toBool(getRequest().getParameter(name));
	}

	/**
	 * 获取Boolean参数
	 */
	public static Boolean getBoolean(String name, Boolean defaultValue) {
		return ConvertHepler.toBool(getRequest().getParameter(name), defaultValue);
	}

	/**
	 * 获得请求域名
	 * 
	 * @return 域名
	 */
	public static String getDomain() {
		return getDomain(getRequest());
	}

	/**
	 * 获得请求域名
	 * 
	 * @param request 请求
	 * @return 域名
	 */
	public static String getDomain(HttpServletRequest request) {
		StringBuffer url = request.getRequestURL();
		return url.delete(url.length() - request.getRequestURI().length(), url.length()).toString();
	}

	/**
	 * 获取请求头中的属性
	 * 
	 * @param name 属性名
	 * @return 属性值
	 */
	public static String getHeader(String name) {
		return getHeader(getRequest(), name);
	}

	/**
	 * 获取请求头中的属性
	 * 
	 * @param request 请求
	 * @param name 属性名
	 * @return 属性值
	 */
	public static String getHeader(HttpServletRequest request, String name) {
		String value = request.getHeader(name);
		if (StrHelper.isEmpty(value)) {
			return ConstLang.STR_EMPTY;
		}
		return urlDecode(value);
	}

	/**
	 * 获得请求头
	 * 
	 * @param request 请求
	 * @return 请求头参数
	 */
	public static Map<String, String> getHeaders(HttpServletRequest request) {
		Map<String, String> map = new LinkedCaseInsensitiveMap<>();
		Enumeration<String> enumeration = request.getHeaderNames();
		if (enumeration != null) {
			while (enumeration.hasMoreElements()) {
				String key = enumeration.nextElement();
				map.put(key, request.getHeader(key));
			}
		}
		return map;
	}

	/**
	 * 获取指定参数的Integer类型参数值
	 * 
	 * @param name 参数名
	 * @return 参数值
	 */
	public static Integer getInt(String name) {
		return ConvertHepler.toInt(getRequest().getParameter(name));
	}

	/**
	 * 获取指定参数的Integer类型参数值
	 * 
	 * @param name 参数名
	 * @param request 请求
	 * @return 参数值
	 */
	public static Integer getInt(String name, HttpServletRequest request) {
		return ConvertHepler.toInt(request.getParameter(name));
	}

	/**
	 * 获取指定参数的Integer类型参数值
	 * 
	 * @param name 参数名
	 * @param defaultValue 默认值
	 * @return 参数值
	 */
	public static Integer getInt(String name, Integer defaultValue) {
		return ConvertHepler.toInt(getRequest().getParameter(name), defaultValue);
	}

	/**
	 * 获得Origin
	 * 
	 * @return Origin
	 */
	public static String getOrigin() {
		return getHeader(HttpHeaders.ORIGIN);
	}

	/**
	 * 获得Origin
	 * 
	 * @param request 请求
	 * @return Origin
	 */
	public static String getOrigin(HttpServletRequest request) {
		return getHeader(request, HttpHeaders.ORIGIN);
	}

	/**
	 * 获取指定参数的Integer类型参数值
	 * 
	 * @param name 参数名
	 * @param defaultValue 默认值
	 * @param request 请求
	 * @return 参数值
	 */
	public static Integer getInt(String name, Integer defaultValue, HttpServletRequest request) {
		return ConvertHepler.toInt(request.getParameter(name), defaultValue);
	}

	/**
	 * 获取指定参数的参数值
	 * 
	 * @param name 参数名
	 * @return 参数值
	 */
	public static String getParameter(String name) {
		return getRequest().getParameter(name);
	}

	/**
	 * 获取指定参数的参数值
	 * 
	 * @param name 参数名
	 * @param defaultValue 默认值
	 * @return 参数值
	 */
	public static String getParameter(String name, String defaultValue) {
		return ConvertHepler.toStr(getRequest().getParameter(name), defaultValue);
	}

	/**
	 * 获得所有参数拼接在URL后的参数
	 * 
	 * @param request 请求
	 * @return 参数Map
	 */
	public static Map<String, String[]> getParameterMap(HttpServletRequest request) {
		return request.getParameterMap();
	}

	/**
	 * 获得PathVariable注解中的参数
	 * 
	 * @param request 请求
	 * @return Map
	 */
	@SuppressWarnings("unchecked")
	public static Map<String, String> getPathVariableParameter(HttpServletRequest request) {
		return (Map<String, String>) request.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);
	}

	/**
	 * 获得PathVariable注解中的参数
	 * 
	 * @param request 请求
	 * @return Map
	 */
	@SuppressWarnings("unchecked")
	public static Map<String, String> getPathVariableWebParameter(HttpServletRequest request) {
		ServletWebRequest webRequest = new ServletWebRequest(request, null);
		return (Map<String, String>) webRequest.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE,
				RequestAttributes.SCOPE_REQUEST);
	}

	/**
	 * 获取request
	 * 
	 * @return HttpServletRequest
	 */
	public static HttpServletRequest getRequest() {
		return getRequestAttributes().getRequest();
	}

	/**
	 * 获得ServletRequestAttributes
	 * 
	 * @return ServletRequestAttributes
	 */
	public static ServletRequestAttributes getRequestAttributes() {
		return (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
	}

	/**
	 * 获取response
	 * 
	 * @return HttpServletResponse
	 */
	public static HttpServletResponse getResponse() {
		return getRequestAttributes().getResponse();
	}

	/**
	 * 获取session
	 * 
	 * @return HttpSession
	 */
	public static HttpSession getSession() {
		return getRequest().getSession();
	}

	/**
	 * 是否是Ajax异步请求
	 * 
	 * @param request 请求
	 * @return true->ajax请求;false->不是ajax请求
	 */
	public static boolean isAjax(HttpServletRequest request) {
		String accept = request.getHeader("accept");
		if (accept != null && accept.contains("application/json")) {
			return true;
		}

		String xRequestedWith = request.getHeader("X-Requested-With");
		if (xRequestedWith != null && xRequestedWith.contains("XMLHttpRequest")) {
			return true;
		}

		String uri = request.getRequestURI();
		if (StrHelper.containsAny(uri.toLowerCase(), ".json", ".xml")) {
			return true;
		}

		String ajax = request.getParameter("__ajax");
		return StrHelper.containsAny(ajax.toLowerCase(), "json", "xml");
	}

	/**
	 * 将结果渲染到客户端
	 * 
	 * @param response 响应
	 * @param object 待渲染的对象
	 */
	public static void render(HttpServletResponse response, Object object) {
		try {
			response.setStatus(200);
			response.setContentType(MediaType.APPLICATION_JSON_VALUE);
			response.setCharacterEncoding(ConstLang.DEFAULT_CHARSET_NAME);
			response.getWriter().print(object);
		} catch (IOException e) {
			log.error(e.getMessage());
			e.printStackTrace();
		}
	}

	/**
	 * 内容编码
	 * 
	 * @param str 内容
	 * @return 编码后的内容
	 */
	public static String urlEncode(String str) {
		try {
			return URLEncoder.encode(str, ConstLang.DEFAULT_CHARSET_NAME);
		} catch (UnsupportedEncodingException e) {
			log.error(e.getMessage());
			return ConstLang.STR_EMPTY;
		}
	}

	/**
	 * 内容解码
	 * 
	 * @param str 内容
	 * @return 解码后的内容
	 */
	public static String urlDecode(String str) {
		try {
			return URLDecoder.decode(str, ConstLang.DEFAULT_CHARSET_NAME);
		} catch (UnsupportedEncodingException e) {
			log.error(e.getMessage());
			return ConstLang.STR_EMPTY;
		}
	}

	/**
	 * 将结果写到响应流中
	 * 
	 * @param response 响应
	 * @param result 结果
	 * @throws IOException
	 */
	public static void write(HttpServletResponse response, Result<?> result) {
		try {
			response.setContentType(MediaType.APPLICATION_JSON_VALUE);
			response.setCharacterEncoding(StandardCharsets.UTF_8.name());
			response.setHeader("Access-Control-Allow-Credentials", "true");
			response.setHeader("Access-Control-Allow-Origin", getHeader(HttpHeaders.ORIGIN));
			JsonHelpers.write(response.getOutputStream(), result);
		} catch (IOException e) {
			e.printStackTrace();
			log.error(e.getMessage());
		}
	}

	public static <T> void write(HttpServletResponse response, T data) {
		write(response, Result.ok(data));
	}

	public static void writeError(HttpServletResponse response) {
		write(response, Result.error());
	}

	public static void writeOk(HttpServletResponse response) {
		write(response, Result.ok());
	}
}