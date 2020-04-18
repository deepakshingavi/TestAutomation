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

import com.dyteam.testApps.webserver.entity.TestcaseExeDetail;
import com.dyteam.testApps.webserver.repository.TestcaseExeDetailRepository;

/**
 * This controller takes care of handling all operations related to Test case execution details
 * @author deepak
 */
@RestController
@RequestMapping("/testcaseExeDetail")
public class TestcaseExeDetailController {

	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
    @Autowired
    TestcaseExeDetailRepository testcaseExeDetailRepo;
    
    @GetMapping("/{testcaseExeDetailId}")
    public TestcaseExeDetail findById(@PathVariable(value="testcaseExeDetailId") Long testcaseExeDetailId) {
    	logger.info("get TestcaseExeDetail by id="+testcaseExeDetailId);
        return testcaseExeDetailRepo.findById(testcaseExeDetailId).orElse(null);
    }
    
    @GetMapping("/all")
    public Iterable<TestcaseExeDetail> findAll() {
    	logger.info("get all testcaseExeDetails");
        return testcaseExeDetailRepo.findAll();
    }
    
   /* @GetMapping("/allForRunnerAndTest/{runnerId}/{testcaseId}")
    public Iterable<TestcaseExeDetail> getAllForRunnerAndTest(@PathVariable("runnerId")Long runnerId,@PathVariable("testcaseId")Long testcaseId) {
    	logger.info("get all testcaseExeDetails");
        return testcaseExeDetailRepo.findAllForRunnerAndTest(runnerId,testcaseId);
    }*/
    
    @PostMapping("/save")
    public TestcaseExeDetail save(@RequestBody TestcaseExeDetail testcaseExeDetail) {
    	logger.info("save testcaseExeDetail = "+testcaseExeDetail);
        return testcaseExeDetailRepo.save(testcaseExeDetail);
    }
    
    @DeleteMapping("/{testcaseExeDetailId}")
    public Boolean delete(@PathVariable(value="testcaseExeDetailId") Long testcaseExeDetailId) {
    	testcaseExeDetailRepo.deleteById(testcaseExeDetailId);
		return true;
    }
    
}
