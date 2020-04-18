package com.dyteam.testApps.webserver.controller;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.mail.MessagingException;
import javax.validation.ValidationException;
import javax.websocket.server.PathParam;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dyteam.testApps.webserver.Util;
import com.dyteam.testApps.webserver.entity.Application;
import com.dyteam.testApps.webserver.entity.Company;
import com.dyteam.testApps.webserver.entity.ExecutionResults;
import com.dyteam.testApps.webserver.entity.TestcaseExeDetail;
import com.dyteam.testApps.webserver.entity.Testcases;
import com.dyteam.testApps.webserver.model.ExecutionResultIds;
import com.dyteam.testApps.webserver.model.ExecutionResultRunnerInfo;
import com.dyteam.testApps.webserver.model.ExecutionResultsUI;
import com.dyteam.testApps.webserver.model.LatestExecutionExtraSummary;
import com.dyteam.testApps.webserver.model.LatestExecutionSummary;
import com.dyteam.testApps.webserver.model.PerfUiBean;
import com.dyteam.testApps.webserver.model.TestcaseRunInfo;
import com.dyteam.testApps.webserver.repository.ApplicationRepository;
import com.dyteam.testApps.webserver.repository.CompanyEnvironUrlRepository;
import com.dyteam.testApps.webserver.repository.CompanyRepository;
import com.dyteam.testApps.webserver.repository.ExecutionResultsRepository;
import com.dyteam.testApps.webserver.repository.TestcasesRepository;
import com.dyteam.testApps.webserver.repository.UserRepository;
import com.dyteam.testApps.webserver.security.LoginUser;
import com.dyteam.testApps.webserver.service.IEmailService;
import com.dyteam.testApps.webserver.service.IExecutionResultsService;
import com.dyteam.testApps.webserver.view.GenerateExcelReport;
import com.dyteam.testApps.webserver.view.GeneratePdfReport;

/**
 * This controller takes care of handling all operations related to Execution result 
 * @author deepak
 */
@RestController
@RequestMapping("/executionResults")
public class ExecutionResultsController {

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	ExecutionResultsRepository executionResultsRepo;

	@Value("${project.base.path}")
	String projectBasePath;

	@Autowired
	TestcasesRepository testcasesRepo;

	@Autowired
	IExecutionResultsService executionResultsService;

	@Autowired
	CompanyRepository companyRepo;

	@Autowired
	ApplicationRepository applicationRepo;

	@Autowired
	IEmailService emailService;

	@Autowired
	CompanyEnvironUrlRepository companyEnvironUrlRepository;

	@Autowired
	UserRepository userRepo;

	@Value("${selenium.home.default.path}")
	String seleniumHomePath;

	@Value("${email.password.key}")
	String key;

	@GetMapping("/{executionResultsId}")
	public ExecutionResults findById(@PathVariable(value = "executionResultsId") Long executionResultsId) {
		logger.info("get ExecutionResults by id=" + executionResultsId);
		return executionResultsRepo.findById(executionResultsId).orElse(null);
	}

	@GetMapping("/all")
	public Iterable<ExecutionResults> findAll() {
		logger.info("get all executionResultss");
		return executionResultsRepo.findAll();
	}

	@GetMapping("/getAllByCompany")
	public Iterable<ExecutionResults> findAll(@AuthenticationPrincipal final LoginUser loggedInUser) {
		logger.info("get all executionResultss");
		return executionResultsRepo.findAll(loggedInUser.getCompanyId());
	}

	/**
	 * Fetches all Executed result by Company
	 * @param loggedInUser
	 * @return
	 */
	@GetMapping("/getAllRunnerByCompany")
	public Map<Long, LatestExecutionSummary> getAllRunnerByCompany(
			@AuthenticationPrincipal final LoginUser loggedInUser) {
		logger.info("get all executionResults by company");
		List<ExecutionResultRunnerInfo> findAllRunner = (List<ExecutionResultRunnerInfo>) executionResultsRepo
				.findAllRunner(loggedInUser.getCompanyId());

		Map<Long, LatestExecutionSummary> map = new LinkedHashMap<>();

		if (null != findAllRunner) {
			Util.populateResults(findAllRunner, map, true);
		}

		return map;
	}

	/**
	 * Fetches map with statistics related to Runner status
	 * @param executionResultIds
	 * @param loggedInUser
	 * @return Map < runnerId --> {Count if failed,passed & queued} >
	 */
	@PostMapping("/getInPogressRunnerStatusByCompany")
	public Map<Long, LatestExecutionSummary> getInPogressRunnerStatusByCompany(
			@RequestBody ExecutionResultIds executionResultIds, @AuthenticationPrincipal final LoginUser loggedInUser) {
		logger.info("get all executionResults by company");
		List<ExecutionResultRunnerInfo> findAllRunner = (List<ExecutionResultRunnerInfo>) executionResultsRepo
				.getInPogressRunnerStatusByCompany(executionResultIds.getExecutionResultIds(),
						loggedInUser.getCompanyId());
		Map<Long, LatestExecutionSummary> map = new HashMap<>();

		if (null != findAllRunner) {
			Util.populateResults(findAllRunner, map, false);
		}
		return map;
	}

	/**
	 * 
	 * @param runnerId
	 * @return
	 */
	@GetMapping("/getRunnerDetailsFull/{runnerId}")
	public Iterable<ExecutionResults> getRunnerDetailsFull(@PathVariable(value = "runnerId") Long runnerId) {
		Iterable<ExecutionResults> findAllDetailsByRunnerId = executionResultsRepo.findAllDetailsByRunnerId(runnerId);
		findAllDetailsByRunnerId.forEach(er -> {
			er.getTestcaseExeDetailList().forEach(ted -> ted.setExecutionResults(null));
		});
		return findAllDetailsByRunnerId;
	}

	@PostMapping("/save")
	public ExecutionResults save(@RequestBody ExecutionResults executionResults) {
		logger.info("save executionResults = " + executionResults);
		return executionResultsRepo.save(executionResults);
	}

	/**
	 * Save Execution results
	 * Create the file required for execution 
	 * Starts the Execution result 
	 * @param executionResultsUI
	 * @param loggedInUser
	 * @return
	 * @throws IOException
	 * @throws ParserConfigurationException
	 * @throws TransformerException
	 */
	@PostMapping("/saveAll")
	public Iterable<ExecutionResults> saveAll(@RequestBody ExecutionResultsUI executionResultsUI,
			@AuthenticationPrincipal final LoginUser loggedInUser)
			throws IOException, ParserConfigurationException, TransformerException {
		logger.info("save executionResults = " + executionResultsUI);
		String executionName = executionResultsUI.getExecutionName();
		boolean nameExists = executionResultsRepo.nameExists(executionName,loggedInUser.getCompanyId());
		if(nameExists) {
			throw new ValidationException("Execution result name already exists.");
		}
		List<ExecutionResultsUI> executionResultsUIs = new ArrayList<>();
		executionResultsUIs.add(executionResultsUI);
		return executionResultsService.executeAll(executionResultsUIs, loggedInUser, null,
				executionResultsUI.getExecutionName(), false);
	}

	/**
	 * Get the runner completion in percentage
	 * @param runnerId
	 * @return
	 */
	@GetMapping("/progress/{runnerId}")
	public Double progress(@PathParam("runnerId") Long runnerId) {
		Double progress = executionResultsRepo.executionResultProgress(runnerId);
		progress = null == progress ? 0 : progress * 100;
		return progress;
	}

	/**
	 * @return Get sample Execution result UI JSON object
	 */
	@GetMapping("/sample")
	public ExecutionResultsUI test() {
		List<Long> testcaseId = new ArrayList<>();
		testcaseId.add(1l);
		testcaseId.add(2l);
		List<Application> applicationName = new ArrayList<>();
		Application e = new Application();
		e.setApplicationName("applicationName");
		e.setApplicationId(1l);
		applicationName.add(e);
		e = new Application();
		e.setApplicationName("applicationName1");
		e.setApplicationId(2l);
		applicationName.add(e);
		List<String> testCasesNames = Arrays.asList(
				new String[] { "ITSM_ValidateMobilePhoneNotifications", "ITSM_ValidateMobilePhoneNotifications" });
		return new ExecutionResultsUI("executionName", 1l, 1l, testcaseId, "chrome", "environmentName", applicationName,
				testCasesNames, 1l);
	}

	@DeleteMapping("/{executionResultsId}")
	public Boolean delete(@PathVariable(value = "executionResultsId") Long executionResultsId) {
		executionResultsRepo.deleteById(executionResultsId);
		return true;
	}

	/**
	 * Deletes Execution result for same runner Id
	 * @param runnerId
	 * @return
	 */
	@DeleteMapping("/deleteByRunnedId/{runnerId}")
	public Boolean deleteByRunnedId(@PathVariable(value = "runnerId") Long runnerId) {
		executionResultsRepo.deleteByRunnerId(runnerId);
		return true;
	}

	/**
	 * Fetches PDF file report for Execution result
	 * @param runnerId
	 * @return
	 */
	@GetMapping(value = "/getRunnerDetailsPdf/{runnerId}", produces = MediaType.APPLICATION_PDF_VALUE)
	public ResponseEntity<InputStreamResource> downloadPdf(@PathVariable(value = "runnerId") Long runnerId) {
		Iterable<ExecutionResults> testcaseResults = executionResultsRepo.findAllByRunnerId(runnerId);
		ByteArrayInputStream citiesReport = GeneratePdfReport.getExecutionResults(testcaseResults);
		HttpHeaders headers = new HttpHeaders();
//		headers.add("Content-Disposition", "inline; filename=citiesreport.pdf");
		headers.add("Content-Disposition", "attachment; report=try.pdf");
		headers.add("Set-Cookie","fileDownload=true; path=/");
		return ResponseEntity.ok().headers(headers).contentType(MediaType.APPLICATION_PDF)
				.body(new InputStreamResource(citiesReport));
	}

	/**
	 * Fetches Excel file report for Execution result
	 * @param runnerId
	 * @return
	 */
	@GetMapping(value = "/getRunnerDetailsExcel/{runnerId}", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
	public ResponseEntity<InputStreamResource> downloadExcel(@PathVariable(value = "runnerId") Long runnerId) throws MessagingException {
		logger.info("start downloadExcel runnerId=="+runnerId);
		Iterable<ExecutionResults> testcaseResults = executionResultsRepo.findAllByRunnerId(runnerId);
		logger.info("testcaseResults=="+testcaseResults);
		ByteArrayInputStream citiesReport = GenerateExcelReport.getExecutionResultReport(testcaseResults);
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Disposition", "attachment; filename=executionResults.xlsx");
		return ResponseEntity.ok().headers(headers).contentType(MediaType.APPLICATION_OCTET_STREAM)
				.body(new InputStreamResource(citiesReport));
	}
	
	/**
	 * Fetches compressed zipped file for the environment folder for master or User
	 * @param runnerId
	 * @return
	 */
	@GetMapping(value = "/downloadTestcasesZip/{applicationName}/{environmentName}/{isMaster}", produces = "application/zip")
	public ResponseEntity<ByteArrayResource> downloadTestcasesZip(
			@PathVariable(value = "applicationName") String applicationName,
			@PathVariable(value = "environmentName") String environmentName,
			@PathVariable(value = "isMaster") Boolean isMaster,
			@AuthenticationPrincipal final LoginUser loggedInUser) throws IOException {
		String companyName = companyRepo.findById(loggedInUser.getCompanyId()).get().getCompanyName();
		String username = null==isMaster || isMaster ? Util.MASTER_FOLDER_NAME  : loggedInUser.getUsername();
		Path path = Paths.get(projectBasePath, Util.COMPANIES_BASE_FOLDER_NAME, companyName,
				applicationName, Util.TEST_DATA_FOLDER_NAME, username,environmentName);
		logger.error("Looking to zip at : "+path);
		if (Files.exists(path)) {
			byte[] zipByteArray = executionResultsService.getZipIS(path.toFile());
			ByteArrayResource zipFileStream = new ByteArrayResource(zipByteArray);

			return ResponseEntity.ok()
					.header(HttpHeaders.CONTENT_DISPOSITION,
							"attachment;filename=" + username + ".zip")
//					.contentType(MediaType.APPLICATION_OCTET_STREAM)
					.contentLength(zipByteArray.length)
					.body(zipFileStream);
		}
		logger.error("Does not exists : "+path);
		throw new ValidationException("No test library exists for "+username);
	}

	/**
	 * Looks for the failed screen shot PNG file and returns its or else the error message.  
	 * @param executionResultsName
	 * @param testcaseName
	 * @param loggedInUser
	 * @return
	 * @throws FileNotFoundException
	 */
	@GetMapping(value = "/downloadTestCaseSnapShot/{executionResultsName}/{testcaseName}", produces = MediaType.IMAGE_PNG_VALUE)
	public ResponseEntity<InputStreamResource> downloadFailedTestCaseScreenShot(
			@PathVariable(value = "executionResultsName") String executionResultsName,
			@PathVariable(value = "testcaseName") String testcaseName, @AuthenticationPrincipal final LoginUser loggedInUser)
			throws FileNotFoundException {
		logger.info("get all executionResults by company");
		
		String seleniumHome = null;
		Company company = companyRepo.findById(loggedInUser.getCompanyId()).get();
		if (org.apache.commons.lang3.StringUtils.isBlank(company.getSeleniumHome())) {
			seleniumHome = seleniumHomePath;
		} else {
			seleniumHome = company.getSeleniumHome();
		}
		String screenshotFileName = testcaseName  + Util.FAILED_TC_SCREENSHOT_EXT;
		Path path = Paths.get(seleniumHome, company.getCompanyName(), loggedInUser.getUsername(),
				executionResultsName, Util.SCREENSHOT, screenshotFileName);

		if (!Files.exists(path)) {
			logger.error("Image path not exists path="+path);
			throw new ValidationException("No failed screenshot file available for test case : "+testcaseName);
		}
		logger.info("Downloading image file from path="+path);
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Disposition", "inline; filename=" + screenshotFileName);

		return ResponseEntity.ok().headers(headers).contentType(MediaType.IMAGE_PNG)
				.body(new InputStreamResource(new FileInputStream(path.toString())));
	}
	
	/**
	 * Looks for the test case log file and returns its or else the error message.
	 * @param executionResultsName
	 * @param testcaseName
	 * @param loggedInUser
	 * @return
	 * @throws FileNotFoundException
	 */
	@GetMapping(value = "/downloadTestCaseLog/{executionResultsName}/{testcaseName}", produces = MediaType.TEXT_PLAIN_VALUE)
	public ResponseEntity<InputStreamResource> downloadTestCaseLog(
			@PathVariable(value = "executionResultsName") String executionResultsName,
			@PathVariable(value = "testcaseName") String testcaseName, @AuthenticationPrincipal final LoginUser loggedInUser)
			throws FileNotFoundException {
		logger.info("Download test case log start");
		
		String seleniumHome = null;
		Company company = companyRepo.findById(loggedInUser.getCompanyId()).get();
		if (org.apache.commons.lang3.StringUtils.isBlank(company.getSeleniumHome())) {
			seleniumHome = seleniumHomePath;
		} else {
			seleniumHome = company.getSeleniumHome();
		}
		String screenshotFileName = testcaseName  + Util.TC_LOG_FILE_EXT;
		Path path = Paths.get(seleniumHome, company.getCompanyName(), loggedInUser.getUsername(),
				executionResultsName, Util.LOG, screenshotFileName);

		if (!Files.exists(path)) {
			logger.error("Log path not exists path="+path);
			throw new ValidationException("No log file available for test case : "+testcaseName);
		}
		logger.info("Downloading log file from path="+path);
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Disposition", "inline; filename=" + screenshotFileName);

		return ResponseEntity.ok().headers(headers).contentType(MediaType.TEXT_PLAIN)
				.body(new InputStreamResource(new FileInputStream(path.toString())));
	}
	
	@GetMapping("/allExpandedER/{runnerId}")
	public LatestExecutionExtraSummary getExpandedER(@PathParam("runnerId") Long runnerId) {
		logger.info("get all executionResultss");
		return executionResultsRepo.getExpandedER(runnerId);
	}
	/**
	 * Fetches list of all pending execution results which are yet to emailed.
	 * @return
	 */
	@GetMapping("/getPendingEmailRecords")
	public Iterable<ExecutionResultRunnerInfo> getPendingEmailRecords() {
		logger.info("get all getPendingEmailRecords");
		Iterable<ExecutionResultRunnerInfo> findAllNonEmailedRunnerStatus = executionResultsRepo.
				findAllNonEmailedRunnerStatus();
		return findAllNonEmailedRunnerStatus;
	}
	
	/**
	 * Get map{ runnerId -->  combined list of all Performance Execution results}
	 * @param loggedInUser
	 * @return
	 */
	@GetMapping("/getPerfResults")
	public Map<Long, PerfUiBean> getPerfResults(@AuthenticationPrincipal final LoginUser loggedInUser) {
		logger.info("get all getPendingEmailRecords ");
		Iterable<ExecutionResults> perfRecords = executionResultsRepo.
				getPerfRecords(loggedInUser.getCompanyId());
		Map<Long, PerfUiBean> perfMap = ((List<ExecutionResults>)perfRecords).stream().map( e-> {
			Testcases tc = e.getTestcases();
			Collection<TestcaseRunInfo> testcases = new ArrayList<>();
			Date executionStartDate = null;
			Date executionEndDate = null;
			List<TestcaseExeDetail> testcaseExeDetailList = e.getTestcaseExeDetailList();
			if(null!=testcaseExeDetailList && !testcaseExeDetailList.isEmpty()) {
				executionStartDate = testcaseExeDetailList.get(0).getExecutionStartDate();
				executionEndDate = testcaseExeDetailList.get(0).getExecutionEndDate();
			}
			TestcaseRunInfo testcaseRunInfo = new TestcaseRunInfo(tc,executionStartDate,executionEndDate);
			testcases.add(testcaseRunInfo);
			return new PerfUiBean(e.getExecutionName(), e.getRunnerId(), testcases, e.getExecutionStartDate(), e.getExecutionEndDate());
		}).collect(Collectors.toMap(PerfUiBean::getRunnerId, Function.identity(),(p1,p2) -> {
			p1.getTestcaseRunInfoList().addAll(p2.getTestcaseRunInfoList());
			return p1;
		}));
		
		return perfMap;
	}
	
	/**
	 * Executed the failed test case of a runnerId
	 * @param runnerId
	 * @param loggedInUser
	 * @return
	 */
	@PostMapping("/reRun/{runnerId}")
	public Boolean reRun(@PathVariable("runnerId") Long runnerId,@AuthenticationPrincipal final LoginUser loggedInUser){
		boolean reRunFailedTests = executionResultsService.reRunFailedTests(runnerId,loggedInUser);
		return reRunFailedTests;
	}

}
