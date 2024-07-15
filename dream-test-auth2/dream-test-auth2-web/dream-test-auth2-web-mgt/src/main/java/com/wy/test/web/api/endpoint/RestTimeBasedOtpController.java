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
 

package com.wy.test.web.api.endpoint;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.wy.test.entity.UserInfo;
import com.wy.test.otp.password.onetimepwd.AbstractOtpAuthn;
import com.wy.test.persistence.service.UserInfoService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "基于时间令牌验证 API文档模块")
@Controller
@RequestMapping(value={"/im/api/otp"})
public class RestTimeBasedOtpController {

	@Autowired
    protected AbstractOtpAuthn timeBasedOtpAuthn;
    
    @Autowired
    private UserInfoService userInfoService;
    
    @Operation(summary = "基于时间令牌验证 API文档模块", description = "传递参数username和token",method="GET")
    @ResponseBody
    @RequestMapping(value = "/timebased/validate", method = RequestMethod.GET)
    public boolean getUser(@RequestParam String username,
    							 @RequestParam String token) {
    	
    	UserInfo validUserInfo = userInfoService.findByUsername(username);
    	if(validUserInfo != null) {
    		if(timeBasedOtpAuthn.validate(validUserInfo, token)) {
    			return true;
    		}
    	}
    	
        return false;
    }

 
}
