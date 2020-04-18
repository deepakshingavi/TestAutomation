package com.dyteam.testApps.webserver.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.dyteam.testApps.webserver.entity.Application;

@Repository
public interface ApplicationRepository extends CrudRepository<Application, Long>{

	@Query("select app from Application app where app.userId = :userId")
	List<Application> findAllByUserId(Long userId);
	
	@Query("select app from Application app where app.companyId = :companyId")
	List<Application> findAllByCompanyIdId(Long companyId);
	
	@Query("select app.applicationName from Application app where app.companyId = :companyId")
	List<String> findAllAppNamesByCompanyId(Long companyId);

	Iterable<Application> findAllByCompanyId(Long companyId);

	void deleteByCompanyId(Long companyId);
	
}
