/*
 * Copyright [2022] [MaxKey of copyright http://www.maxkey.top]
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
 

package com.wy.test.web.config.contorller;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.wy.test.core.authn.annotation.CurrentUser;
import com.wy.test.crypto.password.PasswordReciprocal;
import com.wy.test.entity.LdapContext;
import com.wy.test.entity.Message;
import com.wy.test.entity.UserInfo;
import com.wy.test.persistence.ldap.ActiveDirectoryUtils;
import com.wy.test.persistence.ldap.LdapUtils;
import com.wy.test.persistence.service.LdapContextService;

@Controller
@RequestMapping(value={"/config/ldapcontext"})
public class LdapContextController {
	final static Logger _logger = LoggerFactory.getLogger(LdapContextController.class);
	
	@Autowired
	private LdapContextService ldapContextService;

	@RequestMapping(value={"/get"}, produces = {MediaType.APPLICATION_JSON_VALUE})
	public ResponseEntity<?> get(@CurrentUser UserInfo currentUser){
		LdapContext ldapContext = ldapContextService.get(currentUser.getInstId());
		if(ldapContext != null && StringUtils.isNoneBlank(ldapContext.getCredentials())) {
			ldapContext.setCredentials(PasswordReciprocal.getInstance().decoder(ldapContext.getCredentials()));
		}
		return new Message<LdapContext>(ldapContext).buildResponse();
	}

	@RequestMapping(value={"/update"})
	@ResponseBody
	public ResponseEntity<?> update( @RequestBody LdapContext ldapContext,@CurrentUser UserInfo currentUser,BindingResult result) {
		_logger.debug("update ldapContext : "+ldapContext);
		ldapContext.setCredentials(PasswordReciprocal.getInstance().encode(ldapContext.getCredentials()));
		ldapContext.setInstId(currentUser.getInstId());
		boolean updateResult = false;
		if(StringUtils.isBlank(ldapContext.getId())) {
			ldapContext.setId(ldapContext.getInstId());
			updateResult = ldapContextService.insert(ldapContext);
		}else {
			updateResult = ldapContextService.update(ldapContext);
		}
		if(updateResult) {
			return new Message<LdapContext>(Message.SUCCESS).buildResponse();
		} else {
			return new Message<LdapContext>(Message.FAIL).buildResponse();
		}
	}
	
	
	@RequestMapping(value={"/test"}, produces = {MediaType.APPLICATION_JSON_VALUE})
	public ResponseEntity<?> test(@CurrentUser UserInfo currentUser){
		LdapContext ldapContext = ldapContextService.get(currentUser.getInstId());
		if(ldapContext != null && StringUtils.isNoneBlank(ldapContext.getCredentials())) {
			ldapContext.setCredentials(PasswordReciprocal.getInstance().decoder(ldapContext.getCredentials()));
		}
		
		LdapUtils ldapUtils = null;
		if(ldapContext.getProduct().equalsIgnoreCase(LdapUtils.Product.ActiveDirectory)) {
			ldapUtils = new ActiveDirectoryUtils(
					ldapContext.getProviderUrl(),
					ldapContext.getPrincipal(),
					ldapContext.getCredentials(),
					ldapContext.getBasedn(),
					ldapContext.getMsadDomain());
		}else if(ldapContext.getProduct().equalsIgnoreCase(LdapUtils.Product.OpenLDAP)) {
			ldapUtils = new LdapUtils(
					ldapContext.getProviderUrl(),
					ldapContext.getPrincipal(),
			        ldapContext.getCredentials(),
			        ldapContext.getBasedn());
		}else if(ldapContext.getProduct().equalsIgnoreCase(LdapUtils.Product.StandardLDAP)) {
			ldapUtils = new LdapUtils(
					ldapContext.getProviderUrl(),
					ldapContext.getPrincipal(),
			        ldapContext.getCredentials(),
			        ldapContext.getBasedn());
		}
				
		if(ldapUtils.openConnection() != null) {
			ldapUtils.close();
			return new Message<LdapContext>(Message.SUCCESS).buildResponse();
		}else {
			return new Message<LdapContext>(Message.FAIL).buildResponse();
		}
	}
}
