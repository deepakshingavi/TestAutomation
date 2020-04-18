package com.dyteam.testApps.webserver.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.validation.ValidationException;

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

import com.dyteam.testApps.webserver.Util;
import com.dyteam.testApps.webserver.entity.Environment;
import com.dyteam.testApps.webserver.entity.ExecutionResults;
import com.dyteam.testApps.webserver.entity.LogicalGroup;
import com.dyteam.testApps.webserver.entity.ScheduledExecution;
import com.dyteam.testApps.webserver.entity.Testcases;
import com.dyteam.testApps.webserver.model.ExecutionResultsUI;
import com.dyteam.testApps.webserver.model.ExecutionUiBean;
import com.dyteam.testApps.webserver.model.LogicalGroupExecute;
import com.dyteam.testApps.webserver.model.LogicalGroupView;
import com.dyteam.testApps.webserver.repository.LogicalGroupRepository;
import com.dyteam.testApps.webserver.repository.ScheduledExecutionRepository;
import com.dyteam.testApps.webserver.repository.TestcasesRepository;
import com.dyteam.testApps.webserver.security.LoginUser;
import com.dyteam.testApps.webserver.service.IExecutionResultsService;

/**
 * This controller takes care of handling all operations related to Logical group
 * @author deepak
 */
@RestController
@RequestMapping("/logicalGroup")
public class LogicalGroupController {

	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
    @Autowired
    LogicalGroupRepository logicalGroupRepo;
    
    @Autowired
    ScheduledExecutionRepository scheduledExecutionRepo;
    
    @Autowired
	IExecutionResultsService executionResultsService;
    
    @Autowired
    TestcasesRepository testCaseRepo;
    
    @GetMapping("/{logicalGroupId}")
    public LogicalGroup findById(@PathVariable(value="logicalGroupId") Long logicalGroupId) {
    	logger.info("get LogicalGroup by id="+logicalGroupId);
        return logicalGroupRepo.findById(logicalGroupId).orElse(null);
    }
    
    @GetMapping("/all")
    public Iterable<LogicalGroup> findAll() {
    	logger.info("get all logicalGroups");
        return logicalGroupRepo.findAll();
    }
    
    /**
     * Fetches all Logical group for a Company
     * @param loggedInUser
     * @return
     */
    @GetMapping("/allByCompany")
    public List<ExecutionUiBean> findAllByCompany(@AuthenticationPrincipal final LoginUser loggedInUser) {
    	logger.info("get all logicalGroups by Company");
    	Iterable<Object[]> findAllByCompanyId = logicalGroupRepo.findAllByCompanyId(loggedInUser.getCompanyId());
    	List<ExecutionUiBean> executionUiBeans = ((ArrayList<Object[]>)findAllByCompanyId).stream().
        map(o -> new ExecutionUiBean((LogicalGroup)o[0],(String)o[1],(String)o[2],(String)o[3],(String)o[4],(Long)o[5])).collect(Collectors.toList());
        
		return executionUiBeans;
    }
    /**
     * Logical group View action data. 
     * @param logicalGroupId
     * @return
     */
    @GetMapping("/view/{logicalGroupId}")
    public List<LogicalGroupView> view(@PathVariable(value="logicalGroupId") final Long logicalGroupId) {
    	logger.info("get all logicalGroups by Company");
    	Iterable<Object[]> view =  logicalGroupRepo.getDetails(logicalGroupId);
    	List<LogicalGroupView> list = new ArrayList<>();
    	view.forEach( o -> {
    		list.add(new LogicalGroupView((String)o[0], (String)o[1], (String)o[2], (String)o[3]));
		});
        return list;
    }
    
    /**
     * Triggers the execution of the Logical group
     * @param logicalGroupExecute
     * @param loggedInUser
     * @return
     */
    @PostMapping("/execute")
    public Iterable<ExecutionResults> execute(@RequestBody final LogicalGroupExecute logicalGroupExecute,
    		@AuthenticationPrincipal final LoginUser loggedInUser) {
    	logger.info("get all logicalGroups by Company");
    	List<Long> logicalGroupId = logicalGroupExecute.getLogicalGroupId();
    	Iterable<LogicalGroup> loicalGroups = logicalGroupRepo.findAllById(logicalGroupId);
    	List<ExecutionResultsUI> executionResultsUIs = new ArrayList<>();
    	loicalGroups.forEach( lg -> {
    		List<Long> testcaseIds = ((List<Testcases>)lg.getTestCaseList()).stream().map( tc -> tc.getTestcasesId()).collect(Collectors.toList());
    		List<String> testcaseNames = ((List<Testcases>)lg.getTestCaseList()).stream().map( tc -> tc.getTestcaseName()).collect(Collectors.toList());
    		ExecutionResultsUI executionResultsUI = new ExecutionResultsUI();
    		executionResultsUI.setEnvironmentId(lg.getEnvironment().getEnvironmentId());
    		executionResultsUI.setEnvironmentName(lg.getEnvironment().getEnvironmentName());
    		executionResultsUI.setBrowserId(logicalGroupExecute.getBrowserId());
    		executionResultsUI.setBrowserName(logicalGroupExecute.getBrowserName());
    		executionResultsUI.setExecutionName(logicalGroupExecute.getName());
    		executionResultsUI.setTestcaseIds(testcaseIds);
    		executionResultsUI.setTestcaseNames(testcaseNames);
    		executionResultsUI.setScheduled(false);
    		executionResultsUI.setExecutionUserId(lg.getExecutionUserId());
    		executionResultsUIs.add(executionResultsUI);
    	});
    	
    	Iterable<ExecutionResults> executeAll = executionResultsService.executeAll(executionResultsUIs, loggedInUser, null, logicalGroupExecute.getName(), true);
    	
        return executeAll;
    }
    
    /**
     * Create a Schedule out of 1 or more Logical groups
     * @param logicalGroupExecute
     * @param loggedInUser
     * @return
     */
    @PostMapping("/schedule")
    public List<ScheduledExecution> schedule(@RequestBody final LogicalGroupExecute logicalGroupExecute,
    		@AuthenticationPrincipal final LoginUser loggedInUser) {
    	logger.info("schedule all logicalGroups ");
    	List<Long> logicalGroupId = logicalGroupExecute.getLogicalGroupId();
    	Iterable<LogicalGroup> loicalGroups = logicalGroupRepo.findAllById(logicalGroupId);
    	Long runnerId = Util.getEpochTimeInMillis();
    	List<ScheduledExecution> executionResultsUIs = new ArrayList<>();
    	loicalGroups.forEach( lg -> {
    		ScheduledExecution scheduledExecution = new ScheduledExecution();
    		scheduledExecution.setCompanyId(loggedInUser.getCompanyId());
    		scheduledExecution.setEnvironment(lg.getEnvironment());
    		scheduledExecution.setBrowserId(logicalGroupExecute.getBrowserId());
    		scheduledExecution.setExecutionUserId(lg.getExecutionUserId());
    		scheduledExecution.setScheduledById(loggedInUser.getUserId());
    		scheduledExecution.setScheduledDate(logicalGroupExecute.getScheduledDate());
    		scheduledExecution.setScheduledExecutionName(logicalGroupExecute.getName());
    		scheduledExecution.setStatus(true);
    		scheduledExecution.setTestCaseList(new ArrayList<>(lg.getTestCaseList()));
    		scheduledExecution.setRunnerId(runnerId);
    		executionResultsUIs.add(scheduledExecutionRepo.save(scheduledExecution));
    	});
        return executionResultsUIs;
    }
    
    /**
     * Create , clone or update the Logical group object
     * @param logicalGroup
     * @param isClone
     * @param loggedInUser
     * @return
     */
    @PostMapping("/save/{isClone}")
    public LogicalGroup save(@RequestBody LogicalGroup logicalGroup,
    		@PathVariable(value="isClone") Boolean isClone,
    		@AuthenticationPrincipal final LoginUser loggedInUser) {
    	logger.info("save logicalGroup = "+logicalGroup);
    	
    	String executionName = logicalGroup.getLogicalGroupName();
		boolean nameExists = logicalGroupRepo.nameExists(executionName,loggedInUser.getCompanyId());
		if(nameExists) {
			throw new ValidationException("Testset name already exists.");
		}
    	
    	if(null!=isClone && isClone) {
    		LogicalGroup logicalGroupDB = logicalGroupRepo.findById(logicalGroup.getLogicalGroupId()).get();
    		logicalGroup.setLogicalGroupId(null);
    		if(null!=logicalGroupDB.getTestCaseList() && !logicalGroupDB.getTestCaseList().isEmpty()) {
    			List<Testcases> testcases =  new ArrayList<>();
    			logicalGroupDB.getTestCaseList().forEach(t -> {
    				testcases.add(new Testcases(t.getTestcasesId()));
    			});
    			logicalGroup.setTestCaseList(testcases);
    		}
    		logicalGroup.setStatus(logicalGroupDB.getStatus());
    	} else {
    		logicalGroup.setStatus(true);
    	}
    	
    	if(null==logicalGroup.getTestCaseList() || logicalGroup.getTestCaseList().isEmpty()) {
    		throw new ValidationException("Please select at least one Test case.");
    	}
    	
    	logicalGroup.setCompanyId(loggedInUser.getCompanyId());
    	logicalGroup.setUserId(loggedInUser.getUserId());
        return logicalGroupRepo.save(logicalGroup);
    }
    
    @DeleteMapping("/{logicalGroupId}")
    public Boolean delete(@PathVariable(value="logicalGroupId") Long logicalGroupId) {
    	logicalGroupRepo.deleteById(logicalGroupId);
		return true;
    }
    
    @GetMapping("/getSample")
    public LogicalGroup getSampleLogicalGroup() {
    	LogicalGroup logicalGroup = new LogicalGroup();
    	logicalGroup.setCompanyId(1l);
    	logicalGroup.setEnvironment(new Environment(1l));
    	logicalGroup.setLogicalGroupName("logicalGroupName");
    	List<Testcases> testCaseList = new ArrayList<>();
    	testCaseList.add(new Testcases(1l));
    	testCaseList.add(new Testcases(2l));
		logicalGroup.setTestCaseList(testCaseList);
    	return logicalGroup;
    }
    
   
    
}
