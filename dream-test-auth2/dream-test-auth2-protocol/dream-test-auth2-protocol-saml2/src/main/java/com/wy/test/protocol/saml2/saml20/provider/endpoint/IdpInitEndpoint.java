package com.wy.test.protocol.saml2.saml20.provider.endpoint;

import java.security.KeyStore;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.ModelAndView;

import com.wy.test.core.constant.ConstAuthWeb;
import com.wy.test.core.vo.AppSamlDetailVO;
import com.wy.test.core.web.AuthWebContext;
import com.wy.test.persistence.service.AppSamlDetailService;
import com.wy.test.protocol.saml2.saml.common.AuthnRequestInfo;
import com.wy.test.protocol.saml2.saml20.binding.BindingAdapter;
import com.wy.test.protocol.saml2.saml20.binding.ExtractBindingAdapter;

import dream.flying.flower.framework.safe.keystore.KeyStoreHelpers;
import dream.flying.flower.framework.safe.keystore.KeyStoreLoader;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;

/**
 * idp init not need extract SAML request message AuthnRequestInfo use default
 * init
 *
 * @author 飞花梦影
 * @date 2024-07-16 11:10:34
 * @git {@link https://github.com/dreamFlyingFlower}
 */
@Tag(name = "2-2-SAML v2.0 API文档模块")
@Controller
@Slf4j
public class IdpInitEndpoint {

	private BindingAdapter bindingAdapter;

	@Autowired
	@Qualifier("postSimpleSignBindingAdapter")
	private BindingAdapter postSimpleSignBindingAdapter;

	@Autowired
	@Qualifier("postBindingAdapter")
	private BindingAdapter postBindingAdapter;

	@Autowired
	@Qualifier("extractRedirectBindingAdapter")
	private ExtractBindingAdapter extractRedirectBindingAdapter;

	@Autowired
	@Qualifier("keyStoreLoader")
	private KeyStoreLoader keyStoreLoader;

	@Autowired
	private AppSamlDetailService appSamlDetailService;

	/**
	 * 
	 * @param request
	 * @param response
	 * @param appId
	 * @return
	 * @throws Exception
	 * 
	 *
	 */
	@Operation(summary = "SAML 2.0 IDP Init接口", description = "传递参数应用ID", method = "GET")
	@GetMapping(value = "/authz/saml20/idpinit/{appid}")
	public ModelAndView authorizeIdpInit(HttpServletRequest request, HttpServletResponse response,
			@PathVariable("appid") String appId) throws Exception {
		log.debug("SAML IDP init , app id is " + appId);
		AppSamlDetailVO saml20Details = appSamlDetailService.getAppDetails(appId, true);
		AuthWebContext.setAttribute(ConstAuthWeb.AUTHORIZE_SIGN_ON_APP, saml20Details);
		if (saml20Details == null) {
			log.error("samlId[" + appId + "] Error .");
			throw new Exception();
		}

		KeyStore trustKeyStore = KeyStoreHelpers.bytes2KeyStore(saml20Details.getKeystore(),
				keyStoreLoader.getKeyStore().getType(), keyStoreLoader.getKeystorePassword());

		extractRedirectBindingAdapter.setSaml20Detail(saml20Details);
		extractRedirectBindingAdapter.buildSecurityPolicyResolver(trustKeyStore);

		String binding = saml20Details.getBinding();

		if (binding.endsWith("PostSimpleSign")) {
			bindingAdapter = postSimpleSignBindingAdapter;
		} else {
			bindingAdapter = postBindingAdapter;
		}

		// AuthnRequestInfo init authnRequestID to null
		bindingAdapter.setAuthnRequestInfo(new AuthnRequestInfo());

		bindingAdapter.setExtractBindingAdapter(extractRedirectBindingAdapter);

		request.getSession().setAttribute(ConstAuthWeb.AUTHORIZE_SIGN_ON_APP_SAMLV20_ADAPTER, bindingAdapter);

		log.debug("idp init forwarding to assertion :", "/authz/saml20/assertion");

		return AuthWebContext.forward("/authz/saml20/assertion");
	}

	/**
	 * @param keyStoreLoader the keyStoreLoader to set
	 */
	public void setKeyStoreLoader(KeyStoreLoader keyStoreLoader) {
		this.keyStoreLoader = keyStoreLoader;
	}
}