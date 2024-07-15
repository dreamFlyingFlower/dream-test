package com.wy.test.authz.oauth2.provider;

import com.wy.test.entity.apps.oauth2.provider.ClientDetails;

/**
 * A service that provides the details about an OAuth2 client.
 *
 * @author Ryan Heaton
 */
public interface ClientDetailsService {

  /**
   * Load a client by the client id. This method must not return null.
   *
   * @param clientId The client id.
   * @return The client details (never null).
   * @throws ClientRegistrationException If the client account is locked, expired, disabled, or invalid for any other reason.
   */
  ClientDetails loadClientByClientId(String clientId,boolean cached) throws ClientRegistrationException;

}