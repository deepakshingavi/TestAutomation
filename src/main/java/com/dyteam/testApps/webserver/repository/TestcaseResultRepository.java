package com.dyteam.testApps.webserver.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.dyteam.testApps.webserver.entity.TestcaseResult;

@Repository
public interface TestcaseResultRepository extends CrudRepository<TestcaseResult, Long>{
}
