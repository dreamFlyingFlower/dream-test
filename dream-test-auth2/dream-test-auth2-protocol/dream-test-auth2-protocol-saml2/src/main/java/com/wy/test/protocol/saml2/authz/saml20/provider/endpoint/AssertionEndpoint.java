package com.wy.test.protocol.saml2.authz.saml20.provider.endpoint;

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.opensaml.saml2.core.Response;
import org.opensaml.saml2.metadata.Endpoint;
import org.opensaml.ws.message.encoder.MessageEncodingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import com.wy.test.authentication.core.authn.annotation.CurrentUser;
import com.wy.test.authentication.core.authn.web.AuthorizationUtils;
import com.wy.test.core.entity.UserEntity;
import com.wy.test.core.vo.AppSamlDetailVO;
import com.wy.test.core.web.WebConstants;
import com.wy.test.protocol.saml2.authz.saml.common.AuthnRequestInfo;
import com.wy.test.protocol.saml2.authz.saml.common.EndpointGenerator;
import com.wy.test.protocol.saml2.authz.saml20.binding.BindingAdapter;
import com.wy.test.protocol.saml2.authz.saml20.provider.xml.AuthnResponseGenerator;

import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
public class AssertionEndpoint {

	private BindingAdapter bindingAdapter;

	@Autowired
	@Qualifier("endpointGenerator")
	EndpointGenerator endpointGenerator;

	@Autowired
	@Qualifier("authnResponseGenerator")
	AuthnResponseGenerator authnResponseGenerator;

	@GetMapping(value = "/authz/saml20/assertion")
	public ModelAndView assertion(HttpServletRequest request, HttpServletResponse response,
			@CurrentUser UserEntity currentUser) throws Exception {
		log.debug("saml20 assertion start.");
		bindingAdapter =
				(BindingAdapter) request.getSession().getAttribute(WebConstants.AUTHORIZE_SIGN_ON_APP_SAMLV20_ADAPTER);
		log.debug("saml20 assertion get session samlv20Adapter " + bindingAdapter);
		AppSamlDetailVO saml20Details = bindingAdapter.getSaml20Details();
		log.debug("saml20Details " + saml20Details.getExtendAttr());
		AuthnRequestInfo authnRequestInfo = bindingAdapter.getAuthnRequestInfo();

		if (authnRequestInfo == null) {
			log.warn("Could not find AuthnRequest on the request.  Responding with SC_FORBIDDEN.");
			throw new Exception();
		}

		log.debug("AuthnRequestInfo: {}", authnRequestInfo);
		HashMap<String, String> attributeMap = new HashMap<String, String>();
		attributeMap.put(WebConstants.ONLINE_TICKET_NAME,
				AuthorizationUtils.getPrincipal().getSession().getFormattedId());

		// saml20Details
		Response authResponse = authnResponseGenerator.generateAuthnResponse(saml20Details, authnRequestInfo,
				attributeMap, bindingAdapter, currentUser);

		Endpoint endpoint = endpointGenerator.generateEndpoint(saml20Details.getSpAcsUrl());

		request.getSession().removeAttribute(AuthnRequestInfo.class.getName());

		// we could use a different adapter to send the response based on
		// request issuer...
		try {
			bindingAdapter.sendSAMLMessage(authResponse, endpoint, request, response);
		} catch (MessageEncodingException mee) {
			log.error("Exception encoding SAML message", mee);
			throw new Exception(mee);
		}
		return null;
	}
}