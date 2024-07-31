package com.wy.test.autoconfigure;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.wy.test.core.authn.web.CurrentUserMethodArgumentResolver;
import com.wy.test.core.authn.web.interceptor.PermissionInterceptor;
import com.wy.test.provider.authn.provider.AbstractAuthenticationProvider;

import lombok.extern.slf4j.Slf4j;

@EnableWebMvc
@AutoConfiguration
@Slf4j
public class DreamMgtMvcConfig implements WebMvcConfigurer {

	@Autowired
	AbstractAuthenticationProvider authenticationProvider;

	@Autowired
	PermissionInterceptor permissionInterceptor;

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		log.debug("add Resource Handlers");

		log.debug("add statics");
		registry.addResourceHandler("/static/**").addResourceLocations("classpath:/static/");
		log.debug("add templates");
		registry.addResourceHandler("/templates/**").addResourceLocations("classpath:/templates/");

		log.debug("add swagger");
		registry.addResourceHandler("swagger-ui.html").addResourceLocations("classpath:/META-INF/resources/");
		registry.addResourceHandler("/webjars/**").addResourceLocations("classpath:/META-INF/resources/webjars/");

		log.debug("add knife4j");
		registry.addResourceHandler("doc.html").addResourceLocations("classpath:/META-INF/resources/");
		registry.addResourceHandler("/webjars/**").addResourceLocations("classpath:/META-INF/resources/webjars/");

		log.debug("add Resource Handler finished .");
	}

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		// addPathPatterns 用于添加拦截规则 ， 先把所有路径都加入拦截， 再一个个排除
		// excludePathPatterns 表示改路径不用拦截
		log.debug("add Interceptors");

		permissionInterceptor.setMgmt(true);

		registry.addInterceptor(permissionInterceptor).addPathPatterns("/dashboard/**").addPathPatterns("/orgs/**")
				.addPathPatterns("/users/**").addPathPatterns("/apps/**").addPathPatterns("/session/**")
				.addPathPatterns("/accounts/**")

				.addPathPatterns("/access/**").addPathPatterns("/access/**/**")

				.addPathPatterns("/permissions/**").addPathPatterns("/permissions/**/**")

				.addPathPatterns("/config/**").addPathPatterns("/config/**/**")

				.addPathPatterns("/historys/**").addPathPatterns("/historys/**/**")

				.addPathPatterns("/institutions/**").addPathPatterns("/localization/**")

				.addPathPatterns("/file/upload/")

				.addPathPatterns("/logout").addPathPatterns("/logout/**");

		log.debug("add Permission Adapter");

	}

	@Override
	public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
		argumentResolvers.add(currentUserMethodArgumentResolver());
	}

	@Bean
	CurrentUserMethodArgumentResolver currentUserMethodArgumentResolver() {
		return new CurrentUserMethodArgumentResolver();
	}
}