package com.wy.test.authentication.provider.authn.support.kerberos;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import dream.flying.flower.framework.core.json.JsonHelpers;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class RemoteKerberosService implements KerberosService {

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
		log.debug("" + userDomainUrlList);
		String userDomainUrlJson = JsonHelpers.toString(userDomainUrlList);
		log.debug("userDomain Url Json " + userDomainUrlJson);
		return userDomainUrlJson;
	}
}