package com.wy.test.core.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class SocialsProviderLogin implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2672107566766342357L;

	List<SocialsProvider> providers = new ArrayList<SocialsProvider>();

	String qrScan = null;

	public SocialsProviderLogin(List<SocialsProvider> socialSignOnProviders) {
		super();
		this.providers = socialSignOnProviders;
	}

	public String getQrScan() {
		return qrScan;
	}

	public void setQrScan(String qrScan) {
		this.qrScan = qrScan;
	}

	public List<SocialsProvider> getProviders() {
		return providers;
	}

	public void setProviders(List<SocialsProvider> providers) {
		this.providers = providers;
	}
}
