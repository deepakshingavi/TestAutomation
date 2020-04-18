package com.dyteam.testApps.webserver.controller;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;

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
import com.dyteam.testApps.webserver.entity.Environment;
import com.dyteam.testApps.webserver.repository.ApplicationRepository;
import com.dyteam.testApps.webserver.repository.CompanyRepository;
import com.dyteam.testApps.webserver.repository.EnvironmentRepository;
import com.dyteam.testApps.webserver.repository.ExecutionResultsRepository;
import com.dyteam.testApps.webserver.security.LoginUser;

/**
 * 
 * @author deepak
 * This controller takes care of handling all operations related to environment
 *
 */
@RestController
@RequestMapping("/environment")
public class EnvironmentController {

	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
    @Autowired
    EnvironmentRepository environmentRepo;
    
    @Autowired
	ExecutionResultsRepository executionResultsRepo;
    
    @Autowired
    CompanyRepository companyRepo;
    
    @Value("${project.base.path}") 
	String projectBasePath;
    
    @Autowired
    ApplicationRepository applicationRepo;
    
    @GetMapping("/{environmentId}")
    public Environment findById(@PathVariable(value="environmentId") Long environmentId) {
    	logger.info("get Environment by id="+environmentId);
        return environmentRepo.findById(environmentId).orElse(null);
    }
    
    @GetMapping("/all")
    public Iterable<Environment> findAll(@AuthenticationPrincipal final LoginUser loggedInUser) {
    	logger.info("get all environments");
        return environmentRepo.findAll(loggedInUser.getCompanyId());
    }
    
    /**
     * Save environment by setting its default values
     * Also create default folders on project base path under TestData folder name
     * @param environment - Object to save
     * @param loggedInUser - Logged in user object
     * @return
     */
    @PostMapping("/save")
    public Environment save(@RequestBody Environment environment,
    		@AuthenticationPrincipal final LoginUser loggedInUser) {
    	logger.info("save environment = "+environment);
    	environment.setStatus(1);
    	environment.setAddedBy(loggedInUser.getUserId());
    	environment.setCompanyId(loggedInUser.getCompanyId());
    	environment.setUserId(loggedInUser.getUserId());
    	boolean isNew = null == environment.getEnvironmentId();
        Environment env = environmentRepo.save(environment);
        if(isNew) {
        	String companyName = companyRepo.getName(loggedInUser.getCompanyId());
        	List<String> applicationNames = applicationRepo.findAllAppNamesByCompanyId(loggedInUser.getCompanyId());

        	if(null != applicationNames) {
        		applicationNames.parallelStream().forEach(appName -> {
        			try {
        				Util.createFolders(Paths.get(projectBasePath,Util.COMPANIES_BASE_FOLDER_NAME,
        						companyName,
        						appName,Util.TEST_DATA_FOLDER_NAME,
        						loggedInUser.getUsername(),env.getEnvironmentName()));
        			} catch (IOException e) {
        				logger.error("Error occure while creating dir structure for environment="+environment,e);
        			}
        		});
        	} 
        }
		return env;
    }
    
    @DeleteMapping("/{environmentId}")
    public Boolean delete(@PathVariable(value="environmentId") Long environmentId) {
    	
    	/*boolean envExists = executionResultsRepo.environmentExists(environmentId);
    	if(envExists) {
    		throw new ValidationException("There are Test cases Executed on this environment");
    	}*/
    	
    	environmentRepo.deleteById(environmentId);
		return true;
    }
    
    @GetMapping("/findAllByUserId")
    public Iterable<Environment> findAllByUserId(@AuthenticationPrincipal final LoginUser loggedInUser) {
    	logger.info("get all environments by user id");
        return environmentRepo.findAllByUserId(loggedInUser.getUserId());
    }

	public Iterable<Environment> findAll() {
		logger.info("get all environments");
        return environmentRepo.findAll();
	}
    
}
