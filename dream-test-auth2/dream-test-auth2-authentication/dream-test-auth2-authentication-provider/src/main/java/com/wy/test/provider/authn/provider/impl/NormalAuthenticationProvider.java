package com.wy.test.provider.authn.provider.impl;

import java.text.ParseException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

import com.wy.test.configuration.ApplicationConfig;
import com.wy.test.constants.ConstsLoginType;
import com.wy.test.core.authn.LoginCredential;
import com.wy.test.core.authn.jwt.AuthTokenService;
import com.wy.test.core.authn.session.SessionManager;
import com.wy.test.entity.Institutions;
import com.wy.test.entity.UserInfo;
import com.wy.test.provider.authn.provider.AbstractAuthenticationProvider;
import com.wy.test.provider.authn.realm.AbstractAuthenticationRealm;
import com.wy.test.web.WebConstants;
import com.wy.test.web.WebContext;

/**
 * database Authentication provider.
 * @author Crystal.Sea
 *
 */
public class NormalAuthenticationProvider extends AbstractAuthenticationProvider {
    private static final Logger _logger =
            LoggerFactory.getLogger(NormalAuthenticationProvider.class);

    public String getProviderName() {
        return "normal" + PROVIDER_SUFFIX;
    }
    

    public NormalAuthenticationProvider() {
		super();
	}

    public NormalAuthenticationProvider(
    		AbstractAuthenticationRealm authenticationRealm,
    		ApplicationConfig applicationConfig,
    	    SessionManager sessionManager,
    	    AuthTokenService authTokenService) {
		this.authenticationRealm = authenticationRealm;
		this.applicationConfig = applicationConfig;
		this.sessionManager = sessionManager;
		this.authTokenService = authTokenService;
	}

    @Override
	public Authentication doAuthenticate(LoginCredential loginCredential) {
		UsernamePasswordAuthenticationToken authenticationToken = null;
		_logger.debug("Trying to authenticate user '{}' via {}", 
                loginCredential.getPrincipal(), getProviderName());
        try {
        	
	        _logger.debug("authentication " + loginCredential);
	        
	        Institutions inst = (Institutions)WebContext.getAttribute(WebConstants.CURRENT_INST);
	        
	        if(this.applicationConfig.getLoginConfig().isCaptcha()) {
	        	captchaValid(loginCredential.getState(),loginCredential.getCaptcha());
	        	
	        }else if(!inst.getCaptcha().equalsIgnoreCase("NONE")) {
	        	
	        	captchaValid(loginCredential.getState(),loginCredential.getCaptcha());
	        }
	
	        emptyPasswordValid(loginCredential.getPassword());
	
	        emptyUsernameValid(loginCredential.getUsername());
	
	        UserInfo userInfo =  loadUserInfo(loginCredential.getUsername(),loginCredential.getPassword());
	
	        statusValid(loginCredential , userInfo);
	        
	        //Validate PasswordPolicy
	        authenticationRealm.getPasswordPolicyValidator().passwordPolicyValid(userInfo);
	             
	        //Match password 
	        authenticationRealm.passwordMatches(userInfo, loginCredential.getPassword());

	        //apply PasswordSetType and resetBadPasswordCount
	        authenticationRealm.getPasswordPolicyValidator().applyPasswordPolicy(userInfo);
	        
	        authenticationToken = createOnlineTicket(loginCredential,userInfo);
	        // user authenticated
	        _logger.debug("'{}' authenticated successfully by {}.", 
	        		loginCredential.getPrincipal(), getProviderName());
	        
	        authenticationRealm.insertLoginHistory(userInfo, 
							        				ConstsLoginType.LOCAL, 
									                "", 
									                "xe00000004", 
									                WebConstants.LOGIN_RESULT.SUCCESS);
        } catch (AuthenticationException e) {
            _logger.error("Failed to authenticate user {} via {}: {}",
                    new Object[] {  loginCredential.getPrincipal(),
                                    getProviderName(),
                                    e.getMessage() });
            WebContext.setAttribute(
                    WebConstants.LOGIN_ERROR_SESSION_MESSAGE, e.getMessage());
        } catch (Exception e) {
            _logger.error("Login error Unexpected exception in {} authentication:\n{}" ,
                            getProviderName(), e.getMessage());
        }
       
        return  authenticationToken;
    }
    
    /**
     * captcha validate .
     * 
     * @param authType String
     * @param captcha String
     * @throws ParseException 
     */
    protected void captchaValid(String state ,String captcha) throws ParseException {
        // for basic
    	if(!authTokenService.validateCaptcha(state,captcha)) {
    		throw new BadCredentialsException(WebContext.getI18nValue("login.error.captcha"));
    	}        
    }
}
