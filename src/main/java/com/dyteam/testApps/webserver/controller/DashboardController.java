package com.dyteam.testApps.webserver.controller;

import java.math.BigInteger;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dyteam.testApps.webserver.model.ApplicationTestcaseInfo;
import com.dyteam.testApps.webserver.model.Dashboard;
import com.dyteam.testApps.webserver.model.EXECUTION_RESULT;
import com.dyteam.testApps.webserver.model.LatestExecutionInfo;
import com.dyteam.testApps.webserver.model.LatestExecutionSummary;
import com.dyteam.testApps.webserver.model.TopFailedTestInfo;
import com.dyteam.testApps.webserver.repository.ExecutionResultsRepository;
import com.dyteam.testApps.webserver.repository.TestcasesRepository;
import com.dyteam.testApps.webserver.security.LoginUser;

/**
 * This controller takes care of handling all operations related to Dashboard reports 
 * @author deepak
 */
@RestController
@RequestMapping("/dashboard")
public class DashboardController {

	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	
	@Autowired
	ExecutionResultsRepository executionResultsRepo;
	
	@Autowired
	TestcasesRepository testCasesRepo;
    
	/**
	 * Get Dashboard object consisting of
	 * 	1. Application test case info
	 *  2. Get latest execution summary object
	 * @param loggedInUser
	 * @return
	 */
    @GetMapping("/")
    public Dashboard getDashboard(@AuthenticationPrincipal final LoginUser loggedInUser) {
    	Dashboard dashboard = null;
		try {
			boolean isSuperDashbboard= false;
			if(0l==loggedInUser.getCompanyId()) {
				logger.info("Super user dashboard");
				isSuperDashbboard= true;
			}
			List<ApplicationTestcaseInfo> appTestCaseInfoList = isSuperDashbboard ? testCasesRepo.getSuperDashboardDetails()
																					:testCasesRepo.getDashboardDetails(loggedInUser.getCompanyId());
			
			
			List<LatestExecutionInfo> latestTestCaseExecutionSummary = isSuperDashbboard ? executionResultsRepo.getSuperLatestTestCaseExecutionSummary()
																	:executionResultsRepo.getLatestTestCaseExecutionSummary(loggedInUser.getCompanyId());
			
			if(null!=latestTestCaseExecutionSummary && !latestTestCaseExecutionSummary.isEmpty()) {
				LatestExecutionSummary latestExecutionSummary  = new LatestExecutionSummary();
				latestTestCaseExecutionSummary.stream().forEach(u -> {
					latestExecutionSummary.setExecutedBy(u.getExecutedBy());
					latestExecutionSummary.setExecutedOn(u.getExecutedOn());
					EXECUTION_RESULT valueOf = EXECUTION_RESULT.valueOf(u.getResult().toUpperCase());
					switch(valueOf) {
					case PASSED:
						latestExecutionSummary.setPassedTestCasesCount(u.getTestCasesExecutedCount());
						break;
					case FAILED:
						latestExecutionSummary.setFailedTestCasesCount(u.getTestCasesExecutedCount());
						break;
					case QUEUED:
						latestExecutionSummary.setQueuedTestCasesCount(u.getTestCasesExecutedCount());
					case SUBMITED:
						break;
					default:
						break;
					}
					latestExecutionSummary.setRunName(u.getRunName());
				});
				dashboard = new Dashboard(appTestCaseInfoList,latestExecutionSummary);
			} else {
				dashboard = new Dashboard(appTestCaseInfoList,null);
			}
			
		} catch (Exception e) {
			logger.error("Dashboard cannot be feched for companyId="+loggedInUser.getCompanyId(),e);
		}
		logger.info("dashboard="+dashboard);
        return dashboard;
    }
    
    /**
     * Fetches Top ten failed test cases
     * @param loggedInUser
     * @return
     */
    @GetMapping("/getTopTenFailedTestCases")
    public List<TopFailedTestInfo> getTopTenFailedTestCases(@AuthenticationPrincipal final LoginUser loggedInUser) {
    	Iterable<Object[]>  topTenFailedTestCase = null;
    	if(0l==loggedInUser.getCompanyId()) {
    		topTenFailedTestCase = executionResultsRepo.topTenFailedTestCase( EXECUTION_RESULT.FAILED.toString());
    	}else {
    		topTenFailedTestCase = executionResultsRepo.topTenFailedTestCase(loggedInUser.getCompanyId(), EXECUTION_RESULT.FAILED.toString());
    	}
    	List<TopFailedTestInfo> collect = ((List<Object[]>)topTenFailedTestCase).stream().map(o->new TopFailedTestInfo((String)o[0], (BigInteger)o[1])).collect(Collectors.toList());
    	
    	return collect;
    }
}
