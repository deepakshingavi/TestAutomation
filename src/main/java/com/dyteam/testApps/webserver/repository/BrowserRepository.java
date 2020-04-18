package com.dyteam.testApps.webserver.repository;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.dyteam.testApps.webserver.entity.Browser;

@Repository
public interface BrowserRepository extends CrudRepository<Browser, Long>{
	
	@Modifying
	@Query("update Browser b set b.browserName = :browserName where b.browserId = :browserId")
	Integer update(String browserName, Long browserId);

	Iterable<Browser> findAllByCompanyId(Long companyId);

	void deleteByCompanyId(Long companyId);
	
}
