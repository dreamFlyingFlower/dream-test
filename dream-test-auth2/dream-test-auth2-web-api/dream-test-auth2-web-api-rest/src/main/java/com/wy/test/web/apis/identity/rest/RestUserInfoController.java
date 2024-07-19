package com.wy.test.web.apis.identity.rest;

import java.io.IOException;

import org.dromara.mybatis.jpa.entity.JpaPageResults;
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

import com.wy.test.core.entity.ChangePassword;
import com.wy.test.core.entity.UserInfo;
import com.wy.test.entity.Message;
import com.wy.test.persistence.service.UserInfoService;

import dream.flying.flower.lang.StrHelper;

@RestController
@RequestMapping(value = { "/api/idm/Users" })
public class RestUserInfoController {

	@Autowired
	@Qualifier("userInfoService")
	private UserInfoService userInfoService;

	@GetMapping(value = "/{id}")
	public UserInfo getUser(@PathVariable String id, @RequestParam(required = false) String attributes) {

		UserInfo loadUserInfo = userInfoService.get(id);
		loadUserInfo.setDecipherable(null);
		return loadUserInfo;
	}

	@PostMapping
	public UserInfo create(@RequestBody UserInfo userInfo, @RequestParam(required = false) String attributes,
			UriComponentsBuilder builder) throws IOException {
		UserInfo loadUserInfo = userInfoService.findByUsername(userInfo.getUsername());
		if (loadUserInfo != null) {
			userInfoService.update(userInfo);
		} else {
			userInfoService.insert(userInfo);
		}
		return userInfo;
	}

	@PostMapping(value = "/changePassword")
	public String changePassword(@RequestParam(required = true) String username,
			@RequestParam(required = true) String password, UriComponentsBuilder builder) throws IOException {
		UserInfo loadUserInfo = userInfoService.findByUsername(username);
		if (loadUserInfo != null) {
			ChangePassword changePassword = new ChangePassword(loadUserInfo);
			changePassword.setPassword(password);
			changePassword.setDecipherable(loadUserInfo.getDecipherable());
			userInfoService.changePassword(changePassword, true);
		}
		return "true";
	}

	@PutMapping(value = "/{id}")
	public UserInfo replace(@PathVariable String id, @RequestBody UserInfo userInfo,
			@RequestParam(required = false) String attributes) throws IOException {
		UserInfo loadUserInfo = userInfoService.findByUsername(userInfo.getUsername());
		if (loadUserInfo != null) {
			userInfoService.update(userInfo);
		} else {
			userInfoService.insert(userInfo);
		}
		return userInfo;
	}

	@DeleteMapping(value = "/{id}")
	@ResponseStatus(HttpStatus.OK)
	public void delete(@PathVariable final String id) {
		userInfoService.logicDelete(id);
	}

	@GetMapping(value = { "/.search" }, produces = { MediaType.APPLICATION_JSON_VALUE })
	@ResponseBody
	public ResponseEntity<?> search(@ModelAttribute UserInfo userInfo) {
		if (StrHelper.isBlank(userInfo.getInstId())) {
			userInfo.setInstId("1");
		}
		return new Message<JpaPageResults<UserInfo>>(userInfoService.fetchPageResults(userInfo)).buildResponse();
	}
}