package com.wy.test.oauth2.provider.client;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.provider.NoSuchClientException;

import com.wy.test.core.entity.oauth2.ClientDetails;
import com.wy.test.core.password.PasswordReciprocal;
import com.wy.test.oauth2.provider.ClientDetailsService;

/**
 * @author Dave Syer
 * 
 */
public class ClientDetailsUserDetailsService implements UserDetailsService {

	private final ClientDetailsService clientDetailsService;

	private PasswordEncoder passwordEncoder;

	public ClientDetailsUserDetailsService(ClientDetailsService clientDetailsService) {
		this.clientDetailsService = clientDetailsService;
	}

	/**
	 * @param passwordEncoder the password encoder to set
	 */
	public void setPasswordEncoder(PasswordEncoder passwordEncoder) {
		this.passwordEncoder = passwordEncoder;
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		ClientDetails clientDetails;
		try {
			clientDetails = clientDetailsService.loadClientByClientId(username, true);
		} catch (NoSuchClientException e) {
			throw new UsernameNotFoundException(e.getMessage(), e);
		}

		String clientSecret = clientDetails.getClientSecret();
		if (clientSecret == null || clientSecret.trim().length() == 0) {
			clientSecret = "";
		} else {
			if (passwordEncoder instanceof PasswordReciprocal) {
				clientSecret = ((PasswordReciprocal) passwordEncoder).decoder(clientSecret);
			}
		}

		return new User(username, clientSecret, clientDetails.getAuthorities());
	}

}
