package com.wy.test.protocol.oauth2.filter;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.AuthenticationDetailsSource;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.oauth2.common.util.OAuth2Utils;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.web.HttpRequestMethodNotSupportedException;

import com.wy.test.authentication.core.entity.SignPrincipal;
import com.wy.test.authentication.core.web.AuthorizationUtils;
import com.wy.test.core.web.AuthWebContext;
import com.wy.test.protocol.oauth2.common.OAuth2Constants;
import com.wy.test.protocol.oauth2.endpoint.TokenEndpoint;
import com.wy.test.protocol.oauth2.provider.AuthorizationRequest;
import com.wy.test.protocol.oauth2.provider.OAuth2Authentication;
import com.wy.test.protocol.oauth2.provider.OAuth2Request;
import com.wy.test.protocol.oauth2.provider.OAuth2RequestFactory;

import dream.flying.flower.framework.web.helper.TokenHeader;
import dream.flying.flower.framework.web.helper.TokenHelpers;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 * An optional authentication filter for the {@link TokenEndpoint}. It sits
 * downstream of another filter (usually {@link BasicAuthenticationFilter}) for
 * the client, and creates an {@link OAuth2Authentication} for the Spring
 * {@link SecurityContext} if the request also contains user credentials, e.g.
 * as typically would be the case in a password grant. This filter is only
 * required if the TokenEndpoint (or one of it's dependencies) needs to know
 * about the authenticated user. In a vanilla password grant this <b>isn't</b>
 * normally necessary because the token granter will also authenticate the user.
 * </p>
 * 
 * <p>
 * If this filter is used the Spring Security context will contain an
 * OAuth2Authentication encapsulating (as the authorization request) the form
 * parameters coming into the filter and the client id from the already
 * authenticated client authentication, and the authenticated user token
 * extracted from the request and validated using the authentication manager.
 * </p>
 * 
 * @author Dave Syer
 * 
 */
@WebFilter(filterName = "TokenEndpointAuthenticationFilter",
		urlPatterns = { OAuth2Constants.ENDPOINT.ENDPOINT_TOKEN + "/*",
				OAuth2Constants.ENDPOINT.ENDPOINT_TENCENT_IOA_TOKEN + "/*" })
@Slf4j
@NoArgsConstructor
public class TokenEndpointAuthenticationFilter implements Filter {

	private AuthenticationDetailsSource<HttpServletRequest, ?> authenticationDetailsSource =
			new WebAuthenticationDetailsSource();

	boolean allowOnlyPost;

	private AuthenticationManager authenticationManager;

	private AuthenticationManager oauth2ClientAuthenticationManager;

	private OAuth2RequestFactory oAuth2RequestFactory;

	/**
	 * @param authenticationManager an AuthenticationManager for the incoming
	 *        request
	 */
	public TokenEndpointAuthenticationFilter(AuthenticationManager authenticationManager,
			OAuth2RequestFactory oAuth2RequestFactory) {
		super();
		this.authenticationManager = authenticationManager;
		this.oAuth2RequestFactory = oAuth2RequestFactory;
	}

	/**
	 * A source of authentication details for requests that result in
	 * authentication.
	 * 
	 * @param authenticationDetailsSource the authenticationDetailsSource to set
	 */
	public void setAuthenticationDetailsSource(
			AuthenticationDetailsSource<HttpServletRequest, ?> authenticationDetailsSource) {
		this.authenticationDetailsSource = authenticationDetailsSource;
	}

	@Override
	public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
			throws IOException, ServletException {
		log.debug("Authentication TokenEndpoint ");
		if (authenticationManager == null) {
			authenticationManager =
					AuthWebContext.getBean("oauth2UserAuthenticationManager", AuthenticationManager.class);
		}
		if (oAuth2RequestFactory == null) {
			oAuth2RequestFactory = AuthWebContext.getBean("oAuth2RequestFactory", OAuth2RequestFactory.class);
		}
		if (oauth2ClientAuthenticationManager == null) {
			oauth2ClientAuthenticationManager =
					AuthWebContext.getBean("oauth2ClientAuthenticationManager", AuthenticationManager.class);
		}

		final boolean debug = log.isDebugEnabled();
		final HttpServletRequest request = (HttpServletRequest) req;
		final HttpServletResponse response = (HttpServletResponse) res;

		try {
			String grantType = request.getParameter(OAuth2Utils.GRANT_TYPE);
			if (grantType != null && grantType.equals(OAuth2Constants.PARAMETER.GRANT_TYPE_PASSWORD)) {
				// password
				usernamepassword(request, response);
			} else {
				Authentication authentication = ClientCredentials(request, response);
				log.trace("getPrincipal " + authentication.getPrincipal().getClass());
				SignPrincipal auth = null;
				if (authentication.getPrincipal() instanceof SignPrincipal) {
					// authorization_code
					auth = (SignPrincipal) authentication.getPrincipal();
				} else {
					// client_credentials
					auth = new SignPrincipal((User) authentication.getPrincipal());
				}
				auth.setAuthenticated(true);
				UsernamePasswordAuthenticationToken simpleUserAuthentication = new UsernamePasswordAuthenticationToken(
						auth, authentication.getCredentials(), authentication.getAuthorities());
				AuthorizationUtils.setAuthentication(simpleUserAuthentication);
			}

		} catch (AuthenticationException failed) {
			SecurityContextHolder.clearContext();

			if (debug) {
				log.debug("Authentication request for failed: " + failed);
			}

			onUnsuccessfulAuthentication(request, response, failed);

			return;
		}

		chain.doFilter(request, response);
	}

	public void usernamepassword(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		log.debug("Authentication TokenEndpoint ");

		try {
			Authentication credentials = extractCredentials(request);

			if (credentials != null) {
				log.debug("Authentication credentials found for '" + credentials.getName() + "'");

				Authentication authResult = authenticationManager.authenticate(credentials);

				log.debug("Authentication success: " + authResult.getName());
				String clientId = request.getParameter(OAuth2Utils.CLIENT_ID);
				String clientSecret = request.getParameter(OAuth2Constants.PARAMETER.CLIENT_SECRET);
				UsernamePasswordAuthenticationToken authRequest =
						new UsernamePasswordAuthenticationToken(clientId, clientSecret);
				Authentication clientAuth = oauth2ClientAuthenticationManager.authenticate(authRequest);
				// Authentication clientAuth =
				// SecurityContextHolder.getContext().getAuthentication();
				if (clientAuth == null) {
					throw new BadCredentialsException(
							"No client authentication found. Remember to put a filter upstream of the TokenEndpointAuthenticationFilter.");
				}

				Map<String, String> map = getSingleValueMap(request);
				map.put(OAuth2Utils.CLIENT_ID, clientAuth.getName());
				AuthorizationRequest authorizationRequest = oAuth2RequestFactory.createAuthorizationRequest(map);

				authorizationRequest.setScope(getScope(request));
				if (clientAuth.isAuthenticated()) {
					// Ensure the OAuth2Authentication is authenticated
					authorizationRequest.setApproved(true);
				}

				OAuth2Request storedOAuth2Request = oAuth2RequestFactory.createOAuth2Request(authorizationRequest);

				AuthorizationUtils.setAuthentication(new OAuth2Authentication(storedOAuth2Request, authResult));

				onSuccessfulAuthentication(request, response, authResult);

			}

		} catch (AuthenticationException failed) {
			SecurityContextHolder.clearContext();

			log.debug("Authentication request for failed: " + failed);

			onUnsuccessfulAuthentication(request, response, failed);

			return;
		}
	}

	public Authentication ClientCredentials(HttpServletRequest request, HttpServletResponse response)
			throws AuthenticationException, IOException, ServletException {
		if (allowOnlyPost && !"POST".equalsIgnoreCase(request.getMethod())) {
			throw new HttpRequestMethodNotSupportedException(request.getMethod(), new String[] { "POST", "G" });
		}

		String clientId = request.getParameter(OAuth2Utils.CLIENT_ID);
		String clientSecret = request.getParameter(OAuth2Constants.PARAMETER.CLIENT_SECRET);
		if (clientId == null) {
			// for header authorization basic
			String authorization_bearer = request.getHeader("authorization");
			TokenHeader ahc = TokenHelpers.resolve(authorization_bearer);
			clientId = ahc.getUsername();
			clientSecret = ahc.getCredential();
		}

		log.trace("clientId " + clientId + " , clientSecret " + clientSecret);

		// If the request is already authenticated we can assume that this
		// filter is not needed
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication != null && authentication.isAuthenticated()) {
			return authentication;
		}

		if (clientId == null) {
			throw new BadCredentialsException("No client credentials presented");
		}

		if (clientSecret == null) {
			clientSecret = "";
		}

		clientId = clientId.trim();
		UsernamePasswordAuthenticationToken authRequest =
				new UsernamePasswordAuthenticationToken(clientId, clientSecret);

		return this.oauth2ClientAuthenticationManager.authenticate(authRequest);
	}

	private Map<String, String> getSingleValueMap(HttpServletRequest request) {
		Map<String, String> map = new HashMap<String, String>();
		Map<String, String[]> parameters = request.getParameterMap();
		for (String key : parameters.keySet()) {
			String[] values = parameters.get(key);
			map.put(key, values != null && values.length > 0 ? values[0] : null);
		}
		return map;
	}

	protected void onSuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response,
			Authentication authResult) throws IOException {
	}

	protected void onUnsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException failed) throws IOException {
	}

	/**
	 * If the incoming request contains user credentials in headers or parameters
	 * then extract them here into an Authentication token that can be validated
	 * later. This implementation only recognises password grant requests and
	 * extracts the username and password.
	 * 
	 * @param request the incoming request, possibly with user credentials
	 * @return an authentication for validation (or null if there is no further
	 *         authentication)
	 */
	protected Authentication extractCredentials(HttpServletRequest request) {
		String grantType = request.getParameter(OAuth2Utils.GRANT_TYPE);
		if (grantType != null && grantType.equals(OAuth2Constants.PARAMETER.GRANT_TYPE_PASSWORD)) {
			UsernamePasswordAuthenticationToken result = new UsernamePasswordAuthenticationToken(
					request.getParameter("username"), request.getParameter("password"));
			result.setDetails(authenticationDetailsSource.buildDetails(request));
			return result;
		}
		return null;
	}

	private Set<String> getScope(HttpServletRequest request) {
		return OAuth2Utils.parseParameterList(request.getParameter(OAuth2Utils.SCOPE));
	}

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
	}

	@Override
	public void destroy() {
	}

	protected static class ClientCredentialsRequestMatcher implements RequestMatcher {

		private String path;

		public ClientCredentialsRequestMatcher(String path) {
			this.path = path;

		}

		@Override
		public boolean matches(HttpServletRequest request) {
			String uri = request.getRequestURI();
			int pathParamIndex = uri.indexOf(';');

			if (pathParamIndex > 0) {
				// strip everything after the first semi-colon
				uri = uri.substring(0, pathParamIndex);
			}

			String clientId = request.getParameter(OAuth2Utils.CLIENT_ID);

			if (clientId == null) {
				// Give basic auth a chance to work instead (it's preferred anyway)
				return false;
			}

			if ("".equals(request.getContextPath())) {
				return uri.endsWith(path);
			}

			return uri.endsWith(request.getContextPath() + path);
		}

	}

}
