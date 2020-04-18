package com.dyteam.testApps.webserver.controller;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.dyteam.testApps.webserver.Util;
import com.dyteam.testApps.webserver.entity.Company;
import com.dyteam.testApps.webserver.entity.User;
import com.dyteam.testApps.webserver.repository.ApplicationRepository;
import com.dyteam.testApps.webserver.repository.CompanyRepository;
import com.dyteam.testApps.webserver.repository.UserRepository;
import com.dyteam.testApps.webserver.security.LoginUser;

/**
 * This controller takes care of handling all operations related to User
 * @author deepak
 */
@RestController
@RequestMapping("/user")
public class UserController {

	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
    @Autowired
    UserRepository userRepo;
    
    @Value("${project.base.path}") 
	String projectBasePath;
    
    @Autowired
    ApplicationRepository applicationRepo;
    
    @Autowired
    CompanyRepository companyRepo;
    
    @Autowired
	PasswordEncoder passwordEncoder;
    
    @GetMapping("/{userId}")
    public User findById(@PathVariable(value="userId") Long userId) {
    	logger.info("get User by id="+userId);
        Optional<User> findById = userRepo.findById(userId);
        if(findById.isPresent()) {
        	return findById.get();
        }else {
        	return null;
        }
    }
    
    @GetMapping("/all")
    public Iterable<User> findAll() {
    	logger.info("get all users");
        return userRepo.findAll();
    }
    
    @GetMapping("/allByCompany")
    public Iterable<User> findAllByCompany(@AuthenticationPrincipal final LoginUser loggedInUser) {
    	logger.info("get all users by company");
        return userRepo.findAll(loggedInUser.getCompanyId());
    }
    
    /**
     * Create or update User object and create respective dir. structure
     * @param user
     * @param loggedInUser
     * @return
     */
    @PostMapping("/save")
    public User save(@RequestBody User user,@AuthenticationPrincipal final LoginUser loggedInUser) {
    	User userAfterSaveOuter = null;
		try {
			boolean isNew = null==user.getUserId();
			if(isNew) {
				user.setAddedBy(loggedInUser.getUserId());
				user.setCompanyId(loggedInUser.getCompanyId());
				user.setRefUserId(loggedInUser.getUserId());
				String password = user.getPassword();
				user.setPassword(passwordEncoder.encode(password));
				user.setStatus(1);
			}else {
				User userDB = userRepo.findById(user.getUserId()).get();
				userDB.setfName(user.getfName());
				userDB.setlName(user.getlName());
				userDB.setEmail(user.getEmail());
				userDB.setContact(user.getContact());
				userDB.setUserType(user.getUserType());
				userDB.setAddress(user.getAddress());
				user = userDB;
			}
			final User userAfterSave = userRepo.save(user);
			userAfterSaveOuter = userAfterSave;
			if(isNew) {
				Company company = companyRepo.getCompanyInfoForFolder(loggedInUser.getCompanyId());
				List<String> applicationNames = applicationRepo.findAllAppNamesByCompanyId(loggedInUser.getCompanyId());

				if(null != applicationNames) {
					applicationNames.parallelStream().forEach(appName -> {
						try {
							Util.createFolders(Paths.get(projectBasePath,Util.COMPANIES_BASE_FOLDER_NAME,
									company.getCompanyName(),
									appName,Util.TEST_DATA_FOLDER_NAME,loggedInUser.getUsername()));
							if(StringUtils.isNotBlank(company.getSeleniumHome())) {
								Util.createFolders(Paths.get(company.getSeleniumHome(),Util.COMPANIES_BASE_FOLDER_NAME,
										company.getCompanyName(),loggedInUser.getUsername()));
							} else {
								logger.error("Could not create folder structure for the user as the company's selenium home is "+company.getSeleniumHome());
							}
							
						} catch (IOException e) {
							logger.error("Error occure while creating dir structure for user="+userAfterSave,e);
						}
					});
				} 
			}
		} catch (Exception e) {
			throw e;
		}
    	logger.info("persist user = "+user);
		return userAfterSaveOuter;
    }
    
    @DeleteMapping("/{userId}")
    public Boolean delete(@PathVariable(value="userId") Long userId,@AuthenticationPrincipal final LoginUser loggedInUser) {
    	if(loggedInUser.getUserId().equals(userId)) {
    		return false;
    	}
    	userRepo.deleteById(userId);
		return true;
    }
    
    /**
     * Update User password
     * @param oldPassword
     * @param newPassword
     * @param loggedInUser
     * @return
     */
    @PostMapping("/changePassword")
    public boolean changePassword(@RequestParam("oldPassword") String oldPassword,
    		@RequestParam("newPassword") String newPassword,
    		@AuthenticationPrincipal final LoginUser loggedInUser) {
    	logger.info("changing the password");
    	 Long userId = loggedInUser.getUserId();
		User user = userRepo.findById(userId).get();
    	 if(passwordEncoder.matches(oldPassword, user.getPassword())) {
    		 String encode = passwordEncoder.encode(newPassword);
    		 userRepo.updatePassword(encode,userId);
    		 return true;
    	 }
        return false;
    }
    
    /**
     * Update User's First name,Last name,Email,Contact and Address.
     * @param user
     * @param loggedInUser
     * @return
     */
    @PostMapping("/updateProfile")
    public User updateProfile(@RequestBody User user,
    		@AuthenticationPrincipal final LoginUser loggedInUser) {
    	logger.info("updating the logged in user profile");
		User userDb = userRepo.findById(loggedInUser.getUserId()).get();
		userDb.setfName(user.getfName());
		userDb.setlName(user.getlName());
		userDb.setEmail(user.getEmail());
		userDb.setContact(user.getContact());
		userDb.setAddress(user.getAddress());
		return userRepo.save(userDb);
    }
    
}
