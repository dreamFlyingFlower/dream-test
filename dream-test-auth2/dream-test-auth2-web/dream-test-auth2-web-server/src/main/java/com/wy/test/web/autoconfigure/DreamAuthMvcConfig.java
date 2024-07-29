package com.wy.test.web.autoconfigure;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.wy.test.core.authn.web.CurrentUserMethodArgumentResolver;
import com.wy.test.core.authn.web.interceptor.PermissionInterceptor;
import com.wy.test.core.configuration.ApplicationConfig;
import com.wy.test.provider.authn.provider.AbstractAuthenticationProvider;
import com.wy.test.provider.authn.support.basic.BasicEntryPoint;
import com.wy.test.provider.authn.support.httpheader.HttpHeaderEntryPoint;
import com.wy.test.provider.authn.support.kerberos.HttpKerberosEntryPoint;
import com.wy.test.provider.authn.support.kerberos.KerberosService;
import com.wy.test.web.web.interceptor.HistorySignOnAppInterceptor;
import com.wy.test.web.web.interceptor.SingleSignOnInterceptor;

@EnableWebMvc
@AutoConfiguration
public class DreamAuthMvcConfig implements WebMvcConfigurer {

	private static final Logger _logger = LoggerFactory.getLogger(DreamAuthMvcConfig.class);

	@Value("${dream.login.basic.enable:false}")
	private boolean basicEnable;

	@Value("${dream.login.httpheader.enable:false}")
	private boolean httpHeaderEnable;

	@Value("${dream.login.httpheader.headername:iv-user}")
	private String httpHeaderName;

	@Autowired
	ApplicationConfig applicationConfig;

	@Autowired
	AbstractAuthenticationProvider authenticationProvider;

	@Autowired
	KerberosService kerberosService;

	@Autowired
	PermissionInterceptor permissionInterceptor;

	@Autowired
	SingleSignOnInterceptor singleSignOnInterceptor;

	@Autowired
	HistorySignOnAppInterceptor historySignOnAppInterceptor;

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		_logger.debug("add Resource Handlers");
		_logger.debug("add statics");
		registry.addResourceHandler("/static/**").addResourceLocations("classpath:/static/");
		_logger.debug("add templates");
		registry.addResourceHandler("/templates/**").addResourceLocations("classpath:/templates/");

		_logger.debug("add knife4j");
		registry.addResourceHandler("doc.html").addResourceLocations("classpath:/META-INF/resources/");

		_logger.debug("add swagger");
		registry.addResourceHandler("swagger-ui.html").addResourceLocations("classpath:/META-INF/resources/");
		registry.addResourceHandler("/webjars/**").addResourceLocations("classpath:/META-INF/resources/webjars/");

		_logger.debug("add Resource Handler finished .");
	}

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		// addPathPatterns 用于添加拦截规则 ， 先把所有路径都加入拦截， 再一个个排除
		// excludePathPatterns 表示改路径不用拦截

		_logger.debug("add Http Kerberos Entry Point");
		registry.addInterceptor(
				new HttpKerberosEntryPoint(authenticationProvider, kerberosService, applicationConfig, true))
				.addPathPatterns("/login");

		if (httpHeaderEnable) {
			registry.addInterceptor(new HttpHeaderEntryPoint(httpHeaderName, httpHeaderEnable)).addPathPatterns("/*");
			_logger.debug("add Http Header Entry Point");
		}

		if (basicEnable) {
			registry.addInterceptor(new BasicEntryPoint(basicEnable)).addPathPatterns("/*");
			_logger.debug("add Basic Entry Point");
		}

		// for frontend
		registry.addInterceptor(permissionInterceptor).addPathPatterns("/config/**").addPathPatterns("/historys/**")
				.addPathPatterns("/access/session/**").addPathPatterns("/access/session/**/**")
				.addPathPatterns("/appList").addPathPatterns("/appList/**").addPathPatterns("/socialsignon/**")
				.addPathPatterns("/authz/credential/**").addPathPatterns("/authz/oauth/v20/approval_confirm/**")
				.addPathPatterns("/authz/oauth/v20/authorize/approval/**").addPathPatterns("/logon/oauth20/bind/**")
				.addPathPatterns("/logout").addPathPatterns("/logout/**").addPathPatterns("/authz/refused");

		_logger.debug("add Permission Interceptor");

		// for Single Sign On
		registry.addInterceptor(singleSignOnInterceptor).addPathPatterns("/authz/basic/*")
				// Form based
				.addPathPatterns("/authz/formbased/*")
				// Token based
				.addPathPatterns("/authz/tokenbased/*")
				// JWT
				.addPathPatterns("/authz/jwt/*")
				// SAML
				.addPathPatterns("/authz/saml20/idpinit/*").addPathPatterns("/authz/saml20/assertion")
				.addPathPatterns("/authz/saml20/assertion/")
				// CAS
				.addPathPatterns("/authz/cas/*").addPathPatterns("/authz/cas/*/*").addPathPatterns("/authz/cas/login")
				.addPathPatterns("/authz/cas/login/").addPathPatterns("/authz/cas/granting/*")
				// cas1.0 validate
				.excludePathPatterns("/authz/cas/validate")
				// cas2.0 Validate
				.excludePathPatterns("/authz/cas/serviceValidate").excludePathPatterns("/authz/cas/proxyValidate")
				.excludePathPatterns("/authz/cas/proxy")
				// cas3.0 Validate
				.excludePathPatterns("/authz/cas/p3/serviceValidate").excludePathPatterns("/authz/cas/p3/proxyValidate")
				.excludePathPatterns("/authz/cas/p3/proxy")
				// rest
				.excludePathPatterns("/authz/cas/v1/tickets").excludePathPatterns("/authz/cas/v1/tickets/*")

				// OAuth
				.addPathPatterns("/authz/oauth/v20/authorize").addPathPatterns("/authz/oauth/v20/authorize/*")

				// OAuth TENCENT_IOA
				.addPathPatterns("/oauth2/authorize").addPathPatterns("/oauth2/authorize/*")

				// online ticket Validate
				.excludePathPatterns("/onlineticket/ticketValidate")
				.excludePathPatterns("/onlineticket/ticketValidate/*");
		_logger.debug("add Single SignOn Interceptor");

		registry.addInterceptor(historySignOnAppInterceptor).addPathPatterns("/authz/basic/*")
				.addPathPatterns("/authz/ltpa/*")
				// Extend api
				.addPathPatterns("/authz/api/*")
				// Form based
				.addPathPatterns("/authz/formbased/*")
				// Token based
				.addPathPatterns("/authz/tokenbased/*")
				// JWT
				.addPathPatterns("/authz/jwt/*")
				// SAML
				.addPathPatterns("/authz/saml20/idpinit/*").addPathPatterns("/authz/saml20/assertion")
				// CAS
				.addPathPatterns("/authz/cas/granting")
				// OAuth
				.addPathPatterns("/authz/oauth/v20/approval_confirm");
		_logger.debug("add history SignOn App Interceptor");

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