package com.wy.test.protocol.formbased;

import java.lang.reflect.InvocationTargetException;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.ModelAndView;

import com.wy.test.authorize.endpoint.AuthorizeBaseEndpoint;
import com.wy.test.authorize.endpoint.adapter.AbstractAuthorizeAdapter;
import com.wy.test.core.authn.annotation.CurrentUser;
import com.wy.test.core.authn.web.AuthorizationUtils;
import com.wy.test.core.convert.AppFormDetailConvert;
import com.wy.test.core.entity.AccountEntity;
import com.wy.test.core.entity.AppEntity;
import com.wy.test.core.entity.AppFormDetailEntity;
import com.wy.test.core.entity.UserEntity;
import com.wy.test.core.vo.AppFormDetailVO;
import com.wy.test.persistence.service.AppFormDetailService;
import com.wy.test.protocol.formbased.adapter.FormDefaultAdapter;

import dream.flying.flower.framework.core.enums.BooleanEnum;
import dream.flying.flower.reflect.ReflectHelper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;

@Tag(name = "2-7-FormBased接口文档模块")
@Controller
@Slf4j
public class FormAuthorizeEndpoint extends AuthorizeBaseEndpoint {

	@Autowired
	AppFormDetailService appFormDetailService;

	@Autowired
	private AppFormDetailConvert appFormDetailConvert;

	FormDefaultAdapter defaultFormBasedAdapter = new FormDefaultAdapter();

	@Operation(summary = "FormBased认证地址接口", description = "参数应用ID", method = "GET")
	@GetMapping("/authz/formbased/{id}")
	public ModelAndView authorize(HttpServletRequest request, @PathVariable("id") String id,
			@CurrentUser UserEntity currentUser)
			throws ClassNotFoundException, NoSuchMethodException, SecurityException, InstantiationException,
			IllegalAccessException, IllegalArgumentException, InvocationTargetException {

		AppFormDetailEntity formBasedDetails = appFormDetailService.getAppDetails(id, true);
		log.debug("formBasedDetails {}", formBasedDetails);

		AppFormDetailVO appFormDetailVO = appFormDetailConvert.convertt(formBasedDetails);
		AppEntity application = getApp(id);
		appFormDetailVO.setAdapter(application.getAdapter());
		appFormDetailVO.setIsAdapter(application.getIsAdapter());
		ModelAndView modelAndView = null;

		AccountEntity account = getAccounts(appFormDetailVO, currentUser);
		log.debug("Accounts {}", account);

		if (account == null) {
			return initCredentialView(id, "/authz/formbased/" + id);
		} else {
			modelAndView = new ModelAndView();

			AbstractAuthorizeAdapter adapter;

			if (BooleanEnum.isTrue(appFormDetailVO.getIsAdapter())) {
				Object formBasedAdapter = ReflectHelper.newInstance(appFormDetailVO.getAdapter());
				adapter = (AbstractAuthorizeAdapter) formBasedAdapter;
			} else {
				FormDefaultAdapter formBasedDefaultAdapter = new FormDefaultAdapter();
				adapter = (AbstractAuthorizeAdapter) formBasedDefaultAdapter;
			}
			adapter.setPrincipal(AuthorizationUtils.getPrincipal());
			adapter.setApp(appFormDetailVO);
			adapter.setAccount(account);

			modelAndView = adapter.authorize(modelAndView);
		}
		log.debug("FormBased View Name {}", modelAndView.getViewName());
		return modelAndView;
	}
}