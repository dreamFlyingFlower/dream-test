package com.wy.test.authentication.provider.authn.support.kerberos;

import java.util.List;

public interface KerberosService {

	public List<KerberosProxy> getKerberosProxys();

	public String buildKerberosProxys();

}
