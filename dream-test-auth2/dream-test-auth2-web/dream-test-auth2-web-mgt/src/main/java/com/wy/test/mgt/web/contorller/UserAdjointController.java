package com.wy.test.mgt.web.contorller;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.wy.test.core.authn.annotation.CurrentUser;
import com.wy.test.core.entity.Message;
import com.wy.test.core.entity.UserAdjunctEntity;
import com.wy.test.core.entity.UserEntity;
import com.wy.test.core.query.UserAdjunctQuery;
import com.wy.test.persistence.service.UserAdjunctService;

import dream.flying.flower.result.Result;
import lombok.extern.slf4j.Slf4j;

@Controller
@RequestMapping(value = { "/useradjoint" })
@Slf4j
public class UserAdjointController {

	@Autowired
	UserAdjunctService userAdjunctService;

	@GetMapping(value = { "/list/{userId}" })
	public ModelAndView userinfoAdjointList(@PathVariable("userId") String userId) {
		ModelAndView modelAndView = new ModelAndView("/userinfo/userinfoAdjointList");
		modelAndView.addObject("userId", userId);
		return modelAndView;
	}

	@PostMapping(value = { "/grid" })
	@ResponseBody
	public Result<?> queryDataGrid(@ModelAttribute("userInfoAdjoint") UserAdjunctQuery userInfoAdjoint,
			@CurrentUser UserEntity currentUser) {
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

	@ResponseBody
	@PostMapping(value = { "/add" })
	public ResponseEntity<?> insert(@ModelAttribute("userInfoAdjoint") UserAdjunctEntity userInfoAdjoint,
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
	@ResponseBody
	@PostMapping(value = { "/query" })
	public ResponseEntity<?> query(@ModelAttribute("userInfoAdjoint") UserAdjunctEntity userInfoAdjoint,
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
	@ResponseBody
	@PostMapping(value = { "/update" })
	public ResponseEntity<?> update(@ModelAttribute("userInfoAdjoint") UserAdjunctEntity userInfoAdjoint,
			@CurrentUser UserEntity currentUser) {
		log.debug("-update  userInfoAdjoint :" + userInfoAdjoint);
		userInfoAdjoint.setInstId(currentUser.getInstId());
		if (userAdjunctService.updateById(userInfoAdjoint)) {
			return new Message<UserAdjunctEntity>(Message.SUCCESS).buildResponse();
		} else {
			return new Message<UserAdjunctEntity>(Message.FAIL).buildResponse();
		}

	}

	@ResponseBody
	@PostMapping(value = { "/delete" })
	public ResponseEntity<?> delete(@ModelAttribute("userInfoAdjoint") UserAdjunctEntity userInfoAdjoint) {
		log.debug("-delete  group :" + userInfoAdjoint);
		if (userAdjunctService.removeById(userInfoAdjoint.getId())) {
			return new Message<UserAdjunctEntity>(Message.SUCCESS).buildResponse();
		} else {
			return new Message<UserAdjunctEntity>(Message.FAIL).buildResponse();
		}
	}
}