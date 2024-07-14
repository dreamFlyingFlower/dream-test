package com.wy.test.pretty.impl;

import java.io.StringReader;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;

import com.wy.test.pretty.Pretty;

public class XmlPretty implements Pretty {

	static XmlPretty instance;

	public XmlPretty() {

	}

	public static XmlPretty getInstance() {
		if (null == instance) {
			synchronized (JsonPretty.class) {
				if (instance == null) {
					instance = new XmlPretty();
				}
			}
		}
		return instance;
	}

	@Override
	public String format(String xmlString) {
		try {
			DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
			Document document = documentBuilder.parse(new InputSource(new StringReader(xmlString)));
			return format(document);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public String format(Node node) {
		try {
			return XMLHelper.prettyPrintXML(node);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public String formatln(String source) {
		return LINE_BREAK + format(source);
	}
}