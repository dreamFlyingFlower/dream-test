package com.wy.test.web.mgt.endpoint;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import com.wy.test.core.web.AuthWebContext;

import lombok.extern.slf4j.Slf4j;

/**
 * 首页
 *
 * @author 飞花梦影
 * @date 2024-08-08 11:49:53
 * @git {@link https://github.com/dreamFlyingFlower}
 */
@Controller
@Slf4j
public class IndexEndpoint {

	@GetMapping(value = { "/" })
	public ModelAndView index() {
		log.debug("IndexEndpoint /.");
		return new ModelAndView("index").addObject("appVersion",
				AuthWebContext.properties.getProperty("dream.auth.app.version"));
	}
}