package com.wy.test.core.constants;

import org.springframework.security.core.authority.SimpleGrantedAuthority;

public interface ConstRoles {

	SimpleGrantedAuthority ROLE_ADMINISTRATORS = new SimpleGrantedAuthority("ROLE_ADMINISTRATORS");

	SimpleGrantedAuthority ROLE_MANAGERS = new SimpleGrantedAuthority("ROLE_MANAGERS");

	SimpleGrantedAuthority ROLE_USER = new SimpleGrantedAuthority("ROLE_USER");

	SimpleGrantedAuthority ROLE_ALL_USER = new SimpleGrantedAuthority("ROLE_ALL_USER");

	SimpleGrantedAuthority ROLE_ORDINARY_USER = new SimpleGrantedAuthority("ROLE_ORDINARY_USER");
}