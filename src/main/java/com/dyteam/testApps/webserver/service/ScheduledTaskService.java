package com.dyteam.testApps.webserver.service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.dyteam.testApps.webserver.entity.ScheduledExecution;
import com.dyteam.testApps.webserver.entity.ScheduledExecutionBk;
import com.dyteam.testApps.webserver.repository.ScheduledExecutionBkRepository;
import com.dyteam.testApps.webserver.repository.ScheduledExecutionRepository;

@Component
/**
 * Triggers the execution for Schedule task and arhives them 
 * @author deepak
 */
public class ScheduledTaskService {
	
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	ScheduledExecutionRepository scheduledExecutionRepo; 
	
	@Autowired
	ScheduledExecutionBkRepository scheduledExecutionBkRepo;
	
    @Autowired
    TaskExecutor taskExecutor;
    
    @Value("${fixedRate.in.milliseconds}")
    Long scheduleJobInterval;
    
    @Autowired
    IExecuteScheduledJobs executeScheduledJobs;
	
    @Scheduled(fixedRateString= "${fixedRate.in.milliseconds}")
    public void executionScheduler() throws InterruptedException{
    	Date date = new Date();
		LocalDateTime now = LocalDateTime.now();
    	Date from = Date.from(now.atZone(ZoneId.systemDefault()).toInstant());
    	logger.info("Schedule execute job started at "+date+ " scheduling jobs before "+from);
    	
		archive(date);
    }
    
    @Transactional
    /**
     * Arhives the Scheduled Test cases result and trigger there execution.
     * @param from
     */
	private void archive(Date from) {
    	Iterable<ScheduledExecution> scheduledExecutionJobs = scheduledExecutionRepo.findByScheduledDateBefore(from);
    	logger.info("Scheduled jobs : "+scheduledExecutionJobs);
    	logger.info("Scheduled jobs archive start..");
    	List<ScheduledExecutionBk> scheduledExecutionBkJobs = ((List<ScheduledExecution>)scheduledExecutionJobs).stream().map(s->{
    		ScheduledExecutionBk save = scheduledExecutionBkRepo.save(new ScheduledExecutionBk(null, 
    				s.getScheduledExecutionName(), s.getScheduledDate(),
    				s.getBrowserId(), s.getEnvironment(), s.getScheduledById(), s.getCompanyId(),
    				s.getCreatedAt(), s.getTestCaseList(),s.getExecutionUserId()));
    		save.setRunnerId(0l);
    		scheduledExecutionRepo.deleteById(s.getScheduledExecutionId());
    		return save;
    	}).collect(Collectors.toList());
    	logger.info("Scheduled jobs archived..");
    	
    	logger.info("Scheduled jobs execution start.." + scheduledExecutionBkJobs);
    	
    	logger.info("executeScheduledJobs=="+executeScheduledJobs);
    	
    	executeScheduledJobs.setScheduledExecutionBkJobs(scheduledExecutionBkJobs);
    	taskExecutor.execute(executeScheduledJobs);
    	logger.info("Scheduled jobs submitted..");
	}

}
