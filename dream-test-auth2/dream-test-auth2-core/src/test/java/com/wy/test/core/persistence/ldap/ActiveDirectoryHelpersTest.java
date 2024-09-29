package com.wy.test.core.persistence.ldap;

import javax.naming.NamingException;
import javax.naming.directory.DirContext;

import com.wy.test.core.ldap.ActiveDirectoryHelpers;

public class ActiveDirectoryHelpersTest {

	public static void main(String[] args) throws Exception {
		String trustStore = "D:/JavaIDE/jdk1.6.0_30/jre/lib/security/cacerts";
		String trustStorePassword = "changeit";
		// ActiveDirectoryUtils activeDirectoryUtils=new
		// ActiveDirectoryUtils("ldap://192.168.0.171:389","administrator","p@ssw0rdp@ssw0rd","DC=kygfcrmtest,DC=com","kygfcrmtest");
		ActiveDirectoryHelpers activeDirectoryUtils = new ActiveDirectoryHelpers("ldaps://msad.connsec.com:636",
				"administrator", "1qaz@WSX", "DC=CONNSEC,DC=com", "CONNSEC");
		// ActiveDirectoryUtils activeDirectoryUtils=new
		// ActiveDirectoryUtils("ldap://msad.connsec.com:389","administrator","1qaz@WSX","DC=CONNSEC,DC=com","CONNSEC");
		activeDirectoryUtils.setTrustStore(trustStore);
		activeDirectoryUtils.setTrustStorePassword(trustStorePassword);
		activeDirectoryUtils.setSsl(true);
		// activeDirectoryUtils.setSsl(false);
		DirContext dirContext = activeDirectoryUtils.openConnection();
		try {
			dirContext.close();
		} catch (NamingException e) {
			e.printStackTrace();
		}
	}
}