package com.wy.test.web.authorize.endpoint.response;

import com.wy.test.protocol.cas.endpoint.response.ProxyServiceResponseBuilder;
import com.wy.test.protocol.cas.endpoint.response.ServiceResponseBuilder;

import dream.flying.flower.framework.core.pretty.strategy.JsonPretty;
import dream.flying.flower.framework.core.pretty.strategy.XmlPretty;


public class ServiceResponseBuilderTest {

	public ServiceResponseBuilderTest() {
	}

	public static void main(String[] args) throws Exception {
		ServiceResponseBuilder srbJson=new ServiceResponseBuilder();
		srbJson.success()
			.setUser("shi")
			.setAttribute("bbb", "bbb")
			.setAttribute("aaa", "1111")
			.setAttribute("aaa", "222")
			.setProxy("https://proxy1/pgtUrl")
			.setProxy("https://proxy2/pgtUrl")
			.setFormat("json");
		
		System.out.println(srbJson.serviceResponseBuilder());
		System.out.println(new JsonPretty().format(srbJson.serviceResponseBuilder()));
		
		ServiceResponseBuilder srbXml=new ServiceResponseBuilder();
		srbXml.success()
		.setUser("shi")
		.setAttribute("bbb", "bbb")
		.setAttribute("aaa", "1111")
		.setAttribute("aaa", "222")
		.setProxy("https://proxy1/pgtUrl")
		.setProxy("https://proxy2/pgtUrl");
		System.out.println(new XmlPretty().format(srbXml.serviceResponseBuilder()));
		
		
		ProxyServiceResponseBuilder psrbXml=new ProxyServiceResponseBuilder();
		psrbXml.success()
		.setUser("shi")
		.setTicket("PT-asdf-JESPjdnJjNjNmMUyTtGFjK");
		System.out.println(new XmlPretty().format(psrbXml.serviceResponseBuilder()));
	}

}
