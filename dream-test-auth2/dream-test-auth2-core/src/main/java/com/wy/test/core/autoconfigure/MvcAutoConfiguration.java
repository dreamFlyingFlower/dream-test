package com.wy.test.core.autoconfigure;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.servlet.Filter;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.actuate.endpoint.ApiVersion;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.web.server.ConfigurableWebServerFactory;
import org.springframework.boot.web.server.ErrorPage;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.http.converter.xml.MarshallingHttpMessageConverter;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.security.web.servletapi.SecurityContextHolderAwareRequestFilter;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.filter.DelegatingFilterProxy;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.CookieLocaleResolver;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import com.wy.test.core.constants.ConstTimeInterval;
import com.wy.test.core.entity.xml.UserInfoXML;
import com.wy.test.core.persistence.repository.InstitutionsRepository;
import com.wy.test.core.properties.DreamAuthServerProperties;
import com.wy.test.core.web.WebInstRequestFilter;
import com.wy.test.core.web.WebXssRequestFilter;

import lombok.extern.slf4j.Slf4j;

@AutoConfiguration
@Slf4j
public class MvcAutoConfiguration implements InitializingBean, WebMvcConfigurer {

	/**
	 * 消息处理，可以直接使用properties的key值，返回的是对应的value值 messageSource .
	 * 
	 * @return messageSource
	 */
	@Bean(name = "messageSource")
	ReloadableResourceBundleMessageSource reloadableResourceBundleMessageSource(
			@Value("${spring.messages.basename:classpath:messages/message}") String messagesBasename) {
		log.debug("Basename " + messagesBasename);
		String passwordPolicyMessagesBasename = "classpath:messages/passwordpolicy_message";

		ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
		messageSource.setBasenames(messagesBasename, passwordPolicyMessagesBasename);
		messageSource.setUseCodeAsDefaultMessage(false);
		return messageSource;
	}

	/**
	 * Locale Change Interceptor and Resolver definition .
	 * 
	 * @return localeChangeInterceptor
	 */
	// @Primary
	@Bean(name = "localeChangeInterceptor")
	LocaleChangeInterceptor localeChangeInterceptor() {
		LocaleChangeInterceptor localeChangeInterceptor = new LocaleChangeInterceptor();
		localeChangeInterceptor.setParamName("language");
		return localeChangeInterceptor;
	}

	/**
	 * upload file support .
	 * 
	 * @return multipartResolver
	 */
	@Bean(name = "multipartResolver")
	CommonsMultipartResolver
			commonsMultipartResolver(@Value("${spring.servlet.multipart.max-file-size:0}") int maxUploadSize) {
		log.debug("maxUploadSize " + maxUploadSize);
		CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver();
		multipartResolver.setMaxUploadSize(maxUploadSize);
		return multipartResolver;
	}

	/**
	 * handlerMapping .
	 * 
	 * @return handlerMapping
	 */
	@Bean(name = "handlerMapping")
	RequestMappingHandlerMapping requestMappingHandlerMapping(LocaleChangeInterceptor localeChangeInterceptor) {
		RequestMappingHandlerMapping requestMappingHandlerMapping = new RequestMappingHandlerMapping();
		requestMappingHandlerMapping.setInterceptors(localeChangeInterceptor);
		return requestMappingHandlerMapping;
	}

	/**
	 * jaxb2Marshaller .
	 * 
	 * @return jaxb2Marshaller
	 */
	@Bean(name = "jaxb2Marshaller")
	Jaxb2Marshaller jaxb2Marshaller() {
		Jaxb2Marshaller jaxb2Marshaller = new Jaxb2Marshaller();
		jaxb2Marshaller.setClassesToBeBound(UserInfoXML.class);
		return jaxb2Marshaller;
	}

	/**
	 * marshallingHttpMessageConverter .
	 * 
	 * @return marshallingHttpMessageConverter
	 */
	@Bean(name = "marshallingHttpMessageConverter")
	MarshallingHttpMessageConverter marshallingHttpMessageConverter(Jaxb2Marshaller jaxb2Marshaller) {
		MarshallingHttpMessageConverter marshallingHttpMessageConverter = new MarshallingHttpMessageConverter();
		marshallingHttpMessageConverter.setMarshaller(jaxb2Marshaller);
		marshallingHttpMessageConverter.setUnmarshaller(jaxb2Marshaller);
		ArrayList<MediaType> mediaTypesList = new ArrayList<MediaType>();
		mediaTypesList.add(MediaType.APPLICATION_XML);
		mediaTypesList.add(MediaType.TEXT_XML);
		mediaTypesList.add(MediaType.TEXT_PLAIN);
		log.debug("marshallingHttpMessageConverter MediaTypes " + mediaTypesList);
		marshallingHttpMessageConverter.setSupportedMediaTypes(mediaTypesList);
		return marshallingHttpMessageConverter;
	}

	/**
	 * mappingJacksonHttpMessageConverter .
	 * 
	 * @return mappingJacksonHttpMessageConverter
	 */
	@Bean(name = "mappingJacksonHttpMessageConverter")
	MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter() {
		MappingJackson2HttpMessageConverter mappingJacksonHttpMessageConverter =
				new MappingJackson2HttpMessageConverter();
		ArrayList<MediaType> mediaTypesList = new ArrayList<MediaType>();
		mediaTypesList.add(MediaType.APPLICATION_JSON);
		mediaTypesList.add(MediaType.valueOf(ApiVersion.V2.getProducedMimeType().toString()));
		mediaTypesList.add(MediaType.valueOf(ApiVersion.V3.getProducedMimeType().toString()));
		// mediaTypesList.add(MediaType.TEXT_PLAIN);
		log.debug("mappingJacksonHttpMessageConverter MediaTypes " + mediaTypesList);
		mappingJacksonHttpMessageConverter.setSupportedMediaTypes(mediaTypesList);
		return mappingJacksonHttpMessageConverter;
	}

	/**
	 * cookieLocaleResolver .
	 * 
	 * @return cookieLocaleResolver
	 */

	@Bean(name = "cookieLocaleResolver")
	LocaleResolver cookieLocaleResolver(DreamAuthServerProperties dreamAuthServerProperties) {
		log.debug("DomainName " + dreamAuthServerProperties.getDomain());
		CookieLocaleResolver cookieLocaleResolver = new CookieLocaleResolver();
		cookieLocaleResolver.setCookieName("dream_locale");
		cookieLocaleResolver.setCookieDomain(dreamAuthServerProperties.getDomain());
		cookieLocaleResolver.setCookieMaxAge((int) ConstTimeInterval.TWO_WEEK);
		return cookieLocaleResolver;
	}

	/**
	 * AnnotationMethodHandlerAdapter requestMappingHandlerAdapter .
	 * 
	 * @return requestMappingHandlerAdapter
	 */
	@Bean(name = "addConverterRequestMappingHandlerAdapter")
	RequestMappingHandlerAdapter requestMappingHandlerAdapter(
			MappingJackson2HttpMessageConverter mappingJacksonHttpMessageConverter,
			MarshallingHttpMessageConverter marshallingHttpMessageConverter,
			StringHttpMessageConverter stringHttpMessageConverter,
			RequestMappingHandlerAdapter requestMappingHandlerAdapter) {
		List<HttpMessageConverter<?>> httpMessageConverterList = new ArrayList<HttpMessageConverter<?>>();
		httpMessageConverterList.add(mappingJacksonHttpMessageConverter);
		httpMessageConverterList.add(marshallingHttpMessageConverter);
		httpMessageConverterList.add(stringHttpMessageConverter);
		log.debug("stringHttpMessageConverter {}", stringHttpMessageConverter.getDefaultCharset());

		requestMappingHandlerAdapter.setMessageConverters(httpMessageConverterList);
		return requestMappingHandlerAdapter;
	}

	/**
	 * restTemplate .
	 * 
	 * @return restTemplate
	 */
	@Bean(name = "restTemplate")
	RestTemplate restTemplate(MappingJackson2HttpMessageConverter mappingJacksonHttpMessageConverter,
			MarshallingHttpMessageConverter marshallingHttpMessageConverter) {
		RestTemplate restTemplate = new RestTemplate();
		List<HttpMessageConverter<?>> httpMessageConverterList = new ArrayList<HttpMessageConverter<?>>();
		httpMessageConverterList.add(mappingJacksonHttpMessageConverter);
		httpMessageConverterList.add(marshallingHttpMessageConverter);
		restTemplate.setMessageConverters(httpMessageConverterList);
		return restTemplate;
	}

	/**
	 * 配置默认错误页面（仅用于内嵌tomcat启动时） 使用这种方式，在打包为war后不起作用.
	 *
	 * @return webServerFactoryCustomizer
	 */
	@Bean
	WebServerFactoryCustomizer<ConfigurableWebServerFactory> webServerFactoryCustomizer() {
		return new WebServerFactoryCustomizer<ConfigurableWebServerFactory>() {

			@Override
			public void customize(ConfigurableWebServerFactory factory) {
				log.debug("WebServerFactoryCustomizer ... ");
				ErrorPage errorPage400 = new ErrorPage(HttpStatus.BAD_REQUEST, "/exception/error/400");
				ErrorPage errorPage404 = new ErrorPage(HttpStatus.NOT_FOUND, "/exception/error/404");
				ErrorPage errorPage500 = new ErrorPage(HttpStatus.INTERNAL_SERVER_ERROR, "/exception/error/500");
				factory.addErrorPages(errorPage400, errorPage404, errorPage500);
			}
		};
	}

	@Bean
	SecurityContextHolderAwareRequestFilter securityContextHolderAwareRequestFilter() {
		log.debug("securityContextHolderAwareRequestFilter init ");
		return new SecurityContextHolderAwareRequestFilter();
	}

	@Bean
	FilterRegistrationBean<CorsFilter> corsFilter() {
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		CorsConfiguration corsConfiguration = new CorsConfiguration();
		corsConfiguration.setAllowCredentials(true);
		corsConfiguration.setAllowedOriginPatterns(Collections.singletonList(CorsConfiguration.ALL));
		corsConfiguration.addAllowedHeader(CorsConfiguration.ALL);
		corsConfiguration.addAllowedMethod(CorsConfiguration.ALL);
		source.registerCorsConfiguration("/**", corsConfiguration);
		FilterRegistrationBean<CorsFilter> bean = new FilterRegistrationBean<>();
		bean.setOrder(1);
		bean.setFilter(new CorsFilter(source));
		bean.addUrlPatterns("/*");
		return bean;
	}

	@Bean
	FilterRegistrationBean<Filter> delegatingFilterProxy() {
		log.debug("delegatingFilterProxy init for /* ");
		FilterRegistrationBean<Filter> registrationBean = new FilterRegistrationBean<Filter>();
		registrationBean.setFilter(new DelegatingFilterProxy("securityContextHolderAwareRequestFilter"));
		registrationBean.addUrlPatterns("/*");
		// registrationBean.
		registrationBean.setName("delegatingFilterProxy");
		registrationBean.setOrder(2);

		return registrationBean;
	}

	@Bean
	FilterRegistrationBean<Filter> webXssRequestFilter() {
		log.debug("webXssRequestFilter init for /* ");
		FilterRegistrationBean<Filter> registrationBean = new FilterRegistrationBean<Filter>(new WebXssRequestFilter());
		registrationBean.addUrlPatterns("/*");
		registrationBean.setName("webXssRequestFilter");
		registrationBean.setOrder(3);
		return registrationBean;
	}

	@Bean
	FilterRegistrationBean<Filter> WebInstRequestFilter(InstitutionsRepository institutionsRepository,
			DreamAuthServerProperties dreamServerProperties) {
		log.debug("WebInstRequestFilter init for /* ");
		FilterRegistrationBean<Filter> registrationBean = new FilterRegistrationBean<Filter>(
				new WebInstRequestFilter(institutionsRepository, dreamServerProperties));
		registrationBean.addUrlPatterns("/*");
		registrationBean.setName("webInstRequestFilter");
		registrationBean.setOrder(4);
		return registrationBean;
	}

	@Override
	public void afterPropertiesSet() throws Exception {

	}
}