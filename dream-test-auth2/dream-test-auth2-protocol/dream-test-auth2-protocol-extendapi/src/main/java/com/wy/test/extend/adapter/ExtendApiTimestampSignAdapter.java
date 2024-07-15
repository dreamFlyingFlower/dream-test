package com.wy.test.extend.adapter;

import java.time.Instant;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.ModelAndView;

import com.wy.test.authorize.endpoint.adapter.AbstractAuthorizeAdapter;
import com.wy.test.crypto.DigestUtils;
import com.wy.test.entity.Accounts;
import com.wy.test.entity.ExtraAttrs;
import com.wy.test.entity.apps.Apps;
/**
 * 
 * http://target.maxkey.org/demo/login?code=maxkey&time=timestamp&token=token
 * login url http://target.maxkey.org/demo/login?code=%s&timestamp=%s&token=%s
 * 
 * $code  		= 'maxkey';
 * $key   		= 'a5246932b0f371263c252384076cd3f0';
 * $timestamp  	= '1557034496';
 * $token 		= md5($code . $key . $time);
 * 
 * @author shimingxy
 *
 */
public class ExtendApiTimestampSignAdapter extends AbstractAuthorizeAdapter {
	final static Logger _logger = LoggerFactory.getLogger(ExtendApiTimestampSignAdapter.class);
	
	Accounts account;
	
	@Override
	public Object generateInfo() {
		return null;
	}

	@Override
	public Object encrypt(Object data, String algorithmKey, String algorithm) {
		return null;
	}

	@Override
	public ModelAndView authorize(ModelAndView modelAndView) {
		Apps details=(Apps)app;
		
		String code = details.getPrincipal();
		String key   = details.getCredentials();
		String timestamp  = ""+Instant.now().getEpochSecond();
		String token =DigestUtils.md5Hex(code+key+timestamp);
		
		//extraAttrs from Applications
		ExtraAttrs extraAttrs=null;
		if(details.getIsExtendAttr()==1){
			extraAttrs=new ExtraAttrs(details.getExtendAttr());
			if(extraAttrs.get("sign") == null || extraAttrs.get("sign").equalsIgnoreCase("md5")) {
				
			}else if(extraAttrs.get("sign").equalsIgnoreCase("sha") || extraAttrs.get("sign").equalsIgnoreCase("sha1")) {
				token =DigestUtils.shaHex(code+key+timestamp);
			}else if(extraAttrs.get("sign").equalsIgnoreCase("sha256")) {
				token =DigestUtils.sha256Hex(code+key+timestamp);
			}else if(extraAttrs.get("sign").equalsIgnoreCase("sha384")) {
				token =DigestUtils.sha384Hex(code+key+timestamp);
			}else if(extraAttrs.get("sign").equalsIgnoreCase("sha512")) {
				token =DigestUtils.sha512Hex(code+key+timestamp);
			}
		}
		
		_logger.debug(""+token);
		String account = userInfo.getUsername();
		
		String redirect_uri = String.format(details.getLoginUrl(),account,code,timestamp,token);

		_logger.debug("redirect_uri : "+redirect_uri);
		
        modelAndView.addObject("redirect_uri", redirect_uri);
        
        return modelAndView;
        
	}

}