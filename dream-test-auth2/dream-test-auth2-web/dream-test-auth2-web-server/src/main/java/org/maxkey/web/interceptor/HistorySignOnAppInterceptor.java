/*
 * Copyright [2020] [MaxKey of copyright http://www.maxkey.top]
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
 

package org.maxkey.web.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.maxkey.authn.SignPrincipal;
import org.maxkey.authn.web.AuthorizationUtils;
import org.maxkey.entity.HistoryLoginApps;
import org.maxkey.entity.UserInfo;
import org.maxkey.entity.apps.Apps;
import org.maxkey.persistence.service.AppsService;
import org.maxkey.persistence.service.HistoryLoginAppsService;
import org.maxkey.web.WebConstants;
import org.maxkey.web.WebContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.AsyncHandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

@Component
public class HistorySignOnAppInterceptor  implements AsyncHandlerInterceptor  {
    private static final Logger _logger = LoggerFactory.getLogger(HistorySignOnAppInterceptor.class);

    @Autowired
    HistoryLoginAppsService historyLoginAppsService;

    @Autowired
    protected AppsService appsService;

    /**
     * postHandle .
     * @see org.springframework.web.servlet.handler.HandlerInterceptorAdapter#preHandle(
     *          javax.servlet.http.HttpServletRequest, 
     *          javax.servlet.http.HttpServletResponse, java.lang.Object)
     */
    public void postHandle(HttpServletRequest request,
            HttpServletResponse response,
            Object handler,ModelAndView modelAndView) throws Exception {
        _logger.debug("postHandle");
       
        final Apps app = (Apps)WebContext.getAttribute(WebConstants.AUTHORIZE_SIGN_ON_APP);
        
        SignPrincipal principal = AuthorizationUtils.getPrincipal();
        if(principal != null && app !=null) {
        	final UserInfo userInfo = principal.getUserInfo();
        	String sessionId = principal.getSession().getId();
        	 _logger.debug("sessionId : " + sessionId + " ,appId : " + app.getId());
             HistoryLoginApps historyLoginApps = new HistoryLoginApps();
             historyLoginApps.setAppId(app.getId());
             historyLoginApps.setSessionId(sessionId);
             historyLoginApps.setAppName(app.getAppName());
             historyLoginApps.setUserId(userInfo.getId());
             historyLoginApps.setUsername(userInfo.getUsername());
             historyLoginApps.setDisplayName(userInfo.getDisplayName());
             historyLoginApps.setInstId(userInfo.getInstId());
             historyLoginAppsService.insert(historyLoginApps);
             WebContext.removeAttribute(WebConstants.CURRENT_SINGLESIGNON_URI);
             WebContext.removeAttribute(WebConstants.SINGLE_SIGN_ON_APP_ID);
        }
       
    }
}
