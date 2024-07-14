package com.wy.test.web.tag;

import java.io.IOException;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;

import com.wy.test.web.WebContext;

import freemarker.core.Environment;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateDirectiveModel;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModel;

/**
 * <@basePath/> 获取请求地址及应用上下文标签
 * 
 * @author Crystal.Sea
 *
 */

@FreemarkerTag("basePath")
public class BasePathTagDirective implements TemplateDirectiveModel {

	@Autowired
	private HttpServletRequest request;

	@Override
	@SuppressWarnings("rawtypes")
	public void execute(Environment env, Map params, TemplateModel[] loopVars, TemplateDirectiveBody body)
			throws TemplateException, IOException {

		env.getOut().append(WebContext.getContextPath(request, true));

	}

}
