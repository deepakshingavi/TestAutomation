package com.dyteam.testApps.webserver.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dyteam.testApps.webserver.entity.TestcaseResult;
import com.dyteam.testApps.webserver.repository.TestcaseResultRepository;

/**
 * This controller takes care of handling all operations related to Test Result
 * @author deepak
 */
@RestController
@RequestMapping("/testcaseResult")
public class TestcaseResultController {

	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
    @Autowired
    TestcaseResultRepository testcaseResultRepo;
    
    @GetMapping("/")
    public TestcaseResult findById(@PathVariable(value="testcaseResultId") Long testcaseResultId) {
    	logger.info("get TestcaseResult by id="+testcaseResultId);
        return testcaseResultRepo.findById(testcaseResultId).orElse(null);
    }
    
    @GetMapping("/all")
    public Iterable<TestcaseResult> findAll() {
    	logger.info("get all testcaseResults");
        return testcaseResultRepo.findAll();
    }
    
    @PostMapping("/save")
    public TestcaseResult save(@RequestBody TestcaseResult testcaseResult) {
    	logger.info("save testcaseResult = "+testcaseResult);
        return testcaseResultRepo.save(testcaseResult);
    }
    
    @DeleteMapping("/{testcaseResultId}")
    public Boolean delete(@PathVariable(value="testcaseResultId") Long testcaseResultId) {
    	testcaseResultRepo.deleteById(testcaseResultId);
		return true;
    }
    
}
