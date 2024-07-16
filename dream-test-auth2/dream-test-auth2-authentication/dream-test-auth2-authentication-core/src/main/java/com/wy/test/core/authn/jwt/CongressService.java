package com.wy.test.core.authn.jwt;

public interface CongressService {

	public void store(String congress, AuthJwt authJwt);

	public AuthJwt consume(String congress);

	public AuthJwt remove(String congress);

	public AuthJwt get(String congress);

}
