package com.dyteam.testApps.webserver.controller;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dyteam.testApps.webserver.entity.Environment;
import com.dyteam.testApps.webserver.entity.ExecutionResults;
import com.dyteam.testApps.webserver.entity.ScheduledExecutionBk;
import com.dyteam.testApps.webserver.entity.Testcases;
import com.dyteam.testApps.webserver.repository.ExecutionResultsRepository;
import com.dyteam.testApps.webserver.repository.ScheduledExecutionBkRepository;
import com.dyteam.testApps.webserver.repository.UserRepository;
import com.dyteam.testApps.webserver.view.GenerateExcelReport;
import com.dyteam.testApps.webserver.view.GeneratePdfReport;

/**
 * This controller takes care of handling all operations related to executed Scheduled execution.
 * @author deepak
 */
@RestController
@RequestMapping("/scheduledExecutionBk")
public class ScheduledExecutionBkController {

	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
    @Autowired
    ScheduledExecutionBkRepository scheduledExecutionBkRepo;
    
    @Autowired
	ExecutionResultsRepository executionResultsRepo;
    
    @Autowired
    UserRepository userRepo;
    
    @GetMapping("/{scheduledExecutionBkId}")
    public ScheduledExecutionBk findById(@PathVariable(value="scheduledExecutionBkId") Long scheduledExecutionBkId) {
    	logger.info("get ScheduledExecutionBk by id="+scheduledExecutionBkId);
        return scheduledExecutionBkRepo.findById(scheduledExecutionBkId).orElse(null);
    }
    
    
    @DeleteMapping("/{scheduledExecutionBkId}")
    public Boolean delete(@PathVariable(value="scheduledExecutionBkId") Long scheduledExecutionBkId) {
    	ScheduledExecutionBk scheduledExecutionBk = scheduledExecutionBkRepo.findById(scheduledExecutionBkId).get();
		scheduledExecutionBk.setStatus(false);
		Long runnerId = scheduledExecutionBk.getRunnerId();
		if(null!=runnerId) {
			executionResultsRepo.deleteByRunnerId(runnerId);
		}
    	scheduledExecutionBkRepo.save(scheduledExecutionBk);
		return true;
    }
    
    /**
     * Get detailed info. for a completed Schedule execution by runnerId
     * @param runnerId
     * @return
     */
    @GetMapping("/getRunnerDetailsFull/{runnerId}")
	public Iterable<ExecutionResults> getRunnerDetailsFull(@PathVariable(value = "runnerId") Long runnerId) {
		Iterable<ExecutionResults> findAllDetailsByRunnerId = executionResultsRepo.findAllDetailsByRunnerId(runnerId);
		findAllDetailsByRunnerId.forEach(er -> {
			er.getTestcaseExeDetailList().forEach(ted -> ted.setExecutionResults(null));
		});
		return findAllDetailsByRunnerId;
	}
    
    /**
     * PDF report for completed Scheduled execution test cases
     * @param runnerId
     * @return
     */
    @GetMapping(value = "/downloadPdf/{runnerId}", produces = MediaType.APPLICATION_PDF_VALUE)
	public ResponseEntity<InputStreamResource> downloadPdf(@PathVariable(value = "runnerId") Long runnerId) {
    	Iterable<ExecutionResults> testcaseResults = executionResultsRepo.findAllByRunnerId(runnerId);
		ByteArrayInputStream citiesReport = GeneratePdfReport.getExecutionResults(testcaseResults);
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Disposition", "inline; filename=citiesreport.pdf");
		return ResponseEntity.ok().headers(headers).contentType(MediaType.APPLICATION_PDF)
				.body(new InputStreamResource(citiesReport));
	}

    /**
     * Excel report for completed Scheduled execution test cases
     * @param runnerId
     * @return
     */
	@GetMapping(value = "/downloadExcel/{runnerId}", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
	public ResponseEntity<InputStreamResource> downloadExcel(@PathVariable(value = "runnerId") Long runnerId) {
		Iterable<ExecutionResults> testcaseResults = executionResultsRepo.findAllByRunnerId(runnerId);
		ByteArrayInputStream citiesReport = GenerateExcelReport.getExecutionResultReport(testcaseResults);
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Disposition", "inline; filename=executionResults.xlsx");
		return ResponseEntity.ok().headers(headers).contentType(MediaType.APPLICATION_OCTET_STREAM)
				.body(new InputStreamResource(citiesReport));
	}
	
	@GetMapping("/getSample")
    public ScheduledExecutionBk getSample() {
    	ScheduledExecutionBk scheduledExecutionBk = new ScheduledExecutionBk();
    	scheduledExecutionBk.setCompanyId(1l);
    	scheduledExecutionBk.setEnvironment(new Environment(1l));
    	scheduledExecutionBk.setScheduledExecutionName("scheduledExecutionBkName");
    	scheduledExecutionBk.setScheduledDate(new Date());
    	List<Testcases> testCaseList = new ArrayList<>();
    	testCaseList.add(new Testcases(1l));
    	testCaseList.add(new Testcases(2l));
		scheduledExecutionBk.setTestCaseList(testCaseList);
    	return scheduledExecutionBk;
    }
    
}
