package com.wy.test.core.web;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import com.wy.test.core.constant.ContentType;

import dream.flying.flower.framework.web.helper.TokenHelpers;
import dream.flying.flower.framework.core.json.JsonHelpers;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class HttpRequestAdapter {

	private String mediaType = ContentType.APPLICATION_FORM;

	HashMap<String, String> headers = new HashMap<String, String>();

	public HttpRequestAdapter() {
	}

	public HttpRequestAdapter(String mediaType) {
		this.mediaType = mediaType;
	}

	public String post(String url, Map<String, Object> parameterMap) {
		setContentType(ContentType.APPLICATION_FORM);
		return post(url, parameterMap, headers);
	}

	public HttpRequestAdapter addHeaderAuthorizationBearer(String token) {
		headers.put("Authorization", TokenHelpers.createBearer(token));
		return this;
	}

	public HttpRequestAdapter addHeaderAuthorizationBasic(String username, String password) {
		headers.put("Authorization", TokenHelpers.createBasic(username, password));
		return this;
	}

	public HttpRequestAdapter setContentType(String contentType) {
		headers.put("Content-Type", contentType);
		return this;
	}

	public HttpRequestAdapter addHeader(String name, String value) {
		headers.put(name, value);
		return this;
	}

	public String post(String url, Map<String, Object> parameterMap, HashMap<String, String> headers) {
		// 创建httpClient实例
		CloseableHttpClient httpClient = HttpClients.createDefault();
		CloseableHttpResponse httpResponse = null;
		// 创建httpPost远程连接实例
		HttpPost httpMethod = new HttpPost(url);
		// 配置请求参数实例
		setRequestConfig(httpMethod);
		// 设置请求头
		buildHeader(httpMethod, headers);

		// 封装post请求参数
		if (null != parameterMap && parameterMap.size() > 0) {
			if (mediaType.equals(ContentType.APPLICATION_FORM)) {
				// 为httpPost设置封装好的请求参数
				try {
					httpMethod.setEntity(buildFormEntity(parameterMap));
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
			} else if (mediaType.equals(ContentType.APPLICATION_JSON)) {
				String jsonString = JsonHelpers.toString(parameterMap);
				StringEntity stringEntity = new StringEntity(jsonString, "UTF-8");
				stringEntity.setContentType(ContentType.APPLICATION_JSON);
				httpMethod.setEntity(stringEntity);
			}
			log.trace("Post Message \n{} ", httpMethod.getEntity().toString());
		}

		try {
			// httpClient对象执行post请求,并返回响应参数对象
			httpResponse = httpClient.execute(httpMethod);
			// 从响应对象中获取响应内容
			return resolveHttpResponse(httpResponse);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			close(httpClient, httpResponse);// 关闭资源
		}
		return null;
	}

	public String post(String url, Object data) {
		// 创建httpClient实例
		CloseableHttpClient httpClient = HttpClients.createDefault();
		CloseableHttpResponse httpResponse = null;
		// 创建httpPost远程连接实例
		HttpPost httpMethod = new HttpPost(url);
		// 配置请求参数实例
		setRequestConfig(httpMethod);
		// 设置请求头
		buildHeader(httpMethod, headers);

		// 封装put请求参数
		String jsonString = JsonHelpers.toString(data);
		StringEntity stringEntity = new StringEntity(jsonString, "UTF-8");
		stringEntity.setContentType(ContentType.APPLICATION_JSON);
		httpMethod.setEntity(stringEntity);
		log.debug("Post Message \n{} ", httpMethod.getEntity().toString());

		try {
			// httpClient对象执行put请求,并返回响应参数对象
			httpResponse = httpClient.execute(httpMethod);
			// 从响应对象中获取响应内容
			return resolveHttpResponse(httpResponse);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			close(httpClient, httpResponse);// 关闭资源
		}
		return null;
	}

	public String put(String url, Object data) {
		// 创建httpClient实例
		CloseableHttpClient httpClient = HttpClients.createDefault();
		CloseableHttpResponse httpResponse = null;
		// 创建httpPost远程连接实例
		HttpPut httpMethod = new HttpPut(url);
		// 配置请求参数实例
		setRequestConfig(httpMethod);
		// 设置请求头
		buildHeader(httpMethod, headers);

		// 封装put请求参数
		String jsonString = JsonHelpers.toString(data);
		StringEntity stringEntity = new StringEntity(jsonString, "UTF-8");
		stringEntity.setContentType(ContentType.APPLICATION_JSON);
		httpMethod.setEntity(stringEntity);
		log.debug("Put Message \n{} ", httpMethod.getEntity().toString());

		try {
			// httpClient对象执行put请求,并返回响应参数对象
			httpResponse = httpClient.execute(httpMethod);
			// 从响应对象中获取响应内容
			return resolveHttpResponse(httpResponse);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			close(httpClient, httpResponse);// 关闭资源
		}
		return null;
	}

	public String get(String url) {
		headers.put("Content-Type", ContentType.APPLICATION_FORM);
		return get(url, headers);
	}

	public String get(String url, HashMap<String, String> headers) {
		// 创建httpClient实例
		CloseableHttpClient httpClient = HttpClients.createDefault();
		CloseableHttpResponse httpResponse = null;
		// 创建httpPost远程连接实例
		HttpGet httpMethod = new HttpGet(url);
		// 配置请求参数实例
		setRequestConfig(httpMethod);
		// 设置请求头
		buildHeader(httpMethod, headers);

		try {
			// httpClient对象执行get请求,并返回响应参数对象
			httpResponse = httpClient.execute(httpMethod);
			// 从响应对象中获取响应内容
			return resolveHttpResponse(httpResponse);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			close(httpClient, httpResponse);// 关闭资源
		}
		return null;
	}

	public String delete(String url) {
		// 创建httpClient实例
		CloseableHttpClient httpClient = HttpClients.createDefault();
		CloseableHttpResponse httpResponse = null;
		// 创建HttpDelete远程连接实例
		HttpDelete httpMethod = new HttpDelete(url);
		// 配置请求参数实例
		setRequestConfig(httpMethod);
		// 设置请求头
		buildHeader(httpMethod, headers);

		try {
			// httpClient对象执行post请求,并返回响应参数对象
			httpResponse = httpClient.execute(httpMethod);
			// 从响应对象中获取响应内容
			return resolveHttpResponse(httpResponse);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			close(httpClient, httpResponse);// 关闭资源
		}
		return null;
	}

	String resolveHttpResponse(CloseableHttpResponse httpResponse) throws ParseException, IOException {
		HttpEntity entity = httpResponse.getEntity();
		String content = EntityUtils.toString(entity);
		HttpStatus httpStatus = HttpStatus.valueOf(httpResponse.getStatusLine().getStatusCode());
		log.debug("Http Response HttpStatus {} ", httpStatus);
		log.trace("Http Response Content {} ", content);
		return content;
	}

	/**
	 * @param HttpRequest
	 * @param headers
	 */
	void buildHeader(HttpRequestBase httpRequest, HashMap<String, String> headers) {
		// 设置请求头
		if (null != headers && headers.size() > 0) {
			Set<Entry<String, String>> entrySet = headers.entrySet();
			// 循环遍历，获取迭代器
			Iterator<Entry<String, String>> iterator = entrySet.iterator();
			while (iterator.hasNext()) {
				Entry<String, String> mapEntry = iterator.next();
				log.trace("Name " + mapEntry.getKey() + " , Value " + mapEntry.getValue());
				httpRequest.addHeader(mapEntry.getKey(), mapEntry.getValue());
			}
		}
	}

	UrlEncodedFormEntity buildFormEntity(Map<String, Object> parameterMap) throws UnsupportedEncodingException {
		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		// 通过map集成entrySet方法获取entity
		Set<Entry<String, Object>> entrySet = parameterMap.entrySet();
		// 循环遍历，获取迭代器
		Iterator<Entry<String, Object>> iterator = entrySet.iterator();
		while (iterator.hasNext()) {
			Entry<String, Object> mapEntry = iterator.next();
			log.debug("Name " + mapEntry.getKey() + " , Value " + mapEntry.getValue());
			nvps.add(new BasicNameValuePair(mapEntry.getKey(), mapEntry.getValue().toString()));
		}

		// 为httpPost设置封装好的请求参数
		return new UrlEncodedFormEntity(nvps, "UTF-8");
	}

	void setRequestConfig(HttpRequestBase httpMethod) {
		RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(35000)// 设置连接主机服务超时时间
				.setConnectionRequestTimeout(35000)// 设置连接请求超时时间
				.setSocketTimeout(60000)// 设置读取数据连接超时时间
				.build();
		// 为httpMethod实例设置配置
		httpMethod.setConfig(requestConfig);
	}

	/**
	 * 关闭资源
	 * 
	 * @param httpClient
	 * @param httpResponse
	 */
	void close(CloseableHttpClient httpClient, CloseableHttpResponse httpResponse) {
		if (null != httpResponse) {
			try {
				httpResponse.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		if (null != httpClient) {
			try {
				httpClient.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}