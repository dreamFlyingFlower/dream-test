package com.wy.test.web.mgt.contorller;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.wy.test.authentication.core.authn.annotation.CurrentUser;
import com.wy.test.core.entity.Message;
import com.wy.test.core.entity.UserAdjunctEntity;
import com.wy.test.core.entity.UserEntity;
import com.wy.test.core.query.UserAdjunctQuery;
import com.wy.test.persistence.service.UserAdjunctService;

import dream.flying.flower.result.Result;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import lombok.extern.slf4j.Slf4j;

@Controller
@RequestMapping(value = { "/useradjoint" })
@Slf4j
public class UserAdjunctController {

	@Autowired
	UserAdjunctService userAdjunctService;

	@GetMapping(value = { "/list/{userId}" })
	public ModelAndView userinfoAdjointList(@PathVariable("userId") String userId) {
		ModelAndView modelAndView = new ModelAndView("/userinfo/userinfoAdjointList");
		modelAndView.addObject("userId", userId);
		return modelAndView;
	}

	@PostMapping(value = { "/grid" })
	public Result<?> queryDataGrid(@RequestBody UserAdjunctQuery userInfoAdjoint, @CurrentUser UserEntity currentUser) {
		log.debug("" + userInfoAdjoint);
		userInfoAdjoint.setInstId(currentUser.getInstId());
		return userAdjunctService.listPage(userInfoAdjoint);
	}

	@GetMapping(value = { "/forwardAdd/{userId}" })
	public ModelAndView forwardAdd(@PathVariable("userId") String userId) {
		ModelAndView modelAndView = new ModelAndView("/userinfo/userinfoAdjointAdd");
		modelAndView.addObject("userId", userId);
		return modelAndView;
	}

	@GetMapping(value = { "/forwardUpdate/{id}" })
	public ModelAndView forwardUpdate(@PathVariable("id") String id) {
		ModelAndView modelAndView = new ModelAndView("/userinfo/userinfoAdjointUpdate");
		UserAdjunctEntity userInfoAdjoint = userAdjunctService.getById(id);
		modelAndView.addObject("model", userInfoAdjoint);
		return modelAndView;
	}

	@PostMapping(value = { "/add" })
	public ResponseEntity<?> insert(@RequestBody UserAdjunctEntity userInfoAdjoint,
			@CurrentUser UserEntity currentUser) {
		log.debug("-Add  :" + userInfoAdjoint);
		userInfoAdjoint.setInstId(currentUser.getInstId());
		if (userAdjunctService.save(userInfoAdjoint)) {
			return new Message<UserAdjunctEntity>(Message.SUCCESS).buildResponse();
		} else {
			return new Message<UserAdjunctEntity>(Message.FAIL).buildResponse();
		}
	}

	/**
	 * 查询
	 * 
	 * @param group
	 * @return
	 */

	@PostMapping(value = { "/query" })
	public ResponseEntity<?> query(@RequestBody UserAdjunctEntity userInfoAdjoint,
			@CurrentUser UserEntity currentUser) {
		log.debug("-query  :" + userInfoAdjoint);
		userInfoAdjoint.setInstId(currentUser.getInstId());
		if (CollectionUtils.isNotEmpty(userAdjunctService.list(userInfoAdjoint))) {
			return new Message<UserAdjunctEntity>(Message.SUCCESS).buildResponse();

		} else {
			return new Message<UserAdjunctEntity>(Message.FAIL).buildResponse();
		}

	}

	/**
	 * 修改
	 * 
	 * @param group
	 * @return
	 */

	@PostMapping(value = { "/update" })
	public ResponseEntity<?> update(@RequestBody UserAdjunctEntity userInfoAdjoint,
			@CurrentUser UserEntity currentUser) {
		log.debug("-update  userInfoAdjoint :" + userInfoAdjoint);
		userInfoAdjoint.setInstId(currentUser.getInstId());
		if (userAdjunctService.updateById(userInfoAdjoint)) {
			return new Message<UserAdjunctEntity>(Message.SUCCESS).buildResponse();
		} else {
			return new Message<UserAdjunctEntity>(Message.FAIL).buildResponse();
		}

	}

	@PostMapping(value = { "/delete" })
	public ResponseEntity<?> delete(@RequestBody UserAdjunctEntity userInfoAdjoint) {
		log.debug("-delete  group :" + userInfoAdjoint);
		if (userAdjunctService.removeById(userInfoAdjoint.getId())) {
			return new Message<UserAdjunctEntity>(Message.SUCCESS).buildResponse();
		} else {
			return new Message<UserAdjunctEntity>(Message.FAIL).buildResponse();
		}
	}
}