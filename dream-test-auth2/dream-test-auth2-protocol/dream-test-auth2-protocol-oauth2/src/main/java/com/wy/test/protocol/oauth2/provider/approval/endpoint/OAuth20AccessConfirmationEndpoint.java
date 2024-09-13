package com.wy.test.protocol.oauth2.provider.approval.endpoint;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.common.util.OAuth2Utils;
import org.springframework.security.oauth2.provider.approval.Approval;
import org.springframework.security.oauth2.provider.approval.Approval.ApprovalStatus;
import org.springframework.security.oauth2.provider.approval.ApprovalStore;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.wy.test.authentication.core.authn.annotation.CurrentUser;
import com.wy.test.authentication.core.authn.jwt.AuthTokenService;
import com.wy.test.authentication.core.authn.web.AuthorizationUtils;
import com.wy.test.core.cache.MomentaryService;
import com.wy.test.core.entity.Message;
import com.wy.test.core.entity.oauth2.ClientDetails;
import com.wy.test.core.properties.DreamAuthServerProperties;
import com.wy.test.core.vo.AppVO;
import com.wy.test.core.vo.UserVO;
import com.wy.test.persistence.service.AppService;
import com.wy.test.protocol.oauth2.common.OAuth2Constants;
import com.wy.test.protocol.oauth2.provider.AuthorizationRequest;
import com.wy.test.protocol.oauth2.provider.ClientDetailsService;

import lombok.extern.slf4j.Slf4j;

/**
 * Controller for retrieving the model for and displaying the confirmation page
 * for access to a protected resource.
 */
@Controller
@Slf4j
public class OAuth20AccessConfirmationEndpoint {

	@Autowired
	@Qualifier("appService")
	protected AppService appService;

	@Autowired
	@Qualifier("oauth20JdbcClientDetailsService")
	private ClientDetailsService clientDetailsService;

	@Autowired
	@Qualifier("oauth20ApprovalStore")
	private ApprovalStore approvalStore;

	@Autowired
	@Qualifier("oauth20UserApprovalHandler")
	OAuth20UserApprovalHandler oauth20UserApprovalHandler;

	@Autowired
	protected MomentaryService momentaryService;

	@Autowired
	protected DreamAuthServerProperties dreamServerProperties;

	@Autowired
	AuthTokenService authTokenService;

	/**
	 * getAccessConfirmation.
	 * 
	 * @param model Map
	 * @return throws Exception
	 */

	@GetMapping(OAuth2Constants.ENDPOINT.ENDPOINT_APPROVAL_CONFIRM)
	public ModelAndView getAccessConfirmation(@RequestParam Map<String, Object> model,
			@CurrentUser UserVO currentUser) {
		try {
			// Map<String, Object> model
			AuthorizationRequest clientAuth =
					(AuthorizationRequest) momentaryService.get(currentUser.getSessionId(), "authorizationRequest");
			ClientDetails client = clientDetailsService.loadClientByClientId(clientAuth.getClientId(), true);
			model.put("oauth_approval", authTokenService.genRandomJwt());
			model.put("auth_request", clientAuth);
			model.put("client", client);
			model.put("oauth_version", "oauth 2.0");
			Map<String, String> scopes = new LinkedHashMap<String, String>();
			for (String scope : clientAuth.getScope()) {
				scopes.put(OAuth2Utils.SCOPE_PREFIX + scope, "false");
			}
			String principal = AuthorizationUtils.getPrincipal().getUsername();
			for (Approval approval : approvalStore.getApprovals(principal, client.getClientId())) {
				if (clientAuth.getScope().contains(approval.getScope())) {
					scopes.put(OAuth2Utils.SCOPE_PREFIX + approval.getScope(),
							approval.getStatus() == ApprovalStatus.APPROVED ? "true" : "false");
				}
			}

			model.put("scopes", scopes);

			if (!model.containsKey(OAuth2Constants.PARAMETER.APPROVAL_PROMPT)) {
				model.put(OAuth2Constants.PARAMETER.APPROVAL_PROMPT, client.getApprovalPrompt());
			}
		} catch (Exception e) {
			log.debug("OAuth Access Confirmation process error.", e);
		}

		ModelAndView modelAndView = new ModelAndView("authorize/oauth_access_confirmation");
		log.trace("Confirmation details ");
		for (Object key : model.keySet()) {
			log.trace("key " + key + "=" + model.get(key));
		}

		model.put("authorizeApproveUri", dreamServerProperties.getFrontendUri() + "/#/authz/oauth2approve");

		modelAndView.addObject("model", model);
		return modelAndView;
	}

	@GetMapping(OAuth2Constants.ENDPOINT.ENDPOINT_APPROVAL_CONFIRM + "/get/{oauth_approval}")
	public ResponseEntity<?> getAccess(@PathVariable("oauth_approval") String oauth_approval,
			@CurrentUser UserVO currentUser) {
		Map<String, Object> model = new HashMap<String, Object>();
		if (authTokenService.validateJwtToken(oauth_approval)) {
			try {
				AuthorizationRequest clientAuth =
						(AuthorizationRequest) momentaryService.get(currentUser.getSessionId(), "authorizationRequest");
				ClientDetails client = clientDetailsService.loadClientByClientId(clientAuth.getClientId(), true);

				AppVO app = appService.get(client.getClientId(), true);
				app.transIconBase64();

				model.put("auth_request", clientAuth);
				model.put("client", client);
				model.put("clientId", clientAuth.getClientId());
				model.put("appName", app.getAppName());
				model.put("iconBase64", app.getIconBase64());
				model.put("oauth_version", "oauth 2.0");
				Map<String, String> scopes = new LinkedHashMap<String, String>();
				for (String scope : clientAuth.getScope()) {
					scopes.put(OAuth2Utils.SCOPE_PREFIX + scope, "false");
				}
				String principal = AuthorizationUtils.getPrincipal().getUsername();
				for (Approval approval : approvalStore.getApprovals(principal, client.getClientId())) {
					if (clientAuth.getScope().contains(approval.getScope())) {
						scopes.put(OAuth2Utils.SCOPE_PREFIX + approval.getScope(),
								approval.getStatus() == ApprovalStatus.APPROVED ? "true" : "false");
					}
				}

				model.put("scopes", scopes);

				if (!model.containsKey(OAuth2Constants.PARAMETER.APPROVAL_PROMPT)) {
					model.put(OAuth2Constants.PARAMETER.APPROVAL_PROMPT, client.getApprovalPrompt());
				}
			} catch (Exception e) {
				log.debug("OAuth Access Confirmation process error.", e);
			}

			log.trace("Confirmation details ");
			for (Object key : model.keySet()) {
				log.trace("key " + key + "=" + model.get(key));
			}
		}
		return new Message<Map<String, Object>>(model).buildResponse();
	}

	/**
	 * handleError.
	 * 
	 * @param model Map
	 * @return throws Exception
	 */
	@GetMapping(OAuth2Constants.ENDPOINT.ENDPOINT_ERROR)
	public String handleError(Map<String, Object> model) throws Exception {
		// We can add more stuff to the model here for JSP rendering. If the client was
		// a machine then
		// the JSON will already have been rendered.
		model.put("message", "There was a problem with the OAuth2 protocol");
		return "oauth_error";
	}
}