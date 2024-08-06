package com.wy.test.mgt.web.contorller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import com.wy.test.core.web.WebContext;

/**
 * Index
 */
@Controller
public class IndexEndpoint {

	private static Logger _logger = LoggerFactory.getLogger(IndexEndpoint.class);

	@GetMapping(value = { "/" })
	public ModelAndView index() {
		_logger.debug("IndexEndpoint /.");
		return new ModelAndView("index").addObject("appVersion",
				WebContext.properties.getProperty("application.formatted-version"));

	}
}