package com.dyteam.testApps.webserver.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.dyteam.testApps.webserver.entity.Browser;
import com.dyteam.testApps.webserver.entity.ExecutionResults;
import com.dyteam.testApps.webserver.entity.ScheduledExecutionBk;
import com.dyteam.testApps.webserver.entity.User;
import com.dyteam.testApps.webserver.model.ExecutionResultsUI;
import com.dyteam.testApps.webserver.repository.BrowserRepository;
import com.dyteam.testApps.webserver.repository.EnvironmentRepository;
import com.dyteam.testApps.webserver.repository.ScheduledExecutionBkRepository;
import com.dyteam.testApps.webserver.repository.TestcasesRepository;
import com.dyteam.testApps.webserver.repository.UserRepository;
import com.dyteam.testApps.webserver.security.LoginUser;

/**
 * Cron service runs and executes the Schduled test cases
 * @author deepak
 */
@Service
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class ExecuteScheduledJobs implements IExecuteScheduledJobs {

	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	IExecutionResultsService executionResultsService;
	
	@Autowired
	EnvironmentRepository environmentRepo;
	
	@Autowired
	BrowserRepository browserRepo;
	
	@Autowired
	TestcasesRepository testcasesRepo;
	
	@Autowired
	ScheduledExecutionBkRepository scheduledExecutionBkRepo;
	
	@Autowired
	UserRepository userRepo;

	private Iterable<ScheduledExecutionBk> scheduledExecutionBkJobs;

	public ExecuteScheduledJobs() {
	}
	
	public ExecuteScheduledJobs(Iterable<ScheduledExecutionBk> scheduledExecutionBkJobs) {
		this.scheduledExecutionBkJobs=scheduledExecutionBkJobs;
	}
	
	public void setScheduledExecutionBkJobs(Iterable<ScheduledExecutionBk> scheduledExecutionBkJobs) {
		this.scheduledExecutionBkJobs = scheduledExecutionBkJobs;
	}
	
	public Iterable<ScheduledExecutionBk> getScheduledExecutionBkJobs() {
		return scheduledExecutionBkJobs;
	}

	/**
	 * Checks for matured Schduled test cases execution and executes them.
	 */
	@Override
	public void run() {
		if(null!=scheduledExecutionBkJobs) {
			Map<Long, List<ScheduledExecutionBk>> collect = ((List<ScheduledExecutionBk>)scheduledExecutionBkJobs).stream().
					collect(Collectors.groupingBy(ScheduledExecutionBk::getRunnerId));
			Iterable<ScheduledExecutionBk> scheduledExecutionBkJobsNormal = collect.remove(0l);
			if(null!=scheduledExecutionBkJobsNormal) {
				
				//This schedule runs for single scheduler jobs
				scheduledExecutionBkJobsNormal.forEach( scheduledExecutionBk -> {
					try {
						String environmentName = scheduledExecutionBk.getEnvironment().getEnvironmentName();
						String browserName="";
						try {
							logger.info("browserRepo=="+browserRepo);
							Browser browser = browserRepo.findById(scheduledExecutionBk.getBrowserId()).get();
							logger.info("browser=="+browser);
							browserName = browser.getBrowserName();
						} catch (Exception e) {
							logger.error("Error occured while fetching browser info with browserId="+scheduledExecutionBk.getBrowserId(),e);
							throw e;
						}
						List<String> testcaseNames = scheduledExecutionBk.getTestCaseList().stream().map(tc -> tc.getTestcaseName()).collect(Collectors.toList());
						List<Long> testcaseIds = scheduledExecutionBk.getTestCaseList().stream().map(tc -> tc.getTestcasesId()).collect(Collectors.toList());
						User user = userRepo.findById(scheduledExecutionBk.getScheduledById()).get();
						ExecutionResultsUI executionResultsUI = new ExecutionResultsUI(scheduledExecutionBk.getScheduledExecutionName(), scheduledExecutionBk.getEnvironment().getEnvironmentId(), scheduledExecutionBk.getBrowserId(), 
								testcaseIds, browserName, environmentName, null, testcaseNames,scheduledExecutionBk.getExecutionUserId(),true);
						List<ExecutionResultsUI> executionResultsUIs = new ArrayList<>();
						executionResultsUIs.add(executionResultsUI);
						Iterable<ExecutionResults> saveAll = executionResultsService.executeAll(executionResultsUIs, new LoginUser( scheduledExecutionBk.getScheduledById(),  scheduledExecutionBk.getCompanyId(),user.getUserName()),scheduledExecutionBk,
								scheduledExecutionBk.getScheduledExecutionName(),false);
						scheduledExecutionBkRepo.save(scheduledExecutionBk);
					} catch (Exception e) {
						logger.error("Error occured while fetching browser info with browserId="+scheduledExecutionBk.getBrowserId(),e);
					}
				});
			}
			
			//This schedule runs for Logically grouped scheduler jobs
			collect.values().forEach( scheduledExecutionBks -> {
				List<ExecutionResultsUI> executionResultsUIs = new ArrayList<>(scheduledExecutionBks.size());
				ScheduledExecutionBk scheduledExecutionBk = scheduledExecutionBks.get(0);
				try {
					User user = userRepo.findById(scheduledExecutionBk.getScheduledById()).get();

					logger.info("browserRepo=="+browserRepo);
					Browser browser = browserRepo.findById(scheduledExecutionBk.getBrowserId()).get();
					logger.info("browser=="+browser);
					String browserName = browser.getBrowserName();

					scheduledExecutionBks.forEach(scheduledExecutionBkInner -> {
						try {
							String environmentName = scheduledExecutionBkInner.getEnvironment().getEnvironmentName();
							List<String> testcaseNames = scheduledExecutionBkInner.getTestCaseList().stream().map(tc -> tc.getTestcaseName()).collect(Collectors.toList());
							List<Long> testcaseIds = scheduledExecutionBkInner.getTestCaseList().stream().map(tc -> tc.getTestcasesId()).collect(Collectors.toList());
							ExecutionResultsUI executionResultsUI = new ExecutionResultsUI(scheduledExecutionBkInner.getScheduledExecutionName(), scheduledExecutionBkInner.getEnvironment().getEnvironmentId(), 
									scheduledExecutionBkInner.getBrowserId(), 
									testcaseIds, browserName, environmentName, null, testcaseNames,scheduledExecutionBkInner.getExecutionUserId(),true);
							executionResultsUIs.add(executionResultsUI);
							scheduledExecutionBkRepo.save(scheduledExecutionBkInner);
						} catch (Exception e) {
							logger.error("Error occured while fetching browser info with browserId="+scheduledExecutionBkInner.getBrowserId(),e);
						}
					});
					Iterable<ExecutionResults> saveAll = executionResultsService.executeAll(executionResultsUIs, new LoginUser( scheduledExecutionBk.getScheduledById(),  
							scheduledExecutionBk.getCompanyId(),user.getUserName()),scheduledExecutionBk,
							scheduledExecutionBk.getScheduledExecutionName(),true);
				} catch (Exception e) {
					logger.error("Error occured executing scheduledExecutionBks="+scheduledExecutionBks,e);
					throw e;
				}
				
			});
		}
	}

}
