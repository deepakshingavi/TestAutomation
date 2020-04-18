package com.dyteam.testApps.webserver.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.dyteam.testApps.webserver.entity.TestcaseExeDetail;

@Repository
public interface TestcaseExeDetailRepository extends CrudRepository<TestcaseExeDetail, Long>{

	
//	Iterable<TestcaseExeDetail> findAllForRunnerAndTest(Long runnerId, Long testcaseId);
}
