package com.wy.test.authentication.core.authn.jwt;

import java.util.concurrent.TimeUnit;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;

public class InMemoryCongressService implements CongressService {

	protected static Cache<String, AuthJwt> congressStore =
			Caffeine.newBuilder().expireAfterWrite(3, TimeUnit.MINUTES).maximumSize(200000).build();

	public InMemoryCongressService() {
		super();
	}

	@Override
	public void store(String congress, AuthJwt authJwt) {
		congressStore.put(congress, authJwt);
	}

	@Override
	public AuthJwt remove(String congress) {
		AuthJwt authJwt = congressStore.getIfPresent(congress);
		congressStore.invalidate(congress);
		return authJwt;
	}

	@Override
	public AuthJwt get(String congress) {
		AuthJwt authJwt = congressStore.getIfPresent(congress);
		return authJwt;
	}

	@Override
	public AuthJwt consume(String congress) {
		AuthJwt authJwt = congressStore.getIfPresent(congress);
		congressStore.invalidate(congress);
		return authJwt;
	}

}
