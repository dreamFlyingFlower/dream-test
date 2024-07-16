package com.wy.test.provider.authn.support.kerberos;

import java.util.List;

public interface KerberosService {

	public List<KerberosProxy> getKerberosProxys();

	public String buildKerberosProxys();

}
