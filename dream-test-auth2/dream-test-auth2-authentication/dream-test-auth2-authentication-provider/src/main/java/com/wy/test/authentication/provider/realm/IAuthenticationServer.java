package com.wy.test.authentication.provider.realm;

/**
 * IAuthenticationServer .
 * 
 */
public interface IAuthenticationServer {

	public boolean authenticate(String username, String password);

	public boolean isMapping();
}
