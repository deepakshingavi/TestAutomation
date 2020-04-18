package com.dyteam.testApps.webserver.service;

import com.dyteam.testApps.webserver.entity.Company;
import com.dyteam.testApps.webserver.security.LoginUser;

public interface ICompanyService {

	Company save(Company company, LoginUser loggedInUser);

	boolean delete(Long companyId);

}
