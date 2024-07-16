package com.wy.test.provider.authn.support.kerberos;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.wy.test.util.JsonUtils;

public class RemoteKerberosService implements KerberosService {

	private static Logger _logger = LoggerFactory.getLogger(RemoteKerberosService.class);

	List<KerberosProxy> kerberosProxys;

	@Override
	public List<KerberosProxy> getKerberosProxys() {
		return kerberosProxys;
	}

	public void setKerberosProxys(List<KerberosProxy> kerberosProxys) {
		this.kerberosProxys = kerberosProxys;
	}

	@Override
	public String buildKerberosProxys() {
		List<Map<String, String>> userDomainUrlList = new ArrayList<Map<String, String>>();
		for (KerberosProxy kerberosProxy : kerberosProxys) {
			Map<String, String> userDomainUrl = new HashMap<String, String>();
			userDomainUrl.put("userDomain", kerberosProxy.getUserdomain());
			userDomainUrl.put("redirectUri", kerberosProxy.getRedirectUri());
			userDomainUrlList.add(userDomainUrl);
		}
		_logger.debug("" + userDomainUrlList);
		String userDomainUrlJson = JsonUtils.toString(userDomainUrlList);
		_logger.debug("userDomain Url Json " + userDomainUrlJson);
		return userDomainUrlJson;
	}
}