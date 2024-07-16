package com.wy.test.constants;

import org.springframework.security.core.authority.SimpleGrantedAuthority;

/**
 * PROTOCOLS.
 * 
 */
public final class ConstsRoles {

	public static final SimpleGrantedAuthority ROLE_ADMINISTRATORS = new SimpleGrantedAuthority("ROLE_ADMINISTRATORS");

	public static final SimpleGrantedAuthority ROLE_MANAGERS = new SimpleGrantedAuthority("ROLE_MANAGERS");

	public static final SimpleGrantedAuthority ROLE_USER = new SimpleGrantedAuthority("ROLE_USER");

	public static final SimpleGrantedAuthority ROLE_ALL_USER = new SimpleGrantedAuthority("ROLE_ALL_USER");

	public static final SimpleGrantedAuthority ROLE_ORDINARY_USER = new SimpleGrantedAuthority("ROLE_ORDINARY_USER");

}
