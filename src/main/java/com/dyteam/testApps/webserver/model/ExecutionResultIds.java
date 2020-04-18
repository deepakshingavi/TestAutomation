package com.dyteam.testApps.webserver.model;

import java.io.Serializable;
import java.util.List;

public class ExecutionResultIds implements Serializable {

	private static final long serialVersionUID = -668017904246172314L;
	private List<Long> executionResultIds;

	public List<Long> getExecutionResultIds() {
		return executionResultIds;
	}

	public void setExecutionResultIds(List<Long> executionResultIds) {
		this.executionResultIds = executionResultIds;
	}

}
