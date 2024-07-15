package com.wy.test.web.web.contorller;

import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.wy.test.authorize.singlelogout.DefaultSingleLogout;
import com.wy.test.authorize.singlelogout.LogoutType;
import com.wy.test.authorize.singlelogout.SamlSingleLogout;
import com.wy.test.authorize.singlelogout.SingleLogout;
import com.wy.test.configuration.ApplicationConfig;
import com.wy.test.constants.ConstsProtocols;
import com.wy.test.core.authn.annotation.CurrentUser;
import com.wy.test.core.authn.session.Session;
import com.wy.test.core.authn.session.SessionManager;
import com.wy.test.entity.Message;
import com.wy.test.entity.UserInfo;
import com.wy.test.entity.apps.Apps;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "1-3-单点注销接口文档模块")
@Controller
public class LogoutEndpoint {
	private static Logger _logger = LoggerFactory.getLogger(LogoutEndpoint.class);

	@Autowired 
    ApplicationConfig applicationConfig;
	
	@Autowired
    SessionManager sessionManager;
	
	/**
	 * for front end
	 * @param currentUser
	 * @return ResponseEntity
	 */
	@Operation(summary = "前端注销接口", description = "前端注销接口",method="GET")
	@RequestMapping(value={"/logout"}, produces = {MediaType.APPLICATION_JSON_VALUE})
 	public  ResponseEntity<?> logout(@CurrentUser UserInfo currentUser){
		//if logined in have onlineTicket ,need remove or logout back
		String sessionId = currentUser.getSessionId();
 		Session session = sessionManager.get(sessionId);
 		if(session != null) {
 			_logger.debug("/logout frontend clean Session id {}",session.getId());
	 		Set<Entry<String, Apps>> entrySet = session.getAuthorizedApps().entrySet();
	 
	        Iterator<Entry<String, Apps>> iterator = entrySet.iterator();
	        while (iterator.hasNext()) {
	            Entry<String, Apps> mapEntry = iterator.next();
	            _logger.debug("App Id : "+ mapEntry.getKey()+ " , " +mapEntry.getValue());
	            if( mapEntry.getValue().getLogoutType() == LogoutType.BACK_CHANNEL){
	                SingleLogout singleLogout;
	                if(mapEntry.getValue().getProtocol().equalsIgnoreCase(ConstsProtocols.CAS)) {
	                    singleLogout =new SamlSingleLogout();
	                }else {
	                    singleLogout = new DefaultSingleLogout();
	                }
	                singleLogout.sendRequest(session.getAuthentication(), mapEntry.getValue());
	            }
	        }
	        //terminate session
	        sessionManager.terminate(
	        		session.getId(), 
	        		currentUser.getId(),
	        		currentUser.getUsername());
 		}
 		return new Message<String>().buildResponse();
 	}
	
	@Operation(summary = "单点注销接口", description = "redirect_uri跳转地址",method="GET")
	@RequestMapping(value={"/force/logout"})
 	public ModelAndView forceLogout(
 				HttpServletRequest request,
 				@RequestParam(value = "redirect_uri",required = false) String redirect_uri
 				){
		//invalidate http session
		_logger.debug("/force/logout http Session id {}",request.getSession().getId());
		request.getSession().invalidate();
		StringBuffer logoutUrl = new StringBuffer("");
		logoutUrl.append(applicationConfig.getFrontendUri()).append("/#/passport/logout");
		if(StringUtils.isNotBlank(redirect_uri)) {
			logoutUrl.append("?")
				.append("redirect_uri=").append(redirect_uri);
		}
		ModelAndView modelAndView=new ModelAndView("redirect");
		modelAndView.addObject("redirect_uri", logoutUrl);
		return modelAndView;
 	}
}
