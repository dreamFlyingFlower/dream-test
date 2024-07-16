package com.wy.test.web.tag;

import java.io.IOException;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;

import freemarker.core.Environment;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateDirectiveModel;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModel;

/**
 * 获取应用上下文标签 <@base/>
 */
@FreemarkerTag("base")
public class BaseTagDirective implements TemplateDirectiveModel {

	@Autowired
	private HttpServletRequest request;

	@Override
	@SuppressWarnings("rawtypes")
	public void execute(Environment env, Map params, TemplateModel[] loopVars, TemplateDirectiveBody body)
			throws TemplateException, IOException {
		// String url = params.get(URL).toString();

		String base = request.getContextPath();

		env.getOut().append(base);

	}

}
