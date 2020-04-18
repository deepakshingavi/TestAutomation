package com.dyteam.testApps.webserver.service;

import java.io.File;
import java.io.IOException;
import java.util.List;

import com.dyteam.testApps.webserver.entity.ExecutionResults;
import com.dyteam.testApps.webserver.entity.ScheduledExecution;
import com.dyteam.testApps.webserver.entity.ScheduledExecutionBk;
import com.dyteam.testApps.webserver.model.ExecutionResultsUI;
import com.dyteam.testApps.webserver.security.LoginUser;

public interface IExecutionResultsService {


	void save(ScheduledExecution scheduledExecution);

	Iterable<ExecutionResults> executeAll(List<ExecutionResultsUI> executionResultsUIs, LoginUser loggedInUser,
			ScheduledExecutionBk scheduledExecutionBk, String executionName, boolean isLogical);


	byte[] getZipIS(File dirToZip) throws  IOException;

	boolean reRunFailedTests(Long runnerId, LoginUser loggedInUser);

}
