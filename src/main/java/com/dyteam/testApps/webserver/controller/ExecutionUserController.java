package com.dyteam.testApps.webserver.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dyteam.testApps.webserver.Util;
import com.dyteam.testApps.webserver.entity.ExecutionUser;
import com.dyteam.testApps.webserver.entity.User;
import com.dyteam.testApps.webserver.repository.CompanyRepository;
import com.dyteam.testApps.webserver.repository.ExecutionUserRepository;
import com.dyteam.testApps.webserver.security.LoginUser;

/**
 * This controller takes care of handling all operations related to Execution user
 * @author deepak
 */
@RestController
@RequestMapping("/executionUser")
public class ExecutionUserController {

	private Logger logger = LoggerFactory.getLogger(this.getClass());
	private CompanyRepository companyRepo;
	private String key;
	
	public ExecutionUserController(@Autowired CompanyRepository companyRepo,
			@Value("${execution.user.pass.key}") String key) {
		this.companyRepo=companyRepo;
		this.key=key;
	}
	
    @Autowired
    ExecutionUserRepository executionUserRepo;
    
    @GetMapping("/{executionUserId}")
    public ExecutionUser findById(@PathVariable(value="executionUserId") Long executionUserId) {
    	logger.info("get ExecutionUser by id="+executionUserId);
        ExecutionUser exeUser = executionUserRepo.findById(executionUserId).orElse(null);
        String decodePassword = Util.getString(companyRepo.getDecodePassword(exeUser.getPassword(), key));
        exeUser.setPassword(decodePassword);
		return exeUser;
    }
    
    @GetMapping("/all")
    public Iterable<ExecutionUser> findAll() {
    	logger.info("get all executionUsers");
        Iterable<ExecutionUser> findAll = executionUserRepo.findAll();
        findAll.forEach(eu -> eu.setPassword(null));
		return findAll;
    }
    
    /**
     * Fetches list of all Execution user for a Company
     * @param loggedInUser
     * @return
     */
    @GetMapping("/allByCompany")
    public Iterable<ExecutionUser> findAllCompany(@AuthenticationPrincipal final LoginUser loggedInUser) {
    	logger.info("get all executionUsers");
        Iterable<ExecutionUser> findAllByCompanyId = executionUserRepo.findAllByCompanyId(loggedInUser.getCompanyId());
        findAllByCompanyId.forEach(eu -> eu.setPassword(null));
		return findAllByCompanyId;
    }
    
    /**
     * Create or update Execution user 
     * @param executionUser
     * @param loggedInUser
     * @return
     */
    @PostMapping("/save")
    public ExecutionUser save(@RequestBody ExecutionUser executionUser,@AuthenticationPrincipal final LoginUser loggedInUser) {
    	logger.info("save executionUser = "+executionUser);
    	executionUser.setCompanyId(loggedInUser.getCompanyId());
    	executionUser.setAddedBy(new User(loggedInUser.getUserId()));
    	if(null != executionUser.getExecutionUserId()) {
    		ExecutionUser executionUser2 = executionUserRepo.findById(executionUser.getExecutionUserId()).get();
    		executionUser2.setName(executionUser.getName());
    		executionUser2.setRole(executionUser.getRole());
    		executionUser2.setPassword(executionUser.getPassword());
    		executionUser = executionUser2;
    	} 
    		String rawPassword = executionUser.getPassword();
        	String encodedPassword = companyRepo.getEncodedPassword(rawPassword, key);
        	executionUser.setPassword(encodedPassword);
    	
        return executionUserRepo.save(executionUser);
    }
    
    @DeleteMapping("/{executionUserId}")
    public Boolean delete(@PathVariable(value="executionUserId") Long executionUserId) {
    	executionUserRepo.deleteById(executionUserId);
		return true;
    }
    
}
