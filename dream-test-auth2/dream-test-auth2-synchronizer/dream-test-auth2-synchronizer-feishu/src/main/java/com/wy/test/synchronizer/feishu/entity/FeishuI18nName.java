package com.wy.test.synchronizer.feishu.entity;

public class FeishuI18nName {

	String zh_cn;

	String ja_jp;

	String en_us;

	public FeishuI18nName() {
		super();
	}

	public String getZh_cn() {
		return zh_cn;
	}

	public void setZh_cn(String zh_cn) {
		this.zh_cn = zh_cn;
	}

	public String getJa_jp() {
		return ja_jp;
	}

	public void setJa_jp(String ja_jp) {
		this.ja_jp = ja_jp;
	}

	public String getEn_us() {
		return en_us;
	}

	public void setEn_us(String en_us) {
		this.en_us = en_us;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("FeishuI18nName [zh_cn=");
		builder.append(zh_cn);
		builder.append(", ja_jp=");
		builder.append(ja_jp);
		builder.append(", en_us=");
		builder.append(en_us);
		builder.append("]");
		return builder.toString();
	}

}
