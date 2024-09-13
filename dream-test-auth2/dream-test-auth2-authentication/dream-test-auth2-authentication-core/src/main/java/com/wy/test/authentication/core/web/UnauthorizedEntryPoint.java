package com.wy.test.authentication.core.web;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.fasterxml.jackson.core.exc.StreamWriteException;
import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

/**
 * 未登录处理方法
 *
 * @author 飞花梦影
 * @date 2024-09-10 22:51:47
 * @git {@link https://github.com/dreamFlyingFlower}
 */
@Controller
@RequestMapping(value = "/auth")
@Slf4j
public class UnauthorizedEntryPoint {

	@GetMapping(value = { "/entrypoint" })
	public void entryPoint(HttpServletRequest request, HttpServletResponse response)
			throws StreamWriteException, DatabindException, IOException {
		log.trace("UnauthorizedEntryPoint /entrypoint.");
		response.setContentType(MediaType.APPLICATION_JSON_VALUE);
		response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

		final Map<String, Object> responseBody = new HashMap<>();
		responseBody.put("status", HttpServletResponse.SC_UNAUTHORIZED);
		responseBody.put("error", "Unauthorized");
		responseBody.put("message", "Unauthorized");
		responseBody.put("path", request.getServletPath());

		final ObjectMapper mapper = new ObjectMapper();
		mapper.writeValue(response.getOutputStream(), responseBody);
	}
}