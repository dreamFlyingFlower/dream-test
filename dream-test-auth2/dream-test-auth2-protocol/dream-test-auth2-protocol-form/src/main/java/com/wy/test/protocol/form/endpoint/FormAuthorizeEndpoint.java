package com.wy.test.protocol.form.endpoint;

import java.lang.reflect.InvocationTargetException;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.ModelAndView;

import com.wy.test.authentication.core.annotation.CurrentUser;
import com.wy.test.authentication.core.web.AuthorizationUtils;
import com.wy.test.core.entity.AccountEntity;
import com.wy.test.core.vo.AppFormDetailVO;
import com.wy.test.core.vo.AppVO;
import com.wy.test.core.vo.UserVO;
import com.wy.test.persistence.service.AppFormDetailService;
import com.wy.test.protocol.authorize.endpoint.AuthorizeBaseEndpoint;
import com.wy.test.protocol.authorize.endpoint.adapter.AbstractAuthorizeAdapter;
import com.wy.test.protocol.form.adapter.FormDefaultAdapter;

import dream.flying.flower.enums.BooleanEnum;
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

	FormDefaultAdapter defaultFormBasedAdapter = new FormDefaultAdapter();

	@Operation(summary = "FormBased认证地址接口", description = "参数应用ID", method = "GET")
	@GetMapping("/authz/formbased/{id}")
	public ModelAndView authorize(HttpServletRequest request, @PathVariable("id") String id,
			@CurrentUser UserVO currentUser) throws ClassNotFoundException, NoSuchMethodException, SecurityException,
			InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {

		AppFormDetailVO appFormDetailVO = appFormDetailService.getAppDetails(id, true);
		log.debug("formBasedDetails {}", appFormDetailVO);
		AppVO application = getApp(id);
		appFormDetailVO.setAdapterClass(application.getAdapterClass());
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
				Object formBasedAdapter = ReflectHelper.newInstance(appFormDetailVO.getAdapterClass());
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