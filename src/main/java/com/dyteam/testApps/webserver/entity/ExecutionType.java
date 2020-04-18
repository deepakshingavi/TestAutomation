package com.dyteam.testApps.webserver.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Table(name = "execution_type")
@EntityListeners(AuditingEntityListener.class)
public class ExecutionType{
	
	@Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="execution_type_id")
	private Long executionTypeId;
	
	@Column(name="execution_type_name")
	@NotNull
	private String executionTypeName;

	public ExecutionType() {
	}
	
	public ExecutionType(Long executionTypeId, String executionTypeName) {
		this.executionTypeId = executionTypeId;
		this.executionTypeName = executionTypeName;
	}

	public Long getExecutionTypeId() {
		return executionTypeId;
	}

	public void setExecutionTypeId(Long executionTypeId) {
		this.executionTypeId = executionTypeId;
	}

	public String getExecutionTypeName() {
		return executionTypeName;
	}

	public void setExecutionTypeName(String executionTypeName) {
		this.executionTypeName = executionTypeName;
	}
	
	

}
