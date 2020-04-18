package com.dyteam.testApps.webserver.controller;

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

import com.dyteam.testApps.webserver.entity.Browser;
import com.dyteam.testApps.webserver.repository.BrowserRepository;
import com.dyteam.testApps.webserver.security.LoginUser;

/**
 * 
 * @author deepak
 * This controller takes care of handling all operations related to browser
 */
@RestController
@RequestMapping("/browser")
public class BrowserController {

	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
    @Autowired
    BrowserRepository browserRepo;
    
    @GetMapping("/{browserId}")
    public Browser findById(@PathVariable(value="browserId") Long browserId) {
    	logger.info("get Browser by id="+browserId);
        return browserRepo.findById(browserId).orElse(null);
    }
    
    @GetMapping("/all")
    public Iterable<Browser> findAll() {
    	logger.info("get all browsers");
        return browserRepo.findAll();
    }
    
    @GetMapping("/allByCompany")
    public Iterable<Browser> findAll(@AuthenticationPrincipal final LoginUser loggedInUser) {
    	logger.info("get all browsers by Company");
        return browserRepo.findAllByCompanyId(loggedInUser.getCompanyId());
    }
    
    /**
     * Create or update Browser object 
     * Browser's company & browser name can be udpated
     * @param browser
     * @param companyId 
     * @param loggedInUser
     * @return
     */
    @PostMapping("/save/{companyId}")
    public Browser save(@RequestBody Browser browser, @PathVariable(value="companyId")Long companyId,
    		@AuthenticationPrincipal final LoginUser loggedInUser) {
    	logger.info("save browser = "+browser);
    	
    	if(browser.getBrowserId()==null) {
    		browser.setCompanyId(companyId);
    		browser.setUserId(loggedInUser.getUserId());
    		browser.setStatus(1);
    	}else {
    		Browser browserDb = browserRepo.findById(browser.getBrowserId()).get();
    		browserDb.setCompanyId(companyId);
    		browserDb.setBrowserName(browser.getBrowserName());
    		browser=browserDb;
    	}
    	
    	browser.setUserId(loggedInUser.getUserId());
    	
        return browserRepo.save(browser);
    }
    
    @DeleteMapping("/{browserId}")
    public Boolean delete(@PathVariable(value="browserId") Long browserId) {
    	browserRepo.deleteById(browserId);
		return true;
    }
    
}
