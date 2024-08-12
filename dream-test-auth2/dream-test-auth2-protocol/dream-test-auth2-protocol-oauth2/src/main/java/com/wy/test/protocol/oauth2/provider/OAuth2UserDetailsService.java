package com.wy.test.protocol.oauth2.provider;

import java.util.ArrayList;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.provider.NoSuchClientException;

import com.wy.test.authentication.core.authn.SignPrincipal;
import com.wy.test.authentication.core.authn.session.Session;
import com.wy.test.authentication.provider.authn.provider.AbstractAuthenticationProvider;
import com.wy.test.core.persistence.repository.LoginRepository;
import com.wy.test.core.vo.UserVO;
import com.wy.test.core.web.WebConstants;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class OAuth2UserDetailsService implements UserDetailsService {

	LoginRepository loginRepository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		UserVO userInfo;
		try {
			userInfo = loginRepository.find(username, "");
		} catch (NoSuchClientException e) {
			throw new UsernameNotFoundException(e.getMessage(), e);
		}

		String onlineTickitId =
				WebConstants.ONLINE_TICKET_PREFIX + "-" + java.util.UUID.randomUUID().toString().toLowerCase();

		SignPrincipal principal = new SignPrincipal(userInfo);
		Session onlineTicket = new Session(onlineTickitId);
		// set OnlineTicket
		principal.setSession(onlineTicket);

		ArrayList<GrantedAuthority> grantedAuthoritys = loginRepository.grantAuthority(userInfo);
		principal.setAuthenticated(true);

		for (GrantedAuthority administratorsAuthority : AbstractAuthenticationProvider.grantedAdministratorsAuthoritys) {
			if (grantedAuthoritys.contains(administratorsAuthority)) {
				principal.setRoleAdministrators(true);
				log.trace("ROLE ADMINISTRATORS Authentication .");
			}
		}
		log.debug("Granted Authority " + grantedAuthoritys);

		principal.setGrantedAuthorityApps(grantedAuthoritys);

		return principal;
	}

	public void setLoginRepository(LoginRepository loginRepository) {
		this.loginRepository = loginRepository;
	}

}