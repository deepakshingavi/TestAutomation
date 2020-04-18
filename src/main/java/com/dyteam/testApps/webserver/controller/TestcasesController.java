package com.dyteam.testApps.webserver.controller;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;

import javax.validation.ValidationException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.dyteam.testApps.webserver.Util;
import com.dyteam.testApps.webserver.entity.Application;
import com.dyteam.testApps.webserver.entity.Testcases;
import com.dyteam.testApps.webserver.repository.ApplicationRepository;
import com.dyteam.testApps.webserver.repository.CompanyRepository;
import com.dyteam.testApps.webserver.repository.TestcasesRepository;
import com.dyteam.testApps.webserver.security.LoginUser;

/**
 * This controller takes care of handling all operations related to Test cases
 * @author deepak
 */
@RestController
@RequestMapping("/testcases")
public class TestcasesController {

	private static final String XLSX = ".xlsx";

	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
    @Autowired
    TestcasesRepository testcasesRepo;
    
    @Autowired
    ApplicationRepository appRepo;
    
    @Autowired
    CompanyRepository companyRepo;
    
    @Value( "${testcase.data.file.basePath}" )
    String fileStorageBasePath;
    
    @GetMapping("/{testcasesId}")
    public Testcases findById(@PathVariable(value="testcasesId") Long testcasesId) {
    	logger.info("get Testcases by id="+testcasesId);
        return testcasesRepo.findById(testcasesId).orElse(null);
    }
    
    @GetMapping("/all")
    public Iterable<Testcases> findAll() {
    	logger.info("get all testcasess");
        return testcasesRepo.findAll();
    }
    
    /**
     * Get all Test case for a Company
     * @param loggedInUser
     * @return
     */
    @GetMapping("/allByCompany")
    public Iterable<Testcases> getActiveAll(@AuthenticationPrincipal final LoginUser loggedInUser) {
    	logger.info("get all active testcasess");
        return testcasesRepo.findActiveAll(loggedInUser.getCompanyId());
    }
    
    /**
     * Create or update a Test case
     * @param testcases
     * @param loggedInUser
     * @return
     */
    @PostMapping("/save")
    public Testcases save(@RequestBody Testcases testcases, 
    		@AuthenticationPrincipal final LoginUser loggedInUser)  {
    	logger.info("save testcases = "+testcases);
    	
    	if(null==testcases.getTestcasesId()) {
    		testcases.setUserId(loggedInUser.getUserId());
    		testcases.setStatusType(1);
    		testcases.setStatus(1);
    		testcases.setIsAvailbale(1);
    	} else {
    		Testcases testcasesDB = testcasesRepo.findById(testcases.getTestcasesId()).get();
    		testcasesDB.setTestcaseName(testcases.getTestcaseName());
    		testcasesDB.setClassName(testcases.getClassName());
    		testcasesDB.setDescription(testcases.getDescription());
    		testcasesDB.setIsPerfSuite(testcases.getIsPerfSuite());
    		testcasesDB.setApplicationId(testcases.getApplicationId());
    		testcases = testcasesDB;
    	}
    	Application application = appRepo.findById(testcases.getApplicationId()).get();
    	
    	testcases.setCompanyId(application.getCompanyId());
    	
        Testcases save = testcasesRepo.save(testcases);
		return save;
    }
    
    @DeleteMapping("/{testcasesId}")
    public Boolean delete(@PathVariable(value="testcasesId") Long testcasesId) {
    	testcasesRepo.deleteById(testcasesId);
		return true;
    }
    
    /**
     * Uploads Test case xlsx file and save it respective environment location
     * @param file
     * @param applicationName
     * @param environmentName
     * @param testCaseName
     * @param loggedInUser
     * @return
     * @throws IOException
     */
    @PostMapping("/uploadTestCaseFile")
    public boolean uploadTestCaseFile(@RequestParam("testData") MultipartFile file,
    		@RequestParam("applicationName")String applicationName,
    		@RequestParam("environmentName")String environmentName,
    		@RequestParam("testCaseName")String testCaseName,
    		@AuthenticationPrincipal final LoginUser loggedInUser) throws IOException {
		String username = loggedInUser.getUsername();
		String companyName = companyRepo.getName(loggedInUser.getCompanyId());
		
		logger.info("fileStorageBasePath="+fileStorageBasePath+",companyName="+companyName+
				",applicationName="+applicationName+",environmentName="+environmentName+",Util.MASTER="+Util.MASTER+""
						+ ",testCaseName="+testCaseName);
		
		Path masterDirLocation = Util.getCompleteFilePath(fileStorageBasePath,companyName,applicationName,environmentName,Util.MASTER,testCaseName);
        Util.uploadTestCaseFile(file, masterDirLocation);
        logger.info("pat build to ="+masterDirLocation);
        
        Path userDirLocation = Util.getCompleteFilePath(fileStorageBasePath,companyName,applicationName,environmentName,username,testCaseName);
        Util.uploadTestCaseFile(file, userDirLocation);
        logger.info("pat build to ="+userDirLocation);
		
        return true;
    }
    
    /**
     * Download the Test case execution XLSX file.
     * @param applicationName
     * @param environmentName
     * @param testCaseName
     * @param isMasterFile
     * @param loggedInUser
     * @return
     * @throws IOException
     */
    @GetMapping(value = "/downloadTestCase", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public ResponseEntity<InputStreamResource> downloadTestCase(@RequestParam("applicationName")String applicationName,
    		@RequestParam("environmentName")String environmentName,
    		@RequestParam("testCaseName")String testCaseName,
    		@RequestParam("isMasterFile")Boolean isMasterFile,
    		@AuthenticationPrincipal final LoginUser loggedInUser) throws IOException {
    	String companyName = companyRepo.getName(loggedInUser.getCompanyId());
    	String username = null==isMasterFile || !isMasterFile ? loggedInUser.getUsername() : Util.MASTER;
    	Path userTestCaseFileLocation = Util.getCompleteFilePath(fileStorageBasePath,companyName,applicationName,
    			environmentName,username,testCaseName+XLSX);
    	logger.info("username="+username+",isMasterFile="+isMasterFile+",userTestCaseFileLocation="+userTestCaseFileLocation);
    	InputStream newInputStream=  null;
    	try {
    		newInputStream = Files.newInputStream(userTestCaseFileLocation);
    	} catch (IOException e) {
    		logger.error("Error occured while fetching test case file="+userTestCaseFileLocation,e);
    		throw new ValidationException("Test case for "+testCaseName+" not present.");
    	}
    	HttpHeaders headers = new HttpHeaders();
    	headers.add("Content-Disposition", "attachment; filename=\""+testCaseName+XLSX+"\"");
    	return ResponseEntity
    			.ok()
    			.headers(headers)
    			.contentType(MediaType.APPLICATION_OCTET_STREAM)
    			.body(new InputStreamResource(newInputStream));

    }
    
    
    
    @GetMapping(value = "/getTestCases")
    public Iterable<String> getTestCases() throws IOException {
    	List<Long> ids = Arrays.asList(new Long[] {110l,112l});
    	Iterable<String> testcaseResults = testcasesRepo.getClassesForAll(ids);
    	return testcaseResults;
    }
    
}
