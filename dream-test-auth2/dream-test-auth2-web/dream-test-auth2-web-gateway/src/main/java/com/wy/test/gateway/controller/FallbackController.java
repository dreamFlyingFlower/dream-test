package com.wy.test.gateway.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FallbackController {

	@GetMapping("/defaultFallback")
	public Map<String, Object> defaultFallback() {
		Map<String, Object> map = new HashMap<>();
		map.put("code", 1);
		map.put("message", "服务异常");
		return map;
	}
}
