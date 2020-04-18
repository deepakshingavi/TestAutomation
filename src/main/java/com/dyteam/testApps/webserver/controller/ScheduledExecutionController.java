package com.dyteam.testApps.webserver.controller;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BinaryOperator;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
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
import com.dyteam.testApps.webserver.entity.ScheduledExecution;
import com.dyteam.testApps.webserver.entity.ScheduledExecutionBk;
import com.dyteam.testApps.webserver.entity.Testcases;
import com.dyteam.testApps.webserver.model.EXECUTION_RESULT;
import com.dyteam.testApps.webserver.model.ExecutionResultRunnerInfo;
import com.dyteam.testApps.webserver.model.ExecutionUiBean;
import com.dyteam.testApps.webserver.model.LatestExecutionSummary;
import com.dyteam.testApps.webserver.model.ScheduledUIBean;
import com.dyteam.testApps.webserver.repository.ExecutionResultsRepository;
import com.dyteam.testApps.webserver.repository.ScheduledExecutionBkRepository;
import com.dyteam.testApps.webserver.repository.ScheduledExecutionRepository;
import com.dyteam.testApps.webserver.repository.UserRepository;
import com.dyteam.testApps.webserver.security.LoginUser;
import com.dyteam.testApps.webserver.view.GenerateExcelReport;
import com.dyteam.testApps.webserver.view.GeneratePdfReport;

/**
 * This controller takes care of handling all operations for the Scheduled execution.
 * @author deepak
 */
@RestController
@RequestMapping("/scheduledExecution")
public class ScheduledExecutionController {

	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
    @Autowired
    ScheduledExecutionRepository scheduledExecutionRepo;
    
    @Autowired
    ScheduledExecutionBkRepository scheduledExecutionBkRepo;
    
    @Autowired
    ExecutionResultsRepository executionResultsRepo;
    
    @Autowired
    UserRepository userRepo;
    
    @GetMapping("/{scheduledExecutionId}")
    public ScheduledExecution findById(@PathVariable(value="scheduledExecutionId") Long scheduledExecutionId) {
    	logger.info("get ScheduledExecution by id="+scheduledExecutionId);
        return scheduledExecutionRepo.findById(scheduledExecutionId).orElse(null);
    }
    
    /**
     * Get one more schedule exe. object for a runner.
     * @param scheduledExecutionId
     * @param runnerId
     * @return
     */
    @GetMapping("/{scheduledExecutionId}/{runnerId}")
    public ScheduledExecution findByIdAndRunnerId(@PathVariable(value="scheduledExecutionId") Long scheduledExecutionId,
    		@PathVariable(value="runnerId") Long runnerId) {
    	logger.info("get ScheduledExecution by id="+scheduledExecutionId);
    	ScheduledExecution seToReturn = null;
    	if(0==runnerId) {
    		seToReturn =  scheduledExecutionRepo.findById(scheduledExecutionId).orElse(null);
    	} else {
    		Iterable<ScheduledExecution> findByRunnerId = scheduledExecutionRepo.findByRunnerIdAndTestCaseList(runnerId);
    		if(null!=findByRunnerId) {
    			seToReturn = ((List<ScheduledExecution>)findByRunnerId).stream().reduce((se,se1) -> {
    				se.setTestCaseList(Stream.concat(se.getTestCaseList().stream(), se1.getTestCaseList().stream()).distinct().collect(Collectors.toList()));
    				return se;
    			}).get();
    			return seToReturn;
    		}
    	}
    	return seToReturn;
    }
    
    /**
     * List of all pending and executed Schedule test cases in a single list.
     * @param loggedInUser
     * @return
     */
    @GetMapping("/allByCompany")
    public List<ExecutionUiBean> findAllByCompanyId(@AuthenticationPrincipal final LoginUser loggedInUser) {
    	logger.info("get all scheduledExecutions by Company");
        Iterable<Object[]> scheduledExecution = scheduledExecutionRepo.findAllByCompanyId(loggedInUser.getCompanyId());
        
        Map<Long, List<ExecutionUiBean>> executionUiBeansMap = ((ArrayList<Object[]>)scheduledExecution).stream().
        map(o -> new ExecutionUiBean((ScheduledExecution)o[0],(String)o[1])).distinct().collect(Collectors.groupingBy(ExecutionUiBean::getRunnerId));
        
        
        List<ExecutionUiBean> remove = executionUiBeansMap.remove(0l);
        
        BinaryOperator<ExecutionUiBean> accumulator = (se,se1) -> {
        	return se;
        };
        List<ExecutionUiBean> executionUiBeanList = executionUiBeansMap.values().stream().map(se->{
        	return se.stream().reduce(accumulator).orElse(null);
        }).filter(se->se!=null).collect(Collectors.toList());
        
        logger.info("0:schedules:"+remove+" executionUiBeanList= "+executionUiBeanList);
        
        remove = null==remove?new ArrayList<>():remove;
        
        executionUiBeanList = null==executionUiBeanList?new ArrayList<>():executionUiBeanList;
        
		List<ExecutionUiBean> collect = Stream.concat(remove.stream(),executionUiBeanList.stream()).collect(Collectors.toList());
		collect.sort(Comparator.comparing(ExecutionUiBean::getCreatedAt).reversed());
        
        
        Iterable<Object[]> scheduledExecutionBkList = scheduledExecutionBkRepo.findAllByCompanyId(loggedInUser.getCompanyId());
        
        List<ExecutionResultRunnerInfo> findAllRunner = (List<ExecutionResultRunnerInfo>) executionResultsRepo
				.findAllScheduleRunner(loggedInUser.getCompanyId());
        Map<Long, LatestExecutionSummary> map = new LinkedHashMap<>();

		if (null != findAllRunner) {
			Util.populateResults(findAllRunner, map, true);
		}
        
		collect.addAll(((ArrayList<Object[]>)scheduledExecutionBkList).stream().
                map(o -> {
                	ScheduledExecutionBk scheduledExecutionBk = (ScheduledExecutionBk)o[0];
                	LatestExecutionSummary latestExecutionSummary = map.get(scheduledExecutionBk.getRunnerId());
                	if(null == latestExecutionSummary) {
                		return new ExecutionUiBean(scheduledExecutionBk,(String)o[1],Boolean.TRUE); 
                	} else {
                		return new ExecutionUiBean(scheduledExecutionBk,(String)o[1],Boolean.TRUE,latestExecutionSummary.getProgess());
                	}
                	} ).collect(Collectors.toList()));
        
		return collect;
    }
    
    /**
     * Create or update a Schedule test case.
     * @param scheduledExecution
     * @param loggedInUser
     * @return
     */
    @PostMapping("/save")
    public ScheduledExecution save(@RequestBody ScheduledExecution scheduledExecution,
    		@AuthenticationPrincipal final LoginUser loggedInUser)  {
    	logger.info("save scheduledExecution = "+scheduledExecution);
//    	new SimpleDateFormat("YYYY-MM-dd hh:mm a").parse(scheduleDateStr);
    	if(null == scheduledExecution.getScheduledExecutionId()) {
    		scheduledExecution.setCompanyId(loggedInUser.getCompanyId());
    		scheduledExecution.setScheduledById(loggedInUser.getUserId());
    		scheduledExecution.setStatus(true);
    		scheduledExecution.setRunnerId(0l);
    	} else {
    		ScheduledExecution scheduledExecution2 = scheduledExecutionRepo.findById(scheduledExecution.getScheduledExecutionId()).get();
    		scheduledExecution.setCompanyId(scheduledExecution2.getCompanyId());
    		scheduledExecution.setScheduledById(scheduledExecution2.getScheduledById());
    	}
        return scheduledExecutionRepo.save(scheduledExecution);
    }
    
    @DeleteMapping("/{scheduledExecutionId}")
    public Boolean delete(@PathVariable(value="scheduledExecutionId") Long scheduledExecutionId) {
    	ScheduledExecution scheduledExecution = scheduledExecutionRepo.findById(scheduledExecutionId).get();
		scheduledExecution.setStatus(false);
    	scheduledExecutionRepo.save(scheduledExecution);
		return true;
    }
    
    /**
     * Get List of Schedule by range of dates
     * @return
     */
    @GetMapping("/allByDateRange")
    public Iterable<ScheduledExecution> findAllByDateRange() {
    	logger.info("get all scheduledExecutions");
    	Date from = new Date(1534319700000l);
    	Date to = new Date(1534320000000l);
        return scheduledExecutionRepo.findByScheduledDateBetween(from, to);
    }
    
    /**
     * Get all Schedule test cases which are due for execution.
     * @return
     */
    @GetMapping("/allByBeforeCurrDate")
    public Iterable<ScheduledExecution> findAllByBeforeCurrDate() {
    	logger.info("get all scheduledExecutions");
    	Date to = new Date();
        return scheduledExecutionRepo.findByScheduledDateBefore(to);
    }
    
    /**
     * List of all pending Scheduled test cases
     * @param runnerId
     * @return
     */
    @GetMapping("/getRunnerDetailsFull/{scheduledExecutionId}/{runnerId}")
	public Iterable<ScheduledUIBean> getRunnerDetailsFull(
			@PathVariable(value = "scheduledExecutionId") Long scheduledExecutionId,
			@PathVariable(value = "runnerId") Long runnerId) {
    	if(0==runnerId) {
    		ScheduledExecution scheduledExecution =  scheduledExecutionRepo.findById(scheduledExecutionId).orElse(null);
    		List<ScheduledUIBean> executionResults = scheduledExecution.getTestCaseList().stream().map(se -> {
        		ScheduledUIBean er = new ScheduledUIBean();
        		er.setEnvName(scheduledExecution.getEnvironment().getEnvironmentName());
    			er.setTestcaseName(se.getTestcaseName());
    			er.setResult(EXECUTION_RESULT.SUBMITED.toString());
    			return er;
    		}).collect(Collectors.toList());
        	
        	return executionResults;
    	} else {
    		Iterable<ScheduledExecution> scheduledExecutions = scheduledExecutionRepo.findByRunnerIdAndTestCaseList(runnerId);
    		if(null!=scheduledExecutions) {
    			return ((List<ScheduledExecution>)scheduledExecutions).stream().flatMap(se-> {
    				return se.getTestCaseList().stream().map(tc-> new ScheduledUIBean(tc.getTestcaseName(), se.getEnvironment().getEnvironmentName(), EXECUTION_RESULT.SUBMITED.toString())).
    				collect(Collectors.toList()).stream();
    			}).distinct().collect(Collectors.toList());
    		}
    	}
    	return null;
	}
    
    /**
     * PDF report for pending Scheduled test cases
     * @param runnerId
     * @return
     */
    @GetMapping(value = "/downloadPdf/{scheduledExecutionId}", produces = MediaType.APPLICATION_PDF_VALUE)
	public ResponseEntity<InputStreamResource> downloadPdf(@PathVariable(value = "scheduledExecutionId") Long scheduledExecutionId) {
		ScheduledExecution scheduledExecution = scheduledExecutionRepo.findById(scheduledExecutionId).get();
		ByteArrayInputStream citiesReport = GeneratePdfReport.getScheduledExecutionResults(scheduledExecution);
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
	@GetMapping(value = "/downloadExcel/{scheduledExecutionId}", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
	public ResponseEntity<InputStreamResource> downloadExcel(@PathVariable(value = "scheduledExecutionId") Long scheduledExecutionId) {
		ScheduledExecution scheduledExecution = scheduledExecutionRepo.findById(scheduledExecutionId).get();
		ByteArrayInputStream citiesReport = GenerateExcelReport.getScheduledExecutionResultReport(scheduledExecution);
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Disposition", "inline; filename=executionResults.xlsx");
		return ResponseEntity.ok().headers(headers).contentType(MediaType.APPLICATION_OCTET_STREAM)
				.body(new InputStreamResource(citiesReport));
	}
	
	@GetMapping("/getSample")
    public ScheduledExecution getSample() {
    	ScheduledExecution scheduledExecution = new ScheduledExecution();
    	scheduledExecution.setCompanyId(1l);
    	scheduledExecution.setEnvironment(new Environment(1l));
    	scheduledExecution.setScheduledExecutionName("scheduledExecutionName");
    	scheduledExecution.setScheduledDate(new Date());
    	List<Testcases> testCaseList = new ArrayList<>();
    	testCaseList.add(new Testcases(1l));
    	testCaseList.add(new Testcases(2l));
		scheduledExecution.setTestCaseList(testCaseList);
    	return scheduledExecution;
    }
    
}
