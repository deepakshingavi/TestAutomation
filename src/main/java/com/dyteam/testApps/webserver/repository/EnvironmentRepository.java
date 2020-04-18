package com.dyteam.testApps.webserver.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.dyteam.testApps.webserver.entity.Environment;

@Repository
public interface EnvironmentRepository extends CrudRepository<Environment, Long>{

	@Query("select e "
			+ "from Environment e where e.userId = :userId")
	List<Environment> findAllByUserId(Long userId);

	@Query("select e from Environment e where e.companyId = :companyId")
	Iterable<Environment> findAll(Long companyId);

	void deleteByCompanyId(Long companyId);
}
