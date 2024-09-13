package com.wy.test.protocol.extend;

import java.lang.reflect.InvocationTargetException;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.ModelAndView;

import com.wy.test.authentication.core.annotation.CurrentUser;
import com.wy.test.authentication.core.web.AuthorizationUtils;
import com.wy.test.core.entity.AccountEntity;
import com.wy.test.core.enums.CredentialType;
import com.wy.test.core.vo.AppVO;
import com.wy.test.core.vo.UserVO;
import com.wy.test.protocol.authorize.endpoint.AuthorizeBaseEndpoint;
import com.wy.test.protocol.authorize.endpoint.adapter.AbstractAuthorizeAdapter;

import dream.flying.flower.framework.core.enums.BooleanEnum;
import dream.flying.flower.reflect.ReflectHelper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;

@Tag(name = "2-8-ExtendApi接口文档模块")
@Controller
@Slf4j
public class ExtendAuthorizeEndpoint extends AuthorizeBaseEndpoint {

	@Operation(summary = "ExtendApi认证地址接口", description = "参数应用ID", method = "GET")
	@GetMapping("/authz/api/{id}")
	public ModelAndView authorize(HttpServletRequest request, @PathVariable("id") String id,
			@CurrentUser UserVO currentUser) {

		ModelAndView modelAndView = new ModelAndView("authorize/redirect_sso_submit");
		modelAndView.addObject("errorCode", 0);
		modelAndView.addObject("errorMessage", "");

		AppVO apps = getApp(id);
		log.debug("" + apps);
		if (BooleanEnum.isTrue(apps.getIsAdapter())) {
			log.debug("Adapter {}", apps.getAdapter());
			try {
				AbstractAuthorizeAdapter adapter =
						(AbstractAuthorizeAdapter) ReflectHelper.newInstance(apps.getAdapter());
				AccountEntity account = getAccounts(apps, currentUser);
				if (apps.getCredential().equalsIgnoreCase(CredentialType.USER_DEFINED.name()) && account == null) {
					return initCredentialView(id, "/authorize/api/" + id);
				}

				adapter.setPrincipal(AuthorizationUtils.getPrincipal());
				adapter.setApp(apps);
				adapter.setAccount(account);

				return adapter.authorize(modelAndView);
			} catch (ClassNotFoundException | NoSuchMethodException | SecurityException | InstantiationException
					| IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
				e.printStackTrace();
			}
			return null;
		} else {
			log.debug("redirect_uri {}", apps.getLoginUrl());
			modelAndView.addObject("redirect_uri", apps.getLoginUrl());
			return modelAndView;
		}
	}
}