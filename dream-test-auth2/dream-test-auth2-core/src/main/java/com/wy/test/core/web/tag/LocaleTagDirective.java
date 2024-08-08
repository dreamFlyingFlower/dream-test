package com.wy.test.core.web.tag;

import java.io.IOException;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.support.RequestContextUtils;

import com.wy.test.core.persistence.repository.LocalizationRepository;
import com.wy.test.core.web.WebContext;

import freemarker.core.Environment;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateDirectiveModel;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModel;
import lombok.extern.slf4j.Slf4j;

/**
 * 获取应用上下文标签 .<@locale/>
 */
@FreemarkerTag("locale")
@Slf4j
public class LocaleTagDirective implements TemplateDirectiveModel {

	@Autowired
	private HttpServletRequest request;

	@Autowired
	LocalizationRepository localizationService;

	@SuppressWarnings("rawtypes")
	@Override
	public void execute(Environment env, Map params, TemplateModel[] loopVars, TemplateDirectiveBody body)
			throws TemplateException, IOException {
		WebApplicationContext webApplicationContext = RequestContextUtils.findWebApplicationContext(request);
		String message = "";
		String code = params.get("code") == null ? null : params.get("code").toString();
		String htmlTag = params.get("htmltag") == null ? null : params.get("htmltag").toString();
		log.trace("message code {} , htmltag {}", code, htmlTag);

		if (code == null) {
			message = RequestContextUtils.getLocale(request).getLanguage();
		} else if (code.equals("global.application.version") || code.equals("application.version")) {
			message = WebContext.properties.getProperty("application.formatted-version");
		} else if (code.equals("global.logo")) {

			if (!message.startsWith("http")) {
				message = request.getContextPath() + message;
			}
		} else if (code.equals("global.title") || code.equals("global.consoleTitle")) {

		} else {
			try {
				message = webApplicationContext.getMessage(code, null, RequestContextUtils.getLocale(request));
			} catch (Exception e) {
				log.error("message code " + code, e);
			}
		}
		env.getOut().append(message);
	}
}