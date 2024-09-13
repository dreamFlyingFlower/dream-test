package com.wy.test.protocol.saml2.saml20.provider.endpoint;

import java.security.KeyStore;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.opensaml.common.binding.SAMLMessageContext;
import org.opensaml.saml2.core.AuthnRequest;
import org.opensaml.ws.message.decoder.MessageDecodingException;
import org.opensaml.xml.security.SecurityException;
import org.opensaml.xml.validation.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import com.wy.test.core.constant.ConstAuthWeb;
import com.wy.test.core.vo.AppSamlDetailVO;
import com.wy.test.core.web.AuthWebContext;
import com.wy.test.persistence.service.AppSamlDetailService;
import com.wy.test.protocol.saml2.saml.common.AuthnRequestInfo;
import com.wy.test.protocol.saml2.saml20.binding.BindingAdapter;
import com.wy.test.protocol.saml2.saml20.binding.ExtractBindingAdapter;
import com.wy.test.protocol.saml2.saml20.xml.SAML2ValidatorSuite;

import dream.flying.flower.framework.web.crypto.keystore.KeyStoreHelpers;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;

@Tag(name = "2-2-SAML v2.0 API文档模块")
@Controller
@Slf4j
public class SingleSignOnEndpoint {

	private BindingAdapter bindingAdapter;

	@Autowired
	@Qualifier("postSimpleSignBindingAdapter")
	private BindingAdapter postSimpleSignBindingAdapter;

	@Autowired
	@Qualifier("postBindingAdapter")
	private BindingAdapter postBindingAdapter;

	@Autowired
	@Qualifier("extractPostBindingAdapter")
	private ExtractBindingAdapter extractPostBindingAdapter;

	@Autowired
	@Qualifier("extractRedirectBindingAdapter")
	private ExtractBindingAdapter extractRedirectBindingAdapter;

	@Autowired
	@Qualifier("samlValidaotrSuite")
	private SAML2ValidatorSuite validatorSuite;

	@Autowired
	private AppSamlDetailService appSamlDetailService;

	@Operation(summary = "SAML 2.0 SP Init接收接口", description = "传递参数应用ID", method = "POST")
	@PostMapping(value = "/authz/saml20/{appid}")
	public ModelAndView authorizePost(HttpServletRequest request, HttpServletResponse response,
			@PathVariable("appid") String appId) throws Exception {
		log.debug("SAML Authorize Redirect do POST , app id is  " + appId);
		return extractSAMLRequest(extractPostBindingAdapter, appId, request);
	}

	@Operation(summary = "SAML 2.0 SP Init接收接口", description = "传递参数应用ID", method = "GET")
	@GetMapping(value = "/authz/saml20/{appid}")
	public ModelAndView authorizeRedirect(HttpServletRequest request, HttpServletResponse response,
			@PathVariable("appid") String appId) throws Exception {
		log.debug("SAML Authorize Redirect do GET , app id is  " + appId);
		return extractSAMLRequest(extractRedirectBindingAdapter, appId, request);
	}

	public ModelAndView extractSAMLRequest(ExtractBindingAdapter extractBindingAdapter, String appId,
			HttpServletRequest request) throws Exception {
		log.debug("SAML Redirect Binding , app id is " + appId);

		extractSaml20Detail(extractBindingAdapter, appId);

		extractSAMLMessage(extractBindingAdapter, request);

		request.getSession().setAttribute(ConstAuthWeb.AUTHORIZE_SIGN_ON_APP_SAMLV20_ADAPTER, bindingAdapter);

		return AuthWebContext.forward("/authz/saml20/assertion");
	}

	public void extractSaml20Detail(ExtractBindingAdapter extractBindingAdapter, String samlId) throws Exception {
		AppSamlDetailVO saml20Details = appSamlDetailService.getAppDetails(samlId, true);
		AuthWebContext.setAttribute(ConstAuthWeb.AUTHORIZE_SIGN_ON_APP, saml20Details);
		if (saml20Details == null) {
			log.error("Request SAML APPID [" + samlId + "] is not exist .");
			throw new Exception();
		}

		KeyStore trustKeyStore = KeyStoreHelpers.bytes2KeyStore(saml20Details.getKeystore(),
				extractBindingAdapter.getKeyStoreLoader().getKeyStore().getType(),
				extractBindingAdapter.getKeyStoreLoader().getKeystorePassword());

		extractBindingAdapter.setSaml20Detail(saml20Details);
		extractBindingAdapter.buildSecurityPolicyResolver(trustKeyStore);
	}

	@SuppressWarnings("rawtypes")
	public void extractSAMLMessage(ExtractBindingAdapter extractBindingAdapter, HttpServletRequest request)
			throws Exception {

		SAMLMessageContext messageContext;
		log.debug("extract SAML Message .");

		try {
			messageContext = extractBindingAdapter.extractSAMLMessageContext(request);
			log.debug("validate SAML AuthnRequest .");
			AuthnRequest authnRequest = (AuthnRequest) messageContext.getInboundSAMLMessage();
			log.debug("AuthnRequest ProtocolBinding " + authnRequest.getProtocolBinding());
			log.debug("InboundSAMLMessage Id " + messageContext.getInboundSAMLMessageId());
			log.debug("AuthnRequest AssertionConsumerServiceURL " + authnRequest.getAssertionConsumerServiceURL());
			log.debug("InboundMessage Issuer " + messageContext.getInboundMessageIssuer());
			log.debug("InboundSAMLMessage IssueInstant " + messageContext.getInboundSAMLMessageIssueInstant());
			log.debug("InboundSAMLMessage RelayState " + messageContext.getRelayState());
			log.debug("AuthnRequest isPassive " + authnRequest.isPassive());
			log.debug("AuthnRequest ForceAuthn " + authnRequest.isForceAuthn());

			validatorSuite.validate(authnRequest);

			log.debug("Select Authz  Binding.");
			String binding = extractBindingAdapter.getSaml20Detail().getBinding();

			if (binding.endsWith("PostSimpleSign")) {
				bindingAdapter = postSimpleSignBindingAdapter;
				log.debug("Authz POST Binding is  use PostSimpleSign .");
			} else {
				bindingAdapter = postBindingAdapter;
				log.debug("Authz POST Binding is  use Post .");
			}

			AuthnRequestInfo authnRequestInfo =
					new AuthnRequestInfo(authnRequest.getAssertionConsumerServiceURL(), authnRequest.getID());

			log.debug("AuthnRequest vefified.  Forwarding to AuthnResponder", authnRequestInfo);

			bindingAdapter.setAuthnRequestInfo(authnRequestInfo);

			bindingAdapter.setExtractBindingAdapter(extractBindingAdapter);

			String relayState = request.getParameter("RelayState");
			if (relayState != null) {
				bindingAdapter.setRelayState(relayState);
				log.debug("RelayState : ", relayState);
			}

		} catch (MessageDecodingException e1) {
			log.error("Exception decoding SAML MessageDecodingException", e1);
			throw new Exception(e1);
		} catch (SecurityException e1) {
			log.error("Exception decoding SAML SecurityException", e1);
			throw new Exception(e1);
		} catch (ValidationException ve) {
			log.warn("AuthnRequest Message failed Validation", ve);
			throw new Exception(ve);
		}

	}

	/**
	 * @param validatorSuite the validatorSuite to set
	 */
	public void setValidatorSuite(SAML2ValidatorSuite validatorSuite) {
		this.validatorSuite = validatorSuite;
	}

}
