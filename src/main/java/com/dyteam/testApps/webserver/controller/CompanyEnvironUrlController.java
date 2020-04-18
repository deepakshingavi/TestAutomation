package com.dyteam.testApps.webserver.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dyteam.testApps.webserver.entity.Application;
import com.dyteam.testApps.webserver.entity.CompanyEnvironUrl;
import com.dyteam.testApps.webserver.entity.Environment;
import com.dyteam.testApps.webserver.model.AddCompanyEvnUrl;
import com.dyteam.testApps.webserver.model.AddCompanyEvnUrlEmpty;
import com.dyteam.testApps.webserver.model.AppCompanyEnvWrapper;
import com.dyteam.testApps.webserver.model.ApplicationCompanyUrl;
import com.dyteam.testApps.webserver.model.ApplicationEnvUrlInfo;
import com.dyteam.testApps.webserver.repository.ApplicationRepository;
import com.dyteam.testApps.webserver.repository.CompanyEnvironUrlRepository;
import com.dyteam.testApps.webserver.repository.EnvironmentRepository;
import com.dyteam.testApps.webserver.security.LoginUser;

/**
 * 
 * @author deepak
 * This controller takes care of handling all operations related to Company environment URLs
 */
@RestController
@RequestMapping("/companyEnvironUrl")
public class CompanyEnvironUrlController {

	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
    @Autowired
    CompanyEnvironUrlRepository companyEnvironUrlRepo;
    
    @Autowired
    EnvironmentRepository environmentRepo;
    
    @Autowired
    ApplicationRepository applicationRepo;
    
    @GetMapping("/{companyEnvironUrlId}")
    public CompanyEnvironUrl findById(@PathVariable(value="companyEnvironUrlId") Long companyEnvironUrlId) {
    	logger.info("get CompanyEnvironUrl by id="+companyEnvironUrlId);
        return companyEnvironUrlRepo.findById(companyEnvironUrlId).orElse(null);
    }
    
    @GetMapping("/all")
    public Iterable<CompanyEnvironUrl> findAll(@AuthenticationPrincipal final LoginUser loggedInUser) {
    	logger.info("get all companyEnvironUrls");
//        return companyEnvironUrlRepo.findAll();
    	return companyEnvironUrlRepo.findAll(loggedInUser.getCompanyId());
    }
    
    @PostMapping("/save")
    public CompanyEnvironUrl save(@RequestBody CompanyEnvironUrl companyEnvironUrl,
    		@AuthenticationPrincipal final LoginUser loggedInUser) {
    	if(null == companyEnvironUrl.getCompanyEnvironUrlId()) {
    		companyEnvironUrl.setCompanyId(loggedInUser.getCompanyId());
    		companyEnvironUrl.setUserId(loggedInUser.getUserId());
    	}
    	logger.info("save companyEnvironUrl = "+companyEnvironUrl);
        return companyEnvironUrlRepo.save(companyEnvironUrl);
    }
    
    /**
     * Saves list of Company environment URL for same company & user 
     * @param addCompanyEvnUrl
     * @param loggedInUser
     * @return
     */
    @PostMapping("/saveAll")
    public Iterable<CompanyEnvironUrl> saveAll(@RequestBody AddCompanyEvnUrl addCompanyEvnUrl,
    		@AuthenticationPrincipal final LoginUser loggedInUser) {
    	Application application = addCompanyEvnUrl.getApplication();
    	addCompanyEvnUrl.getCompanyEnvironUrls().forEach(u -> {
    		if(null == u.getCompanyEnvironUrlId()) {
    			u.setCompanyId(loggedInUser.getCompanyId());
    			u.setStatus(1);
    			u.setUserId(loggedInUser.getUserId());
    		}
    		u.setApplication(application);
    	});
    	logger.info("save companyEnvironUrl = "+addCompanyEvnUrl);
        return companyEnvironUrlRepo.saveAll(addCompanyEvnUrl.getCompanyEnvironUrls());
    }
    
    @DeleteMapping("/{companyEnvironUrlId}")
    public Boolean delete(@PathVariable(value="companyEnvironUrlId") Long companyEnvironUrlId) {
    	companyEnvironUrlRepo.deleteById(companyEnvironUrlId);
		return true;
    }
    
    @DeleteMapping("/byApplication/{applicationId}")
    public Boolean deleteByApplication(@PathVariable(value="applicationId") Long applicationId) {
    	companyEnvironUrlRepo.deleteByApplicationId(applicationId);
		return true;
    }
    
    /**
     * Get list of all Company env. URLs by Company
     * @param loggedInUser
     * @return
     */
    @GetMapping("/findAllByCompanyId")
    public AppCompanyEnvWrapper findAllByCompanyId(@AuthenticationPrincipal final LoginUser loggedInUser) {
    	logger.info("get all companyEnvironUrls");
        Iterable<ApplicationCompanyUrl> companyEnvironUrls = companyEnvironUrlRepo.findAllByUserId(loggedInUser.getCompanyId());
//        List<Environment> environments = environmentRepo.getAllByCompanyId(loggedInUser.getCompanyId());
//    	List<Application> applications = applicationRepo.getAllByCompanyId(loggedInUser.getCompanyId());
        logger.info("get all companyEnvironUrls fom db : "+companyEnvironUrls);
        Map<Long, ApplicationEnvUrlInfo> map = new HashMap<>();
        companyEnvironUrls.forEach(ceu -> {
        	if(!map.containsKey(ceu.getApplicationId())) {
        		List<CompanyEnvironUrl> companyEnvironUrlLists = new ArrayList<>();
        		ApplicationEnvUrlInfo applicationEnvUrlInfo = new ApplicationEnvUrlInfo(ceu.getApplicationName(),companyEnvironUrlLists);
        		map.put(ceu.getApplicationId(), applicationEnvUrlInfo);
        	}
        	CompanyEnvironUrl companyEnvironUrl = new CompanyEnvironUrl();
        	companyEnvironUrl.setCompanyEnvironUrlId(ceu.getCompanyEnvironUrlId());
        	companyEnvironUrl.setEnvironment(new Environment(ceu.getEnvironmentId(),ceu.getEnvironmentName()));
        	companyEnvironUrl.setEnvUrl(ceu.getEnvUrl());
        	map.get(ceu.getApplicationId()).add(companyEnvironUrl);
        });
		return new AppCompanyEnvWrapper(map);
    }
    
    /**
     * Fetches a complex object of Company env. list mapped to its respective application
     * @param loggedInUser
     * @return Map (Application id --> List of Company env. list)
     */
    @GetMapping("/getByCompanyId")
    public AppCompanyEnvWrapper getByCompanyId(@AuthenticationPrincipal final LoginUser loggedInUser) {
    	logger.info("get by companyId");
        Iterable<ApplicationCompanyUrl> companyEnvironUrls = companyEnvironUrlRepo.findAllByUserId(loggedInUser.getCompanyId());
        logger.info("get all companyEnvironUrls fom db : "+companyEnvironUrls);
        Map<Long, ApplicationEnvUrlInfo> map = new HashMap<>();
        companyEnvironUrls.forEach(ceu -> {
        	if(!map.containsKey(ceu.getApplicationId())) {
        		List<CompanyEnvironUrl> companyEnvironUrlLists = new ArrayList<>();
        		ApplicationEnvUrlInfo applicationEnvUrlInfo = new ApplicationEnvUrlInfo(ceu.getApplicationName(),companyEnvironUrlLists);
        		map.put(ceu.getApplicationId(), applicationEnvUrlInfo);
        	}
        	CompanyEnvironUrl companyEnvironUrl = new CompanyEnvironUrl();
        	companyEnvironUrl.setCompanyEnvironUrlId(ceu.getCompanyEnvironUrlId());
        	companyEnvironUrl.setEnvironment(new Environment(ceu.getEnvironmentId(),ceu.getEnvironmentName()));
        	companyEnvironUrl.setEnvUrl(ceu.getEnvUrl());
        	map.get(ceu.getApplicationId()).add(companyEnvironUrl);
        });
		return new AppCompanyEnvWrapper(map);
    }
    
    /**
     * Get List of all application & environment for this user
     * @param loggedInUser
     * @return
     */
    @GetMapping("/getAddCompanyEnvUrl")
    public AddCompanyEvnUrlEmpty getAddCompanyEvnUrl(@AuthenticationPrincipal final LoginUser loggedInUser) {
    	Long userId = loggedInUser.getUserId();
    	List<Environment> environments = environmentRepo.findAllByUserId(userId);
    	List<Application> applications = applicationRepo.findAllByUserId(userId);
    	return new AddCompanyEvnUrlEmpty(userId,applications,environments);
    }
    
    /**
     * Saves list of Company env. URL in single instant
     * @param addCompanyEvnUrl
     * @return
     */
    @PostMapping("/saveCompanyEvnUrls")
    public boolean saveCompanyEvnUrls(@RequestBody AddCompanyEvnUrl addCompanyEvnUrl) {
    	logger.info("addCompanyEvnUrl="+addCompanyEvnUrl);
    	Iterable<CompanyEnvironUrl> saveAll = companyEnvironUrlRepo.saveAll(addCompanyEvnUrl.getCompanyEnvironUrls());
    	logger.info("saveAll="+saveAll);
    	return true;
    }
    
    /**
     * Test method to get sample JSON object 
     * @return
     */
    @GetMapping("/getSample")
    public AddCompanyEvnUrl getSample() {
    	List<CompanyEnvironUrl> companyEnvironUrls = new ArrayList<>();
    	CompanyEnvironUrl e1 = new CompanyEnvironUrl();
    	CompanyEnvironUrl e = new CompanyEnvironUrl();
    	e.setEnvUrl("aaaaa");
    	Environment environment = new Environment();
    	environment.setEnvironmentId(1l);
    	environment.setEnvironmentName("QA");
    	e.setEnvironment(environment);
    	e1.setEnvironment(environment);
    	e1.setEnvUrl("bbbb");
    	Application application = new Application();
    	application.setApplicationId(1l);
		e.setApplication(application);
		e1.setApplication(application);
		companyEnvironUrls.add(e);
		companyEnvironUrls.add(e1);
		return new AddCompanyEvnUrl(companyEnvironUrls,application);
    }
    
    /**
     * Get list of all Company env. URL
     * @param applicationId
     * @param loggedInUser
     * @return
     */
    @GetMapping("/getAllByCompanyId/{applicationId}")
    public AddCompanyEvnUrl getAllByCompanyId(@PathVariable(value="applicationId")Long applicationId,@AuthenticationPrincipal final LoginUser loggedInUser) {
    	logger.info("get all companyEnvironUrls");
        Iterable<Object[]> companyEnvironUrls = companyEnvironUrlRepo.findAllByUserId1(loggedInUser.getCompanyId(),applicationId);
        logger.info("get all companyEnvironUrls fom db : "+companyEnvironUrls);
        List<CompanyEnvironUrl> companyEnvironUrlList = new ArrayList<>();
        companyEnvironUrls.forEach(ceu -> {
        	CompanyEnvironUrl ceuInner = (CompanyEnvironUrl)ceu[0];
        	ceuInner.setApplication(null);
        	companyEnvironUrlList.add(ceuInner);
        });
		return new AddCompanyEvnUrl(companyEnvironUrlList, new Application(applicationId,null));
    }
    
    
}
