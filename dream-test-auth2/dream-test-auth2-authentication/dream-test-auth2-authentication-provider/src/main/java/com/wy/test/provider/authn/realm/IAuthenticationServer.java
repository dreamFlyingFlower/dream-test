package com.wy.test.provider.authn.realm;

/**
 * IAuthenticationServer .
 * 
 */
public interface IAuthenticationServer {

	public boolean authenticate(String username, String password);

	public boolean isMapping();
}
