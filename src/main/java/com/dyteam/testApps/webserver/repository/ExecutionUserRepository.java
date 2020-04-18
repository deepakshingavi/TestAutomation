package com.dyteam.testApps.webserver.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.dyteam.testApps.webserver.entity.ExecutionUser;

@Repository
public interface ExecutionUserRepository extends CrudRepository<ExecutionUser, Long>{

	Iterable<ExecutionUser> findAllByCompanyId(Long companyId);
	
}
