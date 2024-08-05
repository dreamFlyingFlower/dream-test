package com.wy.test.web.apis.identity.rest;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import com.wy.test.core.convert.UserConvert;
import com.wy.test.core.entity.ChangePassword;
import com.wy.test.core.entity.Message;
import com.wy.test.core.entity.UserEntity;
import com.wy.test.core.query.UserQuery;
import com.wy.test.persistence.service.UserService;

import dream.flying.flower.lang.StrHelper;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping(value = { "/api/idm/Users" })
@Slf4j
public class RestUserInfoController {

	@Autowired
	@Qualifier("userService")
	private UserService userService;

	@Autowired
	private UserConvert userConvert;

	@GetMapping(value = "/{id}")
	public UserEntity getUser(@PathVariable String id, @RequestParam(required = false) String attributes) {
		log.debug("UserInfo id {} , attributes {}", id, attributes);
		UserEntity loadUserInfo = userService.getById(id);
		loadUserInfo.setDecipherable(null);
		return loadUserInfo;
	}

	@PostMapping
	public UserEntity create(@RequestBody UserEntity userInfo, @RequestParam(required = false) String attributes,
			UriComponentsBuilder builder) throws IOException {
		log.debug("UserInfo content {} , attributes {}", userInfo, attributes);
		UserEntity loadUserInfo = userService.findByUsername(userInfo.getUsername());
		if (loadUserInfo != null) {
			userService.update(userInfo);
		} else {
			userService.insert(userInfo);
		}
		return userInfo;
	}

	@PostMapping(value = "/changePassword")
	public String changePassword(@RequestParam(required = true) String username,
			@RequestParam(required = true) String password, UriComponentsBuilder builder) throws IOException {
		log.debug("UserInfo username {} , password {}", username, password);
		UserEntity loadUserInfo = userService.findByUsername(username);
		if (loadUserInfo != null) {
			ChangePassword changePassword = new ChangePassword(userConvert.convertt(loadUserInfo));
			changePassword.setPassword(password);
			changePassword.setDecipherable(loadUserInfo.getDecipherable());
			userService.changePassword(changePassword, true);
		}
		return "true";
	}

	@PutMapping(value = "/{id}")
	public UserEntity replace(@PathVariable String id, @RequestBody UserEntity userInfo,
			@RequestParam(required = false) String attributes) throws IOException {
		log.debug("UserInfo content {} , attributes {}", userInfo, attributes);
		UserEntity loadUserInfo = userService.findByUsername(userInfo.getUsername());
		if (loadUserInfo != null) {
			userService.update(userInfo);
		} else {
			userService.insert(userInfo);
		}
		return userInfo;
	}

	@DeleteMapping(value = "/{id}")
	@ResponseStatus(HttpStatus.OK)
	public void delete(@PathVariable final String id) {
		log.debug("UserInfo id {} ", id);
		userService.removeById(id);
	}

	@GetMapping(value = { "/.search" }, produces = { MediaType.APPLICATION_JSON_VALUE })
	@ResponseBody
	public ResponseEntity<?> search(@ModelAttribute UserQuery userInfo) {
		if (StrHelper.isBlank(userInfo.getInstId())) {
			userInfo.setInstId("1");
		}
		return new Message<>(userService.listPage(userInfo)).buildResponse();
	}
}