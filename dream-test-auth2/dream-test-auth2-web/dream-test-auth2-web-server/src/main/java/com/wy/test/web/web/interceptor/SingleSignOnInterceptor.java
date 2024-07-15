package com.wy.test.web.web.interceptor;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.web.util.UrlUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.AsyncHandlerInterceptor;

import com.wy.test.authz.oauth2.common.OAuth2Constants;
import com.wy.test.cas.authz.endpoint.ticket.CasConstants;
import com.wy.test.configuration.ApplicationConfig;
import com.wy.test.core.authn.SignPrincipal;
import com.wy.test.core.authn.jwt.AuthTokenService;
import com.wy.test.core.authn.session.SessionManager;
import com.wy.test.core.authn.web.AuthorizationUtils;
import com.wy.test.crypto.Base64Utils;
import com.wy.test.entity.apps.Apps;
import com.wy.test.persistence.service.AppsCasDetailsService;
import com.wy.test.persistence.service.AppsService;
import com.wy.test.web.WebConstants;
import com.wy.test.web.WebContext;

@Component
public class SingleSignOnInterceptor  implements AsyncHandlerInterceptor {
    private static final Logger _logger = LoggerFactory.getLogger(SingleSignOnInterceptor.class);
    
    @Autowired
    ApplicationConfig applicationConfig;
    
    @Autowired
	SessionManager sessionManager;
    
    @Autowired
	AuthTokenService authTokenService ;
    
    @Autowired
    AppsService appsService;
    
    @Autowired
    AppsCasDetailsService casDetailsService;
    
    @Override
    public boolean preHandle(HttpServletRequest request, 
            HttpServletResponse response, Object handler)
            throws Exception {
    	_logger.trace("Single Sign On Interceptor");
       
    	AuthorizationUtils.authenticateWithCookie(
    				request,authTokenService,sessionManager);

        if(AuthorizationUtils.isNotAuthenticated()) {
        	String loginUrl = applicationConfig.getFrontendUri() + "/#/passport/login?redirect_uri=%s";
        	String redirect_uri = UrlUtils.buildFullRequestUrl(request);
        	String base64RequestUrl = Base64Utils.base64UrlEncode(redirect_uri.getBytes());
        	_logger.debug("No Authentication ... Redirect to /passport/login , redirect_uri {} , base64 {}",
        					redirect_uri ,base64RequestUrl);
        	response.sendRedirect(String.format(loginUrl,base64RequestUrl));
        	return false;
        }
        
        //判断应用访问权限
        if(AuthorizationUtils.isAuthenticated()){
	        _logger.debug("preHandle {}",request.getRequestURI());
	        Apps app = (Apps)WebContext.getAttribute(WebConstants.AUTHORIZE_SIGN_ON_APP);
	        if(app == null) {
	        	
	        	String requestURI = request.getRequestURI();
	        	if(requestURI.contains("/authz/cas/login")) {//for CAS service
	        		app = casDetailsService.getAppDetails(
	        				request.getParameter(CasConstants.PARAMETER.SERVICE), true);
	        	}else if(requestURI.contains("/authz/jwt/")
	        			||requestURI.contains("/authz/api/")
	        			||requestURI.contains("/authz/formbased/")
	        			||requestURI.contains("/authz/tokenbased/")
	        			||requestURI.contains("/authz/api/")
	        			||requestURI.contains("/authz/saml20/consumer/")
	        			||requestURI.contains("/authz/saml20/idpinit/")
	        			||requestURI.contains("/authz/cas/")
	        	) {//for id end of URL
	        		String [] requestURIs = requestURI.split("/");
	        		String appId = requestURIs[requestURIs.length -1];
	        		_logger.debug("appId {}",appId);
		        	app = appsService.get(appId,true);
	        	}else if(requestURI.contains("/authz/oauth/v20/authorize")) {//oauth
		        	app = appsService.get(
		        			request.getParameter(OAuth2Constants.PARAMETER.CLIENT_ID),true);
	        	}
	        }
	        
	        if(app == null) {
	        	_logger.debug("preHandle app is not exist . ");
	        	return true;
	        }
	        
	        SignPrincipal principal = AuthorizationUtils.getPrincipal();
	        if(principal != null && app !=null) {
	            if(principal.getGrantedAuthorityApps().contains(new SimpleGrantedAuthority(app.getId()))) {
	                _logger.trace("preHandle have authority access {}" , app);
	                return true;
	            }
	        }
	        _logger.debug("preHandle not have authority access " + app);
	        response.sendRedirect(request.getContextPath()+"/authz/refused");
	        return false;
    	}
        return true;
    }

}
