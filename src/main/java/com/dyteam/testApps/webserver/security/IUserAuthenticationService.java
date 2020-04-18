package com.dyteam.testApps.webserver.security;

import java.util.Optional;

import org.springframework.security.core.userdetails.UserDetails;

public interface IUserAuthenticationService {

	/**
	   * Logs in with the given {@code username} and {@code password}.
	   *
	   * @param username
	   * @param password
	   * @return an {@link Optional} of a user when login succeeds
	   */
	  LoginUser login(String username, String password);

	  /**
	   * Finds a user by its dao-key.
	   *
	   * @param token user dao key
	   * @return
	   */
	  Optional<LoginUser> findByToken(String token);

	  /**
	   * Logs out the given input {@code user}.
	   *
	   * @param user the user to logout
	   */
	  boolean logout(LoginUser user);

	UserDetails altlogin(String username, String password);
	}
