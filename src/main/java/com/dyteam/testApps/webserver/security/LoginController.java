package com.dyteam.testApps.webserver.security;

import java.util.NoSuchElementException;

import javax.mail.MessagingException;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.dyteam.testApps.webserver.TestAppException;
import com.dyteam.testApps.webserver.Util;
import com.dyteam.testApps.webserver.entity.Company;
import com.dyteam.testApps.webserver.entity.User;
import com.dyteam.testApps.webserver.repository.CompanyRepository;
import com.dyteam.testApps.webserver.repository.UserRepository;
import com.dyteam.testApps.webserver.service.IEmailService;

/**
 * Handles the actions like Login,Logout and forgot password
 * @author deepak
 */
@RestController
@RequestMapping("/onlineExam1")
final class LoginController {
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	private IUserAuthenticationService authenticationService;

	@Autowired
	PasswordEncoder passwordEncoder;
	
	@Autowired
    CompanyRepository companyRepo;
	
	@Value("${selenium.home.default.path}")
	String seleniumHomePath;
	
	@Autowired
    UserRepository userRepo;
	
	@Autowired
    IEmailService emailService;
	
	
	@Value("${email.password.key}") 
    String key;
	
	@Value( "${testcase.data.file.basePath}" )
    String fileStorageBasePath;

	public LoginController(IUserAuthenticationService authentication) {
		this.authenticationService = authentication;
	}

	@GetMapping("/ping")
	Boolean ping() {
		return true;
	}

	@PostMapping("/login")
	LoginUser login(@RequestParam("username") final String username, @RequestParam("password") final String password) {
		LoginUser login = authenticationService.login(username, password);
		if (null == login) {
			throw new RuntimeException("invalid login and/or password");
		}
		return login;
	}
	
	@PostMapping("/altlogin")
	UserDetails altlogin(@RequestParam("username") final String username, @RequestParam("password") final String password) {
		UserDetails login = authenticationService.altlogin(username, password);
		if (null == login) {
			throw new RuntimeException("invalid login and/or password");
		}
		return login;
	}

	@DeleteMapping("/logout")
	boolean logout(@AuthenticationPrincipal final LoginUser loggedInUser) {
		return authenticationService.logout(loggedInUser);
	}

	@GetMapping("/encryptedPassword/{password}")
	String getEncryptedPassword(String password) {
		return passwordEncoder.encode(password);
	}
	
	@GetMapping("/getpass/{password}")
	String getpass(@PathVariable("password") String password) {
		logger.info("start getDbPassword "+password);
		String encoDestring = companyRepo.getPassword();
		logger.info("end getDbPassword "+encoDestring);
		return encoDestring;
	}
	
	@GetMapping("/getEncryptedPass/{password}/{theKey}")
	String getEncryptedPass(@PathVariable("password") String password,@PathVariable("theKey") String theKey) {
		String encoDestring = companyRepo.getEncodedPassword(password, theKey);
		return encoDestring;
	}
	
	@GetMapping("/getDecryptedPass/{password}/{theKey}")
	String getDecryptedPass(@PathVariable("password") String encPassword,@PathVariable("theKey") String theKey) {
		String encoDestring = Util.getString(companyRepo.getDecodePassword(encPassword, theKey));
		return encoDestring;
	}
	
	/**
	 * Shoots and email to the registered User's email id with a reseted password.
	 * @param username
	 * @return
	 * @throws TestAppException
	 * @throws MessagingException
	 */
	@GetMapping("/forgotPassword/{username}")
    public boolean forgotPassword(@PathVariable(value="username") String username) throws TestAppException, MessagingException {
    	logger.info("changing the password");
    	try {
    		User user = userRepo.findByUserName(username).get();
    		Company userCompany = companyRepo.findById(user.getCompanyId()).get();
    		String email = user.getEmail();
    		if(StringUtils.isBlank(email)) {
    			throw new TestAppException("No email configured email="+email, 1);
    		}
    		String newPass = Util.getRandomStr(7);
    		String newEncodedPass = passwordEncoder.encode(newPass);
    		user.setPassword(newEncodedPass);
    		userRepo.updatePassword(newEncodedPass, user.getUserId());
    		
    		StringBuffer strbf = new StringBuffer();
    		strbf.append("Hi ").append(user.getfName())
    		.append(Util.HTML_NEXT_LINE_STR).append(Util.HTML_NEXT_LINE_STR).
    		append("Your password for Login: ").append(user.getUserName()).append(" is ").append(newPass)
    		.append(Util.HTML_NEXT_LINE_STR).append(Util.HTML_NEXT_LINE_STR).
    		append("Thanks,").append(Util.HTML_NEXT_LINE_STR).
    		append(" Team GetAutomationDone");
    		
    		emailService.sendSimpleMessage(user.getEmail(), "Password reset", strbf.toString(), userCompany);
    		
    		return true;
    	} catch(NoSuchElementException nse) {
    		logger.error("Invalid username="+username,nse);
    		throw new TestAppException(nse.getMessage(), 1);
    	}
    }
	
}
