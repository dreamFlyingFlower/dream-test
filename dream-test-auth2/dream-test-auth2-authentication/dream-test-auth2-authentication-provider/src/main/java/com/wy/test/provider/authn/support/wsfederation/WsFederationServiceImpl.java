package com.wy.test.provider.authn.support.wsfederation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WsFederationServiceImpl implements WsFederationService {

	final static Logger _logger = LoggerFactory.getLogger(WsFederationServiceImpl.class);

	private WsFederationConfiguration wsFederationConfiguration;

	public void setWsFederationConfiguration(WsFederationConfiguration wsFederationConfiguration) {
		this.wsFederationConfiguration = wsFederationConfiguration;
	}

	@Override
	public WsFederationConfiguration getWsFederationConfiguration() {
		return wsFederationConfiguration;
	}

}