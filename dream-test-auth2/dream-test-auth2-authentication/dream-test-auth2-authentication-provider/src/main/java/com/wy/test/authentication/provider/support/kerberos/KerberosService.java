package com.wy.test.authentication.provider.support.kerberos;

import java.util.List;

public interface KerberosService {

	public List<KerberosProxy> getKerberosProxys();

	public String buildKerberosProxys();

}
