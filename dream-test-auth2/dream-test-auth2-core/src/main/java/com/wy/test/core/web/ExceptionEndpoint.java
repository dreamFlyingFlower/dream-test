package com.wy.test.core.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * Exception.
 *
 */
@Controller
public class ExceptionEndpoint {

	private static Logger _logger = LoggerFactory.getLogger(ExceptionEndpoint.class);

	@GetMapping(value = { "/exception/error/400" })
	public ModelAndView error400(HttpServletRequest request, HttpServletResponse response) {
		_logger.debug("Exception BAD_REQUEST");
		return new ModelAndView("exception/400");
	}

	/**
	 * //查看浏览器中的报错信息.
	 * 
	 * @param request HttpServletRequest
	 * @param response HttpServletResponse
	 * @return
	 */
	@GetMapping(value = { "/exception/error/404" })
	public ModelAndView error404(HttpServletRequest request, HttpServletResponse response) {
		_logger.debug("Exception PAGE NOT_FOUND ");
		return new ModelAndView("exception/404");
	}

	@GetMapping(value = { "/exception/error/500" })
	public ModelAndView error500(HttpServletRequest request, HttpServletResponse response) {
		_logger.debug("Exception INTERNAL_SERVER_ERROR ");
		return new ModelAndView("exception/500");
	}

	@GetMapping(value = { "/exception/accessdeny" })
	public ModelAndView accessdeny(HttpServletRequest request, HttpServletResponse response) {
		_logger.debug("exception/accessdeny ");
		return new ModelAndView("exception/accessdeny");
	}
}
