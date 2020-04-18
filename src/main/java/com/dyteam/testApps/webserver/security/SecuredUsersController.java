package com.dyteam.testApps.webserver.security;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Deals iwth the logged user info. exchanes with UI.
 * @author deepak
 */
@RestController
@RequestMapping("/secured/users")
final class SecuredUsersController {
	private IUserAuthenticationService authentication;

	public SecuredUsersController(com.dyteam.testApps.webserver.security.IUserAuthenticationService authentication) {
		super();
		this.authentication = authentication;
	}

	/**
	 * Get Current logged User object
	 * @param user
	 * @return
	 */
	@GetMapping("/current")
	LoginUser getCurrent(@AuthenticationPrincipal final LoginUser user) {
		return user;
	}

	/**
	 * Logs out the current active user.
	 * @param loggedInUser
	 * @return
	 */
	@DeleteMapping("/logout")
	boolean logout(@AuthenticationPrincipal final LoginUser loggedInUser) {
		return authentication.logout(loggedInUser);
	}
}
