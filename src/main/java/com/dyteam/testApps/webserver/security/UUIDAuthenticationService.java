package com.dyteam.testApps.webserver.security;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.dyteam.testApps.webserver.entity.User;
import com.dyteam.testApps.webserver.repository.UserRepository;

/**
 * Generate OAuth Tokens ad fetches the user basic info and saves it in Cache.
 * @author deepak
 */
@Service
final class UUIDAuthenticationService implements IUserAuthenticationService {
	
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
  private UserCrudService loggedInUsers;
  
  private UserRepository userRepo;
  
  private PasswordEncoder passEncoder;
  
  public UUIDAuthenticationService(UserCrudService users,UserRepository userRepo,PasswordEncoder passEncoder) {
	super();
	this.loggedInUsers = users;
	this.userRepo=userRepo;
	this.passEncoder=passEncoder;
}
  
  private Collection<? extends GrantedAuthority> getAuthorities(
		  Collection<String> roles) {
		    List<GrantedAuthority> authorities
		      = new ArrayList<>();
		    for (String role: roles) {
		        authorities.add(new SimpleGrantedAuthority(role));
		    }
		     
		    return authorities;
		}

@Override
  public LoginUser login(final String loginId, final String password) {
    final String uuid = UUID.randomUUID().toString();
    LoginUser user = null;
    Optional<User> findByLoginId = userRepo.findByUserName(loginId);
    if(findByLoginId.isPresent()) {
    	User userEntity = findByLoginId.get();
    	Integer userType = userEntity.getUserType();
    	Optional<ROLE> role = ROLE.find(userType);
//    	List<GrantedAuthority> roles = null;
    	Collection<? extends GrantedAuthority> authorities = null;
    	if(role.isPresent()) {
    		authorities = getAuthorities(Arrays.asList(role.get().toString()));
//    		GrantedAuthority authority = new SimpleGrantedAuthority();
//    		roles = Arrays.asList(authority);
//    		roles = Arrays.asList(role.get().toString());
    	}
    	user = new LoginUser(uuid, userEntity.getUserId(),loginId, password,userEntity.getCompanyId(),authorities);
    	String passwordDb = userEntity.getPassword();
    	if(passEncoder.matches(password, passwordDb)) {
    		loggedInUsers.save(user);
    		return user;
    	}
    }
    return null;
  }

  @Override
  public Optional<LoginUser> findByToken(final String token) {
    Optional<LoginUser> find = loggedInUsers.find(token);
    if(find.isPresent()) {
    	logger.info(find.get().toString());
    }
	return find;
  }

  @Override
  public boolean logout(final LoginUser user) {
	  LoginUser delete = loggedInUsers.delete(user);
	  return null==delete;
  }

@Override
public UserDetails altlogin(String username, String password) {
//	final String uuid = UUID.randomUUID().toString();
//    LoginUser user = null;
    Optional<User> findByLoginId = userRepo.findByUserName(username);
    if(findByLoginId.isPresent()) {
    	User userEntity = findByLoginId.get();
    	Integer userType = userEntity.getUserType();
    	Optional<ROLE> role = ROLE.find(userType);
//    	List<GrantedAuthority> roles = null;
    	Collection<? extends GrantedAuthority> authorities = null;
    	if(role.isPresent()) {
    		authorities = getAuthorities(Arrays.asList(role.get().toString()));
//    		GrantedAuthority authority = new SimpleGrantedAuthority();
//    		roles = Arrays.asList(authority);
//    		roles = Arrays.asList(role.get().toString());
    	}
    	
//    	user = new User(uuid, userEntity.getUserId(),username, password,userEntity.getCompanyId(),authorities);
    	
    	return new org.springframework.security.core.userdetails.User(username, password, authorities);
    	
    }
    return null;
}
}
