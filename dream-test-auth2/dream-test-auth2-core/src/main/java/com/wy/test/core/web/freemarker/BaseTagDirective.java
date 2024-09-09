package com.wy.test.core.web.freemarker;

import java.io.IOException;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import freemarker.core.Environment;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateDirectiveModel;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModel;

/**
 * 获取应用上下文标签 <@base/>
 *
 * @author 飞花梦影
 * @date 2024-09-09 23:30:45
 * @git {@link https://github.com/dreamFlyingFlower}
 */
@Component
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