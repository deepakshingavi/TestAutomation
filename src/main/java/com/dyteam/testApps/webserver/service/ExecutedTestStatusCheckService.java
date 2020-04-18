package com.dyteam.testApps.webserver.service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.dyteam.testApps.webserver.Util;
import com.dyteam.testApps.webserver.entity.Company;
import com.dyteam.testApps.webserver.model.ExecutionResultRunnerInfo;
import com.dyteam.testApps.webserver.model.LatestExecutionExtraSummary;
import com.dyteam.testApps.webserver.model.LatestExecutionSummary;
import com.dyteam.testApps.webserver.repository.CompanyRepository;
import com.dyteam.testApps.webserver.repository.ExecutionResultsRepository;

@Component
public class ExecutedTestStatusCheckService {
	
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
    IEmailService emailService;
	
	@Autowired
	ExecutionResultsRepository executionResultsRepo;
	
	@Autowired
	CompanyRepository companyRepo;
	
    @Autowired
    TaskExecutor taskExecutor;
    
    @Scheduled(fixedRateString= "${testcase.status.check.milliseconds}")
    public void executionScheduler() throws InterruptedException{
    	Date date = new Date();
    	logger.info("Test case Result check job started at "+date);
		checkAndEmail();
    }
    
    /**
     * Checks if the email has been sent for the finished Execution test cases. If no then triggers the email.
     */
    @Transactional
	private void checkAndEmail() {
    	logger.info("in checkAndEmail");
    	Iterable<ExecutionResultRunnerInfo> findAllNonEmailedRunnerStatus = executionResultsRepo.
    			findAllNonEmailedRunnerStatus();
    	
//    	logger.info("In progess Test case execution : "+findAllNonEmailedRunnerStatus);
    	
    	if (null != findAllNonEmailedRunnerStatus) {
			Map<Long, LatestExecutionSummary> map = new HashMap<>();
			Util.populateResults((List<ExecutionResultRunnerInfo>) findAllNonEmailedRunnerStatus, map, false);
			 map.values().stream().filter( latestExeSummary -> latestExeSummary.getProgess().intValue()==100).map(r->r.getRunId()).forEach( runnerId-> {
				 try {
					LatestExecutionExtraSummary expandedER = executionResultsRepo.getExpandedER(runnerId);
					 Iterable<Object[]> testCaseInfo = executionResultsRepo.getTestCaseNameResult(runnerId);
					 Company company = companyRepo.emailSettingById(expandedER.getCompanyId());
					 String emailTable = Util.getTestCaseResultHtml(expandedER,testCaseInfo);
					 emailService.sendSimpleMessage(expandedER.getExecutedByEmail(), "Execution Completed", 
							 emailTable, company);
				} catch (Exception e) {
					logger.error("Error while sending email for runnerId="+runnerId,e);
				}
				 executionResultsRepo.updateEmailSent(runnerId);
			 });
		}
	}

}
