package com.dyteam.testApps.webserver.service;

import com.dyteam.testApps.webserver.entity.ScheduledExecutionBk;

public interface IExecuteScheduledJobs extends Runnable{

	void setScheduledExecutionBkJobs(Iterable<ScheduledExecutionBk> scheduledExecutionBkJobs);

}
