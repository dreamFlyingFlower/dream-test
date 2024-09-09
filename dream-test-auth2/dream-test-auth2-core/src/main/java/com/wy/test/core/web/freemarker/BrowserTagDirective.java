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
 * 获取应用上下文标签 <@browser name=""></@browser>
 *
 * @author 飞花梦影
 * @date 2024-09-09 23:32:23
 * @git {@link https://github.com/dreamFlyingFlower}
 */
@Component
@FreemarkerTag("browser")
public class BrowserTagDirective implements TemplateDirectiveModel {

	@Autowired
	private HttpServletRequest request;

	@Override
	@SuppressWarnings("rawtypes")
	public void execute(Environment env, Map params, TemplateModel[] loopVars, TemplateDirectiveBody body)
			throws TemplateException, IOException {
		String browser = params.get("name").toString();
		String userAgent = request.getHeader("User-Agent");
		env.getOut().append("<!--<div style='display:none'>" + userAgent + "</div>-->");

		if (userAgent.indexOf(browser) > 0) {
			body.render(env.getOut());
		}
	}
}