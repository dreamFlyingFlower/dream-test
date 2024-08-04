package com.wy.test.core.entity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Properties;

import dream.flying.flower.framework.core.json.JsonHelpers;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ExtraAttrs {

	ArrayList<ExtraAttr> extraAttrs;

	public ExtraAttrs() {
		super();
	}

	public ExtraAttrs(String arrayJsonString) {
		String extraAttrsJsonString = "{\"extraAttrs\":" + arrayJsonString + "}";
		log.debug("Extra Attrs Json String " + extraAttrsJsonString);
		ExtraAttrs extraAttrs = JsonHelpers.read(extraAttrsJsonString, ExtraAttrs.class);
		this.extraAttrs = extraAttrs.getExtraAttrs();
	}

	public void put(String attr, String value) {
		if (extraAttrs == null) {
			extraAttrs = new ArrayList<ExtraAttr>();
		}
		this.extraAttrs.add(new ExtraAttr(attr, value));
	}

	public void put(String attr, String type, String value) {
		if (extraAttrs == null) {
			extraAttrs = new ArrayList<ExtraAttr>();
		}
		this.extraAttrs.add(new ExtraAttr(attr, type, value));
	}

	public String get(String attr) {
		String value = null;
		if (extraAttrs != null && extraAttrs.size() != 0) {
			for (ExtraAttr extraAttr : extraAttrs) {
				if (extraAttr.getAttr().equals(attr)) {
					value = extraAttr.getValue();
				}
			}
		}
		return value;
	}

	public String toJsonString() {
		String jsonString = JsonHelpers.toString(extraAttrs);
		log.debug("jsonString " + jsonString);
		return jsonString;
	}

	public HashMap<String, String> toJsonHashMap() {
		HashMap<String, String> extraAttrsHashMap = new HashMap<String, String>();
		for (ExtraAttr extraAttr : extraAttrs) {
			extraAttrsHashMap.put(extraAttr.getAttr(), extraAttr.getValue());
		}
		log.debug("extraAttrs HashMap " + extraAttrsHashMap);
		return extraAttrsHashMap;
	}

	public Properties toProperties() {
		Properties properties = new Properties();
		for (ExtraAttr extraAttr : extraAttrs) {
			properties.put(extraAttr.getAttr(), extraAttr.getValue());
		}
		log.debug("extraAttrs HashMap " + properties);
		return properties;
	}

	public ArrayList<ExtraAttr> getExtraAttrs() {
		return extraAttrs;
	}

	public void setExtraAttrs(ArrayList<ExtraAttr> extraAttrs) {
		this.extraAttrs = extraAttrs;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("ExtraAttrs [extraAttrs=");
		builder.append(extraAttrs);
		builder.append("]");
		return builder.toString();
	}
}