package com.dyteam.testApps.webserver.security;

import static java.util.Optional.ofNullable;

import java.util.Optional;

import org.springframework.stereotype.Service;

/**
 * Handles the action related to active login User and there oauth tokens
 * @author deepak
 */
@Service
public class LoginUserService implements UserCrudService {

	// Map<String, LoginUser> loggedInUsers = new HashMap<>();

	LoginUserRepo loginUserRepo;

	public LoginUserService(LoginUserRepo loginUserRepo) {
		this.loginUserRepo = loginUserRepo;
	}

	@Override
	public LoginUser save(final LoginUser user) {
		return loginUserRepo.saveUser(user);
	}

	@Override
	public Optional<LoginUser> find(final String id) {
		return ofNullable(loginUserRepo.getUserById(id));
	}

	@Override
	public LoginUser delete(LoginUser user) {
		return loginUserRepo.deleteUser(user);
	}

}
