package com.wy.test.web.authorize.endpoint.cas.ticket.generator;

import com.wy.test.cas.authz.endpoint.ticket.generator.DefaultUniqueTicketIdGenerator;

public class DefaultUniqueTicketIdGeneratorTest {

	public static void main(String[] args) {
		DefaultUniqueTicketIdGenerator t = new DefaultUniqueTicketIdGenerator();
		System.out.println(t.getNewTicketId("ST"));
	}

}
