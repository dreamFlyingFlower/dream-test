package com.wy.test.web.core.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import com.wy.test.core.web.AuthWebContext;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;

/**
 * 首页
 *
 * @author 飞花梦影
 * @date 2024-08-08 11:46:23
 * @git {@link https://github.com/dreamFlyingFlower}
 */
@Tag(name = "管理系统首页API")
@Controller
@Slf4j
public class IndexEndpoint {

	@Operation(summary = "首页", method = "GET")
	@GetMapping(value = { "/" })
	public ModelAndView index() {
		log.debug("IndexEndpoint /.");
		return new ModelAndView("index").addObject("appVersion",
				AuthWebContext.properties.getProperty("dream.auth.app.version"));
	}
}