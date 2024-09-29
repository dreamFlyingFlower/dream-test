package com.wy.test.authentication.core.entity;

import java.util.Collection;
import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import com.wy.test.core.constant.ConstAuthWeb;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoginCredential implements Authentication {

	private static final long serialVersionUID = 3125709257481600320L;

	private String congress;

	private String username;

	private String password;

	private String state;

	private String captcha;

	private String otpCaptcha;

	private String remeberMe;

	@Schema(description = "认证类型")
	private String authType;

	private String jwtToken;

	private String onlineTicket;

	private String provider;

	private String code;

	@Builder.Default
	private String message = ConstAuthWeb.LOGIN_RESULT.SUCCESS;

	private String instId;

	private List<GrantedAuthority> grantedAuthority;

	private boolean authenticated;

	private boolean roleAdministrators;

	private String mobile;

	/**
	 * BasicAuthentication.
	 */
	public LoginCredential(String username, String password, String authType) {
		this.username = username;
		this.password = password;
		this.authType = authType;
	}

	@Override
	public String getName() {
		return "Login Credential";
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return grantedAuthority;
	}

	@Override
	public Object getCredentials() {
		return this.getPassword();
	}

	@Override
	public Object getDetails() {
		return null;
	}

	@Override
	public Object getPrincipal() {
		return this.getUsername();
	}

	@Override
	public boolean isAuthenticated() {
		return authenticated;
	}

	@Override
	public void setAuthenticated(boolean authenticated) throws IllegalArgumentException {
		this.authenticated = authenticated;
	}
}