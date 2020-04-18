package com.dyteam.testApps.webserver.controller;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dyteam.testApps.webserver.Util;
import com.dyteam.testApps.webserver.entity.Company;
import com.dyteam.testApps.webserver.repository.CompanyRepository;
import com.dyteam.testApps.webserver.security.LoginUser;
import com.dyteam.testApps.webserver.service.ICompanyService;

/**
 * 
 * @author deepak
 * This controller takes care of handling all operations related to company
 */
@RestController
@RequestMapping("/company")
public class CompanyController {

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	
	private ICompanyService companyService;
	
	private CompanyRepository companyRepo;
	private String key;
	
	private String projectBasePath;


	private String seleniumHomePath;

	public CompanyController(@Autowired CompanyRepository companyRepo,
			 @Autowired ICompanyService companyService,
			 @Value("${email.password.key}") String key,
			 @Value("${project.base.path}") String projectBasePath,
			 @Value("${selenium.home.default.path}") String seleniumHomePath) {
		this.companyRepo = companyRepo;
		this.companyService=companyService;
		this.key = key;
		this.projectBasePath=projectBasePath;
		this.seleniumHomePath=seleniumHomePath;
	}

	@GetMapping("/{companyId}")
	public Company findById(@PathVariable(value = "companyId") Long companyId) {
		logger.info("get Company by id=" + companyId);
		Optional<Company> findById = companyRepo.findById(companyId);
		if (findById.isPresent()) {
			Company company = findById.get();
			String finalPwd = Util.getString(companyRepo.getDecodePassword(company.getPassword(), key));
			company.setPassword(finalPwd);
		}
		return findById.orElse(null);
	}

	/**
	 * Fetch Company object with limited attribute/ column values.
	 * @param companyId
	 * @return
	 */
	@GetMapping("/basicDetails/{companyId}")
	public Company basicDetailsById(@PathVariable(value = "companyId") Long companyId) {
		logger.info("get Company by id=" + companyId);
		Optional<Company> findById = companyRepo.basicDetailsById(companyId);
		return findById.orElse(null);
	}

	@GetMapping("/all")
	public Iterable<Company> findAll() {
		logger.info("get all companys");
		return companyRepo.findAll();
	}

	@PostMapping("/save")
	public Company save(@RequestBody Company company,@AuthenticationPrincipal final LoginUser loggedInUser) {
		logger.info("save company = " + company);
		boolean isNew = null==company.getCompanyId();
		company.setUserId(loggedInUser.getUserId());
		company = companyService.save(company,loggedInUser);
		if(isNew) {
			try {
				Util.createFolders(Paths.get(projectBasePath,Util.COMPANIES_BASE_FOLDER_NAME,company.getCompanyName()));
				Util.createFolders(Paths.get(seleniumHomePath,Util.COMPANIES_BASE_FOLDER_NAME,
							company.getCompanyName()));
				
			} catch (IOException e) {
				logger.error("Error occure while creating dir structor for company="+company,e);
			}
		}
		return company;
	}

	@DeleteMapping("/{companyId}")
	public Boolean delete(@PathVariable(value = "companyId") Long companyId) {
		return companyService.delete(companyId);
	}

	/**
	 * Reset email setting of the company
	 * @param companyId
	 * @return
	 */
	@GetMapping("/emailSetting/{companyId}")
	public Company emailSettingById(@PathVariable(value = "companyId") Long companyId) {
		Company company = companyRepo.emailSettingById(companyId);
		String password = company.getPassword();
		String decodePassword = Util.getString(companyRepo.getDecodePassword(password, key));
		company.setPassword(decodePassword);
		return company;
	}

	@GetMapping("/applicationPath/{companyId}")
	public Company applicationPathById(@PathVariable(value = "companyId") Long companyId) {
		Company findById = companyRepo.applicationPathById(companyId);
		return findById;
	}

	/**
	 * Reset email setting of the company
	 * Encode the incoming Company' email password and save in DB
	 * @param companyId
	 * @return
	 */
	@PostMapping("/save/emailSetting")
	public Integer saveEmailSettings(@RequestBody Company company,
			@AuthenticationPrincipal final LoginUser loggedInUser) {
		Integer findById = null;
		try {
			String password = company.getPassword();
			String encodedPassword = companyRepo.getEncodedPassword(password, key);
			findById = companyRepo.saveEmailSettingById(company.getHostName(), company.getPort(), company.getEmail(),
					company.getUsername(), company.getSecuirityProtocol(), encodedPassword, company.getCompanyId());
		} catch (Exception e) {
			logger.error("Error while saving emailSetting=" + company, e);
		}
		return findById;
	}

	/**
	 * Update company attributes like contact & address
	 * @param company
	 * @param loggedInUser
	 * @return
	 */
	@PostMapping("/update")
	public Integer update(@RequestBody Company company, @AuthenticationPrincipal final LoginUser loggedInUser) {
		Integer findById = null;
		try {
			findById = companyRepo.update(company.getContact(), company.getAddress(), company.getCompanyId());
		} catch (Exception e) {
			logger.error("Error while saving emailSetting=" + company, e);
		}
		return findById;
	}

	/**
	 * Update Company's Selenium home,test data, screen shot ,batch file and logs home path
	 * @param company
	 * @param loggedInUser
	 * @return
	 */
	@PostMapping("/save/applicationPath")
	public Integer saveApplicationPath(@RequestBody Company company,
			@AuthenticationPrincipal final LoginUser loggedInUser) {
		Integer findById = null;
		try {
			findById = companyRepo.saveApplicationPathById(company.getSeleniumHome(), company.getTestDataHome(),
					company.getScreenShotsHome(), company.getBatchFileHome(), company.getLogsHome(),
					company.getCompanyId());
			
			if(StringUtils.isNotBlank(company.getSeleniumHome())) {
				Util.moveFolders(Paths.get(seleniumHomePath,Util.COMPANIES_BASE_FOLDER_NAME,company.getCompanyName()),
						Paths.get(company.getSeleniumHome(),Util.COMPANIES_BASE_FOLDER_NAME,
								company.getCompanyName()));
			}
			
		} catch (Exception e) {
			logger.error("Error while saving applicationPath=" + company, e);
		}
		return findById;
	}

}
