package com.mrunal.taskmanagement.entity;

import org.springframework.security.core.GrantedAuthority;

public enum Role implements GrantedAuthority{
	
	ROLE_USER,
	ROLE_ADMIN;

	@Override
	public String getAuthority() {
		// TODO Auto-generated method stub
		return name();
	}

}
