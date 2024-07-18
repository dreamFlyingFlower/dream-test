package com.wy.test.web.contorller;

import org.apache.commons.collections4.CollectionUtils;
import org.dromara.mybatis.jpa.entity.JpaPageResults;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import com.wy.test.core.entity.UserInfo;
import com.wy.test.core.entity.UserInfoAdjoint;
import com.wy.test.entity.Message;
import com.wy.test.persistence.service.UserInfoAdjointService;

@Controller
@RequestMapping(value = { "/useradjoint" })
public class UserAdjointController {

	final static Logger _logger = LoggerFactory.getLogger(UserAdjointController.class);

	@Autowired
	UserInfoAdjointService userInfoAdjointService;

	@GetMapping(value = { "/list/{userId}" })
	public ModelAndView userinfoAdjointList(@PathVariable("userId") String userId) {
		ModelAndView modelAndView = new ModelAndView("/userinfo/userinfoAdjointList");
		modelAndView.addObject("userId", userId);
		return modelAndView;
	}

	@PostMapping(value = { "/grid" })
	@ResponseBody
	public JpaPageResults<UserInfoAdjoint> queryDataGrid(
			@ModelAttribute("userInfoAdjoint") UserInfoAdjoint userInfoAdjoint, @CurrentUser UserInfo currentUser) {
		_logger.debug("" + userInfoAdjoint);
		userInfoAdjoint.setInstId(currentUser.getInstId());
		return userInfoAdjointService.fetchPageResults(userInfoAdjoint);
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
		UserInfoAdjoint userInfoAdjoint = userInfoAdjointService.get(id);
		modelAndView.addObject("model", userInfoAdjoint);
		return modelAndView;
	}

	@ResponseBody
	@PostMapping(value = { "/add" })
	public ResponseEntity<?> insert(@ModelAttribute("userInfoAdjoint") UserInfoAdjoint userInfoAdjoint,
			@CurrentUser UserInfo currentUser) {
		_logger.debug("-Add  :" + userInfoAdjoint);
		userInfoAdjoint.setInstId(currentUser.getInstId());
		if (userInfoAdjointService.insert(userInfoAdjoint)) {
			return new Message<UserInfoAdjoint>(Message.SUCCESS).buildResponse();

		} else {
			return new Message<UserInfoAdjoint>(Message.FAIL).buildResponse();
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
	public ResponseEntity<?> query(@ModelAttribute("userInfoAdjoint") UserInfoAdjoint userInfoAdjoint,
			@CurrentUser UserInfo currentUser) {
		_logger.debug("-query  :" + userInfoAdjoint);
		userInfoAdjoint.setInstId(currentUser.getInstId());
		if (CollectionUtils.isNotEmpty(userInfoAdjointService.query(userInfoAdjoint))) {
			return new Message<UserInfoAdjoint>(Message.SUCCESS).buildResponse();

		} else {
			return new Message<UserInfoAdjoint>(Message.FAIL).buildResponse();
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
	public ResponseEntity<?> update(@ModelAttribute("userInfoAdjoint") UserInfoAdjoint userInfoAdjoint,
			@CurrentUser UserInfo currentUser) {
		_logger.debug("-update  userInfoAdjoint :" + userInfoAdjoint);
		userInfoAdjoint.setInstId(currentUser.getInstId());
		if (userInfoAdjointService.update(userInfoAdjoint)) {
			return new Message<UserInfoAdjoint>(Message.SUCCESS).buildResponse();

		} else {
			return new Message<UserInfoAdjoint>(Message.FAIL).buildResponse();
		}

	}

	@ResponseBody
	@PostMapping(value = { "/delete" })
	public ResponseEntity<?> delete(@ModelAttribute("userInfoAdjoint") UserInfoAdjoint userInfoAdjoint) {
		_logger.debug("-delete  group :" + userInfoAdjoint);

		if (userInfoAdjointService.deleteBatch(userInfoAdjoint.getId())) {
			return new Message<UserInfoAdjoint>(Message.SUCCESS).buildResponse();
		} else {
			return new Message<UserInfoAdjoint>(Message.FAIL).buildResponse();
		}

	}
}
