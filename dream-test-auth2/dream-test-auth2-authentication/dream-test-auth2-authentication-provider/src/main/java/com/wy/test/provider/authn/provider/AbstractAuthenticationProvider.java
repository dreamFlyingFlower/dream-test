package com.wy.test.provider.authn.provider;

import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.web.authentication.WebAuthenticationDetails;

import com.wy.test.configuration.ApplicationConfig;
import com.wy.test.constants.ConstsLoginType;
import com.wy.test.constants.ConstsStatus;
import com.wy.test.core.authn.LoginCredential;
import com.wy.test.core.authn.SignPrincipal;
import com.wy.test.core.authn.jwt.AuthTokenService;
import com.wy.test.core.authn.session.Session;
import com.wy.test.core.authn.session.SessionManager;
import com.wy.test.core.authn.web.AuthorizationUtils;
import com.wy.test.entity.UserInfo;
import com.wy.test.otp.password.onetimepwd.AbstractOtpAuthn;
import com.wy.test.otp.password.onetimepwd.MailOtpAuthnService;
import com.wy.test.provider.authn.realm.AbstractAuthenticationRealm;
import com.wy.test.web.WebConstants;
import com.wy.test.web.WebContext;
/**
 * login Authentication abstract class.
 * 
 * @author Crystal.Sea
 *
 */
public abstract class AbstractAuthenticationProvider {
    private static final Logger _logger = 
            LoggerFactory.getLogger(AbstractAuthenticationProvider.class);

    public static String PROVIDER_SUFFIX = "AuthenticationProvider";
    
    public class AuthType{
    	public final static String NORMAL 	= "normal";
    	public final static String TFA 		= "tfa";
    	public final static String MOBILE 	= "mobile";
    	public final static String TRUSTED 	= "trusted";
    }
    
    protected ApplicationConfig applicationConfig;

    protected AbstractAuthenticationRealm authenticationRealm;

    protected AbstractOtpAuthn tfaOtpAuthn;
    
    protected MailOtpAuthnService otpAuthnService;

    protected SessionManager sessionManager;
    
    protected AuthTokenService authTokenService;
    
    public static  ArrayList<GrantedAuthority> grantedAdministratorsAuthoritys = new ArrayList<GrantedAuthority>();
    
    static {
        grantedAdministratorsAuthoritys.add(new SimpleGrantedAuthority("ROLE_ADMINISTRATORS"));
    }

    public abstract String getProviderName();

    public abstract Authentication doAuthenticate(LoginCredential authentication);
    
    @SuppressWarnings("rawtypes")
    public boolean supports(Class authentication) {
        return (UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication));
    }

    public Authentication authenticate(LoginCredential authentication){
    	return null;
    }
    
    public Authentication authenticate(LoginCredential authentication,boolean trusted) {
    	return null;
    }
    
    /**
     * createOnlineSession 
     * @param credential
     * @param userInfo
     * @return
     */
    public UsernamePasswordAuthenticationToken createOnlineTicket(LoginCredential credential,UserInfo userInfo) {
        //create session
        Session session = new Session();

        //set session with principal
        SignPrincipal principal = new SignPrincipal(userInfo,session);

        ArrayList<GrantedAuthority> grantedAuthoritys = authenticationRealm.grantAuthority(userInfo);
        principal.setAuthenticated(true);
        
        for(GrantedAuthority administratorsAuthority : grantedAdministratorsAuthoritys) {
            if(grantedAuthoritys.contains(administratorsAuthority)) {
            	principal.setRoleAdministrators(true);
                _logger.trace("ROLE ADMINISTRATORS Authentication .");
            }
        }
        _logger.debug("Granted Authority {}" , grantedAuthoritys);
        
        principal.setGrantedAuthorityApps(authenticationRealm.queryAuthorizedApps(grantedAuthoritys));
        
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(
                		principal, 
                        "PASSWORD", 
                        grantedAuthoritys
                );
        
        authenticationToken.setDetails(
                new WebAuthenticationDetails(WebContext.getRequest()));
        
        /*
         *  put Authentication to current session context
         */
        session.setAuthentication(authenticationToken);
        
        //create session
        this.sessionManager.create(session.getId(), session);
        
        //set Authentication to http session
        AuthorizationUtils.setAuthentication(authenticationToken);
     
        return authenticationToken;
    }
    
    /**
     * login user by j_username and j_cname first query user by j_cname if first
     * step userinfo is null,query user from system.
     * 
     * @param username String
     * @param password String
     * @return
     */
    public UserInfo loadUserInfo(String username, String password) {
        UserInfo userInfo = authenticationRealm.loadUserInfo(username, password);

        if (userInfo != null) {
            if (userInfo.getUserType() == "SYSTEM") {
                _logger.debug("SYSTEM User Login. ");
            } else {
                _logger.debug("User Login. ");
            }
            
        }

        return userInfo;
    }

    /**
     * check input password empty.
     * 
     * @param password String
     * @return
     */
    protected boolean emptyPasswordValid(String password) {
        if (null == password || "".equals(password)) {
            throw new BadCredentialsException(WebContext.getI18nValue("login.error.password.null"));
        }
        return true;
    }

    /**
     * check input username or password empty.
     * 
     * @param email String
     * @return
     */
    protected boolean emptyEmailValid(String email) {
        if (null == email || "".equals(email)) {
            throw new BadCredentialsException("login.error.email.null");
        }
        return true;
    }

    /**
     * check input username empty.
     * 
     * @param username String
     * @return
     */
    protected boolean emptyUsernameValid(String username) {
        if (null == username || "".equals(username)) {
            throw new BadCredentialsException(WebContext.getI18nValue("login.error.username.null"));
        }
        return true;
    }

    protected boolean statusValid(LoginCredential loginCredential , UserInfo userInfo) {
        if (null == userInfo) {
            String i18nMessage = WebContext.getI18nValue("login.error.username");
            _logger.debug("login user  " + loginCredential.getUsername() + " not in this System ." + i18nMessage);
            UserInfo loginUser = new UserInfo(loginCredential.getUsername());
            loginUser.setId(loginUser.generateId());
            loginUser.setUsername(loginCredential.getUsername());
            loginUser.setDisplayName("not exist");
            loginUser.setLoginCount(0);
            authenticationRealm.insertLoginHistory(
            			loginUser, 
            			ConstsLoginType.LOCAL, 
            			"",
            			i18nMessage,
            			WebConstants.LOGIN_RESULT.USER_NOT_EXIST);
            throw new BadCredentialsException(i18nMessage);
        }else {
        	if(userInfo.getIsLocked()==ConstsStatus.LOCK) {
        		authenticationRealm.insertLoginHistory( 
        				userInfo, 
                        loginCredential.getAuthType(), 
                        loginCredential.getProvider(), 
                        loginCredential.getCode(), 
                        WebConstants.LOGIN_RESULT.USER_LOCKED
                    );
        	}else if(userInfo.getStatus()!=ConstsStatus.ACTIVE) {
        		authenticationRealm.insertLoginHistory( 
        				userInfo, 
                        loginCredential.getAuthType(), 
                        loginCredential.getProvider(), 
                        loginCredential.getCode(), 
                        WebConstants.LOGIN_RESULT.USER_INACTIVE
                    );
        	}
        }
        return true;
    }

}
