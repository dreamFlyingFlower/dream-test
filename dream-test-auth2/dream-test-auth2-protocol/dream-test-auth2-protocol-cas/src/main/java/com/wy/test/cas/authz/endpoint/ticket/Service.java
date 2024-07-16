package com.wy.test.cas.authz.endpoint.ticket;

/**
 * Marker interface for Services. Services are generally either remote
 * applications utilizing CAS or applications that principals wish to gain
 * access to. In most cases this will be some form of web application.
 *
 *        <p>
 *        This is a published and supported CAS Server 3 API.
 *        </p>
 */
public interface Service {

	String getId();

	boolean matches(Service service);
}
