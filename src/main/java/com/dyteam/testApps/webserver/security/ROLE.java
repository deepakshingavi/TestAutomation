package com.dyteam.testApps.webserver.security;

import java.util.Arrays;
import java.util.Optional;

/**
 * List of user roles 
 * 1 - ADMIN
 * 2 - SUPER
 * 3 - TESTER
 */
public enum ROLE {
	TESTER(3),ADMIN(1),SUPER(2);
	private int roleId;
	
	ROLE(int roleId){
		this.roleId=roleId;
	}
	
	public int getRoleId() {
		return roleId;
	}
	
	public static Optional<ROLE> find(int roleId) {
		return Arrays.asList(ROLE.values()).stream().filter(role -> role.getRoleId()==roleId).findFirst();
	}

	public static String[] strValues() {
		return Arrays.stream(ROLE.values()).map(role -> role.toString()).toArray(String[]::new);
	}
	
}
