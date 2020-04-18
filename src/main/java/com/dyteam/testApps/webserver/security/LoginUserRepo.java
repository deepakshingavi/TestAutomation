package com.dyteam.testApps.webserver.security;

import java.time.Duration;
import java.util.concurrent.ConcurrentMap;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

/**
 * Handles the action related to active login User and there oauth tokens
 * @author deepak
 */
@Repository
public class LoginUserRepo {

	private ConcurrentMap<String, LoginUser> loggedInUsers;
	
	
	
	public LoginUserRepo(@Value( "${token.expiry.minutes}" )Integer tokenExpiry) {
	    Cache<String, LoginUser> cache = CacheBuilder.newBuilder().expireAfterWrite(Duration.ofMinutes(tokenExpiry)).build();
	    this.loggedInUsers = cache.asMap();
	}
	
	public LoginUser getUserById(String token){
		return loggedInUsers.get(token);
	}
	
	public LoginUser saveUser(LoginUser loginUser) {
		loggedInUsers.put(loginUser.getId(), loginUser);
		return loginUser;
	}
	
	public LoginUser deleteUser(LoginUser loginUser) {
		return loggedInUsers.remove(loginUser.getId());
	}
	
	
}
