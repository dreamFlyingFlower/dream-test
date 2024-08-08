package com.wy.test.core.web;

import java.io.IOException;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import com.wy.test.core.web.tag.FreemarkerTag;

import freemarker.template.Configuration;
import freemarker.template.TemplateException;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class ConfigurerFreeMarker implements ApplicationContextAware {

	ApplicationContext applicationContext;

	@Autowired
	Configuration configuration;

	@PostConstruct // 在项目启动时执行方法
	public void setSharedVariable() throws IOException, TemplateException {
		// 根据注解FreemarkerTag获取bean ,key is bean name ,value is bean object
		Map<String, Object> map = this.applicationContext.getBeansWithAnnotation(FreemarkerTag.class);
		for (String key : map.keySet()) {
			configuration.setSharedVariable(key, map.get(key));
			log.trace("FreeMarker Template " + key);
		}

	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}
}