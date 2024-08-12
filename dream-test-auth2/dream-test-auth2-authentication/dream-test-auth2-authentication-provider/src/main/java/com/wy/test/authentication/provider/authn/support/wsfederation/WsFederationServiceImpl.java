package com.wy.test.authentication.provider.authn.support.wsfederation;

public class WsFederationServiceImpl implements WsFederationService {

	private WsFederationConfiguration wsFederationConfiguration;

	public void setWsFederationConfiguration(WsFederationConfiguration wsFederationConfiguration) {
		this.wsFederationConfiguration = wsFederationConfiguration;
	}

	@Override
	public WsFederationConfiguration getWsFederationConfiguration() {
		return wsFederationConfiguration;
	}
}