package com.wy.test.web.core.autoconfigure;

import java.util.List;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.wy.test.authentication.core.authn.web.CurrentUserMethodArgumentResolver;
import com.wy.test.authentication.core.authn.web.interceptor.PermissionInterceptor;
import com.wy.test.authentication.provider.authn.provider.AbstractAuthenticationProvider;
import com.wy.test.authentication.provider.authn.support.basic.BasicEntryPoint;
import com.wy.test.authentication.provider.authn.support.httpheader.HttpHeaderEntryPoint;
import com.wy.test.authentication.provider.authn.support.kerberos.HttpKerberosEntryPoint;
import com.wy.test.authentication.provider.authn.support.kerberos.KerberosService;
import com.wy.test.core.properties.DreamAuthLoginProperties;
import com.wy.test.core.properties.DreamAuthServerProperties;
import com.wy.test.web.core.interceptor.HistorySignOnAppInterceptor;
import com.wy.test.web.core.interceptor.SingleSignOnInterceptor;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@EnableWebMvc
@AutoConfiguration
@AllArgsConstructor
@Slf4j
public class DreamAuthMvcConfig implements WebMvcConfigurer {

	DreamAuthServerProperties dreamServerProperties;

	DreamAuthLoginProperties dreamAuthLoginProperties;

	AbstractAuthenticationProvider authenticationProvider;

	KerberosService kerberosService;

	PermissionInterceptor permissionInterceptor;

	SingleSignOnInterceptor singleSignOnInterceptor;

	HistorySignOnAppInterceptor historySignOnAppInterceptor;

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		log.debug("add Resource Handlers");
		log.debug("add statics");
		registry.addResourceHandler("/static/**").addResourceLocations("classpath:/static/");
		log.debug("add templates");
		registry.addResourceHandler("/templates/**").addResourceLocations("classpath:/templates/");

		log.debug("add knife4j");
		registry.addResourceHandler("doc.html").addResourceLocations("classpath:/META-INF/resources/");

		log.debug("add swagger");
		registry.addResourceHandler("swagger-ui.html").addResourceLocations("classpath:/META-INF/resources/");
		registry.addResourceHandler("/webjars/**").addResourceLocations("classpath:/META-INF/resources/webjars/");

		log.debug("add Resource Handler finished .");
	}

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		// addPathPatterns 用于添加拦截规则 ， 先把所有路径都加入拦截， 再一个个排除
		// excludePathPatterns 表示改路径不用拦截

		log.debug("add Http Kerberos Entry Point");
		registry.addInterceptor(
				new HttpKerberosEntryPoint(authenticationProvider, kerberosService, dreamServerProperties, true))
				.addPathPatterns("/login");

		if (dreamAuthLoginProperties.getHttpHeader().isEnabled()) {
			registry.addInterceptor(new HttpHeaderEntryPoint(dreamAuthLoginProperties.getHttpHeader().getHeaderName(),
					dreamAuthLoginProperties.getHttpHeader().isEnabled())).addPathPatterns("/*");
			log.debug("add Http Header Entry Point");
		}

		if (dreamAuthLoginProperties.getBasic().isEnabled()) {
			registry.addInterceptor(new BasicEntryPoint(dreamAuthLoginProperties.getBasic().isEnabled()))
					.addPathPatterns("/*");
			log.debug("add Basic Entry Point");
		}

		// for frontend
		registry.addInterceptor(permissionInterceptor).addPathPatterns("/config/**").addPathPatterns("/historys/**")
				.addPathPatterns("/access/session/**").addPathPatterns("/access/session/**/**")
				.addPathPatterns("/appList").addPathPatterns("/appList/**").addPathPatterns("/socialsignon/**")
				.addPathPatterns("/authz/credential/**").addPathPatterns("/authz/oauth/v20/approval_confirm/**")
				.addPathPatterns("/authz/oauth/v20/authorize/approval/**").addPathPatterns("/logon/oauth20/bind/**")
				.addPathPatterns("/logout").addPathPatterns("/logout/**").addPathPatterns("/authz/refused");

		log.debug("add Permission Interceptor");

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
		log.debug("add Single SignOn Interceptor");

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
		log.debug("add history SignOn App Interceptor");

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