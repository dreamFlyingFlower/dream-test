package com.wy.test.core.web.freemarker;

import java.io.IOException;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.wy.test.core.web.WebContext;

import freemarker.core.Environment;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateDirectiveModel;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModel;

/**
 * <@basePath/> 获取请求地址及应用上下文标签
 *
 * @author 飞花梦影
 * @date 2024-09-09 23:30:22
 * @git {@link https://github.com/dreamFlyingFlower}
 */
@Component
@FreemarkerTag("basePath")
public class BasePathTagDirective implements TemplateDirectiveModel {

	@Autowired
	private HttpServletRequest request;

	/**
	 * 自定义标签
	 * 
	 * @param env 环境变量,通过env.getOut拿到Write
	 * @param params 可以得到参数
	 * @param loopVars 所有数据类型的顶级接口,通过loopVars[i]的i来返回指定的第几个参数的返回值
	 * @param body 标签开始和结束的内容,通过对象的render()可以接着执行自定义标签里的其他标签
	 * @throws TemplateException
	 * @throws IOException
	 */
	@Override
	@SuppressWarnings("rawtypes")
	public void execute(Environment env, Map params, TemplateModel[] loopVars, TemplateDirectiveBody body)
			throws TemplateException, IOException {

		env.getOut().append(WebContext.getContextPath(request, true));
	}
}