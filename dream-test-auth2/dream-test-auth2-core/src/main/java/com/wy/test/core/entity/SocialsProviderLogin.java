package com.wy.test.core.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class SocialsProviderLogin implements Serializable {

	private static final long serialVersionUID = -2672107566766342357L;

	List<SocialProviderEntity> providers = new ArrayList<SocialProviderEntity>();

	String qrScan = null;

	public SocialsProviderLogin(List<SocialProviderEntity> socialSignOnProviders) {
		super();
		this.providers = socialSignOnProviders;
	}

	public String getQrScan() {
		return qrScan;
	}

	public void setQrScan(String qrScan) {
		this.qrScan = qrScan;
	}

	public List<SocialProviderEntity> getProviders() {
		return providers;
	}

	public void setProviders(List<SocialProviderEntity> providers) {
		this.providers = providers;
	}
}