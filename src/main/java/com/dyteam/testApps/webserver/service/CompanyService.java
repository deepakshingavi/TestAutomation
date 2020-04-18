package com.dyteam.testApps.webserver.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dyteam.testApps.webserver.entity.Company;
import com.dyteam.testApps.webserver.entity.User;
import com.dyteam.testApps.webserver.repository.ApplicationRepository;
import com.dyteam.testApps.webserver.repository.BrowserRepository;
import com.dyteam.testApps.webserver.repository.CompanyEnvironUrlRepository;
import com.dyteam.testApps.webserver.repository.CompanyRepository;
import com.dyteam.testApps.webserver.repository.EnvironmentRepository;
import com.dyteam.testApps.webserver.repository.ExecutionResultsRepository;
import com.dyteam.testApps.webserver.repository.LogicalGroupRepository;
import com.dyteam.testApps.webserver.repository.ScheduledExecutionBkRepository;
import com.dyteam.testApps.webserver.repository.ScheduledExecutionRepository;
import com.dyteam.testApps.webserver.repository.TestcasesRepository;
import com.dyteam.testApps.webserver.repository.UserRepository;
import com.dyteam.testApps.webserver.security.LoginUser;
import com.dyteam.testApps.webserver.security.ROLE;

/**
 * 
 * @author deepak
 */
@Service
public class CompanyService implements ICompanyService{

	private CompanyRepository companyRepo;
	private UserRepository userRepo;
	private String key;
	private PasswordEncoder passwordEncoder;
	
	@Autowired
	BrowserRepository browserRepo;
	
	@Autowired
	ApplicationRepository applicationRepo;
	
	@Autowired
	EnvironmentRepository environmentRepo;
	
	@Autowired
	TestcasesRepository testcasesRepo;

	@Autowired
	CompanyEnvironUrlRepository companyEnvironUrlRepository;
	
	@Autowired
	ExecutionResultsRepository executionResultsRepository;
	
	@Autowired
	LogicalGroupRepository logicalGroupRepository;
	
	@Autowired
	ScheduledExecutionRepository scheduledExecutionRepository;
	
	@Autowired
	ScheduledExecutionBkRepository scheduledExecutionBkRepository;
	
	
	public CompanyService(@Autowired CompanyRepository companyRepo, @Autowired UserRepository userRepo,
			@Autowired PasswordEncoder passwordEncoder,
			@Value("${email.password.key}") String key) {
		this.companyRepo=companyRepo;
		this.userRepo=userRepo;
		this.passwordEncoder=passwordEncoder;
		this.key=key;
	}
	
	/**
	 * Create or updates Company
	 * For new Company create a Default User with Admin role and default Username & Password while saving company. 
	 */
	@Transactional
	public Company save(Company company, LoginUser loggedInUser) {
		String rawPassword = company.getPassword();
		String encodedPassword = companyRepo.getEncodedPassword(rawPassword, key);
		company.setPassword(encodedPassword);
		company.setBatchFileHome("");
		company.setLogsHome("");
		company.setScreenShotsHome("");
		company.setSeleniumHome("");
		company.setTestDataHome("");
		
		Company save = companyRepo.save(company);
		if (null != save && null != save.getCompanyId()) {
			User user= new User();
			user.setCompanyId(save.getCompanyId());
			user.setUserType(ROLE.ADMIN.getRoleId());
			user.setRefUserId(loggedInUser.getUserId());
			user.setAddedBy(loggedInUser.getUserId());
			user.setEmail(save.getEmail());
			user.setUserName(save.getUsername());
			user.setPassword(passwordEncoder.encode(rawPassword));
			user.setContact(save.getContact());
			user.setAddress(save.getAddress());
			user.setStatus(1);
			user.setfName("");
			user.setlName("");
			userRepo.save(user);
		}
		return save;
	}

	/**
	 * Delete company and its related entities
	 */
	@Override
	@Transactional
	public boolean delete(Long companyId) {
		companyRepo.deleteById(companyId);
		userRepo.deleteByCompanyId(companyId);
		logicalGroupRepository.deleteByCompanyId(companyId);
		scheduledExecutionRepository.deleteByCompanyId(companyId);
		scheduledExecutionBkRepository.deleteByCompanyId(companyId);
		executionResultsRepository.inactivateByCompanyId(companyId);
		companyEnvironUrlRepository.deleteByCompanyId(companyId);
		testcasesRepo.deleteByCompanyId(companyId);
		browserRepo.deleteByCompanyId(companyId);
		applicationRepo.deleteByCompanyId(companyId);
		environmentRepo.deleteByCompanyId(companyId);
		return true;
	}

	
	
}
