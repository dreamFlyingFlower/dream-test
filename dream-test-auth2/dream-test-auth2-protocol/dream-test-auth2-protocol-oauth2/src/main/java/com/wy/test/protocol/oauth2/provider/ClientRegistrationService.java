package com.wy.test.protocol.oauth2.provider;

import java.util.List;

import org.springframework.security.oauth2.provider.ClientAlreadyExistsException;
import org.springframework.security.oauth2.provider.NoSuchClientException;

import com.wy.test.core.entity.oauth2.ClientDetails;

/**
 * Interface for client registration, handling add, update and remove of
 * {@link ClientDetails} from an Authorization Server.
 * 
 * @author Dave Syer
 * 
 */
public interface ClientRegistrationService {

	void addClientDetails(ClientDetails clientDetails) throws ClientAlreadyExistsException;

	void updateClientDetails(ClientDetails clientDetails) throws NoSuchClientException;

	void updateClientSecret(String clientId, String secret) throws NoSuchClientException;

	void removeClientDetails(String clientId) throws NoSuchClientException;

	List<ClientDetails> listClientDetails();

}
