package com.wy.test.web.core.config;

import javax.servlet.DispatcherType;

import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

/**
 * 
 *
 * @author 飞花梦影
 * @date 2024-09-29 16:34:39
 * @git {@link https://github.com/dreamFlyingFlower}
 */
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true, securedEnabled = true)
public class SecurityConfig {

	@Bean
	SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
		httpSecurity
				// 设置在页面可以通过iframe访问受保护的页面,默认为不允许访问
				.headers(header -> header.frameOptions(frame -> frame.sameOrigin()))
				.authorizeHttpRequests(authorize -> authorize
						// 默认情况下,Spring Security授权所有调度程序类型,即使请求转发上建立的安全上下文会延续到后续dispatch中,
						// 但细微的不匹配有时会导致意外的AccessDeniedException,如下可避免该问题
						// FORWARD主要用于内置页面,当跳转到内置页面时,一次授权通过Controller跳转到指定方法,一次是渲染内置页面
						.dispatcherTypeMatchers(DispatcherType.FORWARD, DispatcherType.ERROR)
						.permitAll()
						// 所有匹配的url请求不需要验证
						.requestMatchers("/**")
						.permitAll())
				.csrf(csrf -> csrf.disable());

		return httpSecurity.build();
	}
}