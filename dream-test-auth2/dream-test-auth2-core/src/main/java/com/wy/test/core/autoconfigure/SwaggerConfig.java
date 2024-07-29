package com.wy.test.core.autoconfigure;

import java.util.HashMap;
import java.util.Map;

import org.springdoc.core.GroupedOpenApi;
import org.springdoc.core.customizers.GlobalOpenApiCustomizer;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Bean;

import com.wy.test.core.properties.DreamSwaggerProperties;

import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import lombok.AllArgsConstructor;

@AutoConfiguration
@AllArgsConstructor
public class SwaggerConfig {

	private final DreamSwaggerProperties dreamSwaggerProperties;

	@Bean
	GlobalOpenApiCustomizer orderGlobalOpenApiCustomizer() {
		return openApi -> {
			if (openApi.getTags() != null) {
				openApi.getTags().forEach(tag -> {
					Map<String, Object> map = new HashMap<>();
					map.put("x-order", 1);
					tag.setExtensions(map);
				});
			}
			if (openApi.getPaths() != null) {
				openApi.addExtension("x-test123", "333");
				openApi.getPaths().addExtension("x-abb", 1);
			}
		};
	}

	@Bean
	GroupedOpenApi userApi() {
		String[] paths = { "/login", "/logout", "/login/**", "/logout/**", "/authz/**", "/authz/**/**",
				"/metadata/saml20/**", "/onlineticket/validate/**", "/api/connect/v10/userinfo", "/api/oauth/v20/me"

		};
		String[] packagedToMatch = { "org.dream.authz" };
		return GroupedOpenApi.builder().group(dreamSwaggerProperties.getTitle()).pathsToMatch(paths)
				.packagesToScan(packagedToMatch).build();
	}

	@Bean
	OpenAPI docOpenAPI() {
		return new OpenAPI()
				.info(new Info().title(dreamSwaggerProperties.getTitle())
						.description(dreamSwaggerProperties.getDescription())
						.version(dreamSwaggerProperties.getVersion())
						.termsOfService("http://www.gitee.com/dreamFlyingFlower")
						.license(new License().name("Apache License, Version 2.0")
								.url("http://www.apache.org/licenses/LICENSE-2.0")))
				.externalDocs(new ExternalDocumentation().description("SSO contact 582822832@qq.com")
						.url("http://www.gitee.com/dreamFlyingFlower"));
	}
}