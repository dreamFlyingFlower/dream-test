package com.wy.test.openapi.autoconfigure;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import com.wy.test.core.properties.DreamServerProperties;
import com.wy.test.openapi.web.interceptor.RestApiPermissionAdapter;
import com.wy.test.provider.authn.provider.AbstractAuthenticationProvider;

@EnableWebMvc
@AutoConfiguration
public class DreamOpenApiMvcConfig implements WebMvcConfigurer {

	private static final Logger _logger = LoggerFactory.getLogger(DreamOpenApiMvcConfig.class);

	@Autowired
	DreamServerProperties dreamServerProperties;

	@Autowired
	AbstractAuthenticationProvider authenticationProvider;

	@Autowired
	PermissionInterceptor permissionInterceptor;

	@Autowired
	RestApiPermissionAdapter restApiPermissionAdapter;

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		_logger.debug("add Resource Handlers");

		_logger.debug("add statics");
		registry.addResourceHandler("/static/**").addResourceLocations("classpath:/static/");
		_logger.debug("add templates");
		registry.addResourceHandler("/templates/**").addResourceLocations("classpath:/templates/");

		_logger.debug("add swagger");
		registry.addResourceHandler("swagger-ui.html").addResourceLocations("classpath:/META-INF/resources/");
		registry.addResourceHandler("/webjars/**").addResourceLocations("classpath:/META-INF/resources/webjars/");

		_logger.debug("add knife4j");
		registry.addResourceHandler("doc.html").addResourceLocations("classpath:/META-INF/resources/");
		registry.addResourceHandler("/webjars/**").addResourceLocations("classpath:/META-INF/resources/webjars/");

		_logger.debug("add Resource Handler finished .");
	}

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		// addPathPatterns 用于添加拦截规则 ， 先把所有路径都加入拦截， 再一个个排除
		// excludePathPatterns 表示改路径不用拦截
		_logger.debug("add Interceptors");

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

		_logger.debug("add Permission Adapter");

		/*
		 * api idm scim
		 */
		registry.addInterceptor(restApiPermissionAdapter).addPathPatterns("/api/**").addPathPatterns("/api/idm/**")
				.addPathPatterns("/api/idm/scim/**");

		_logger.debug("add Rest Api Permission Adapter");

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
