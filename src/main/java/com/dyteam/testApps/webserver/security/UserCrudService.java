package com.dyteam.testApps.webserver.security;

import java.util.Optional;

public interface UserCrudService {

	LoginUser save(LoginUser user);

	Optional<LoginUser> find(String id);
	
	LoginUser delete(LoginUser user);
}
