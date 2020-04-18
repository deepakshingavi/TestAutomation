package com.dyteam.testApps.webserver.controller;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

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
import com.dyteam.testApps.webserver.entity.Application;
import com.dyteam.testApps.webserver.entity.Company;
import com.dyteam.testApps.webserver.model.CompanyAppMap;
import com.dyteam.testApps.webserver.repository.ApplicationRepository;
import com.dyteam.testApps.webserver.repository.CompanyRepository;
import com.dyteam.testApps.webserver.security.LoginUser;

/**
 * 
 * @author deepak
 * This controller takes care of handling all operations related to application
 *
 */
@RestController
@RequestMapping("/application")
public class ApplicationController {

	
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	
	@Value("${project.base.path}") 
	String projectBasePath;
	
    @Autowired
    ApplicationRepository applicationRepo;
    
    @Autowired
    CompanyRepository companyRepo;
    
    @GetMapping("/{applicationId}")
    public Optional<Application> findById(@PathVariable(value="applicationId") Long applicationId) {
    	logger.info("get Application by id="+applicationId);
        return applicationRepo.findById(applicationId);
    }
    
    @GetMapping("/all")
    public Iterable<Application> findAll(@AuthenticationPrincipal final LoginUser loggedInUser) {
    	logger.info("get all applications by Company");
        return applicationRepo.findAll();
    }
    
    @GetMapping("/allByCompany")
    public Iterable<Application> findAllByCompany(@AuthenticationPrincipal final LoginUser loggedInUser) {
        return applicationRepo.findAllByCompanyId(loggedInUser.getCompanyId());
    }
    
    @GetMapping("/mapByCompany")
    public CompanyAppMap mapByCompany() {
    	Iterable<Company> companies = companyRepo.findAll();
    	Iterable<Application> apps = applicationRepo.findAll();
    	Map<Long,List<Application>> map = new HashMap<>();
    	apps.forEach(a -> {
    		List<Application> list = map.get(a.getCompanyId());
    		if(null==list) {
    			list = new ArrayList<>();
    			map.put(a.getCompanyId(), list);
    		}
    		list.add(a);
    	});
        
		return new CompanyAppMap(companies, map);
    }
    
    /**
     * Create application object by setting default object 
     * Update not allowed 
     * @param application Object to save
     * @param companyId Company id for creating test & master folder under company base folder 
     * @param loggedInUser
     * @return
     */
    @PostMapping("/save/{companyId}")
    public Application save(@RequestBody Application application,@PathVariable(value="companyId")Long companyId,
    		@AuthenticationPrincipal final LoginUser loggedInUser) {
    	logger.info("save application = "+application);
    	application.setStatus(1);
    	application.setCompanyId(companyId);
    	application.setUserId(loggedInUser.getUserId());
    	boolean isNew = null==application.getApplicationId();
    	Application save = applicationRepo.save(application);
    	if(isNew) {
			try {
				String compnayName = companyRepo.getName(companyId);
				Util.createFolders(Paths.get(projectBasePath,Util.COMPANIES_BASE_FOLDER_NAME,
						compnayName,
						application.getApplicationName(),Util.TEST_DATA_FOLDER_NAME,Util.MASTER_FOLDER_NAME));
			} catch (IOException e) {
				logger.error("Error occure while creating dir structor for application="+application,e);
			}
		}
		return save;
    }
    
    @DeleteMapping("/{applicationId}")
    public Boolean delete(@PathVariable(value="applicationId") Long applicationId) {
    	applicationRepo.deleteById(applicationId);
		return true;
    }
    
}
