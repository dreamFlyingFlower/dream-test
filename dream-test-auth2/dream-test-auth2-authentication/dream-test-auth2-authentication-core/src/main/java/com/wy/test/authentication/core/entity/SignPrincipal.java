package com.wy.test.authentication.core.entity;

import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.wy.test.authentication.core.session.Session;
import com.wy.test.core.vo.UserVO;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@NoArgsConstructor
public class SignPrincipal implements UserDetails {

	private static final long serialVersionUID = -110742975439268030L;

	UserVO userInfo;

	UserDetails userDetails;

	Session session;

	List<GrantedAuthority> grantedAuthority;

	List<GrantedAuthority> grantedAuthorityApps;

	boolean authenticated;

	boolean roleAdministrators;

	private boolean accountNonExpired;

	private boolean accountNonLocked;

	private boolean credentialsNonExpired;

	private boolean enabled;

	public SignPrincipal(UserVO userInfo) {
		this.userInfo = userInfo;
		this.authenticated = true;
		this.accountNonExpired = true;
		this.accountNonLocked = true;
		this.credentialsNonExpired = true;
		this.enabled = true;
	}

	public SignPrincipal(UserVO userInfo, Session session) {
		this.userInfo = userInfo;
		this.authenticated = true;
		this.accountNonExpired = true;
		this.accountNonLocked = true;
		this.credentialsNonExpired = true;
		this.enabled = true;
		this.session = session;
		this.userInfo.setSessionId(session.getId());
	}

	public SignPrincipal(UserDetails userDetails) {
		this.userDetails = userDetails;
		this.authenticated = true;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return grantedAuthority;
	}

	@Override
	public String getUsername() {
		if (this.userInfo != null) {
			return this.userInfo.getUsername();
		} else {
			return this.userDetails.getUsername();
		}
	}

	@Override
	public String getPassword() {
		if (this.userInfo != null) {
			return this.userInfo.getPassword();
		} else {
			return this.userDetails.getPassword();
		}
	}
}