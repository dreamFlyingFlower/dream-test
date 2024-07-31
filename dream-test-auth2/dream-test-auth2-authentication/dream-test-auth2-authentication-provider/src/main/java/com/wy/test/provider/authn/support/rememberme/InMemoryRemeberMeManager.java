package com.wy.test.provider.authn.support.rememberme;

import java.util.concurrent.TimeUnit;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.wy.test.core.constants.ConstTimeInterval;

public class InMemoryRemeberMeManager extends AbstractRemeberMeManager {

	protected static final Cache<String, RemeberMe> remeberMeStore =
			Caffeine.newBuilder().expireAfterWrite(ConstTimeInterval.TWO_WEEK, TimeUnit.SECONDS).build();

	@Override
	public void save(RemeberMe remeberMe) {
		remeberMeStore.put(remeberMe.getUsername(), remeberMe);
	}

	@Override
	public void update(RemeberMe remeberMe) {
		remeberMeStore.put(remeberMe.getUsername(), remeberMe);
	}

	@Override
	public RemeberMe read(RemeberMe remeberMe) {
		return remeberMeStore.getIfPresent(remeberMe.getUsername());
	}

	@Override
	public void remove(String username) {
		remeberMeStore.invalidate(username);
	}

}
