package com.dyteam.testApps.webserver.controller;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Path;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.dyteam.testApps.webserver.Util;
import com.dyteam.testApps.webserver.repository.CompanyRepository;
import com.dyteam.testApps.webserver.security.LoginUser;
import com.dyteam.testApps.webserver.service.IStorageService;

/**
 * This controller takes care of handling all operations related to Bulk upload of Test cases
 * @author deepak
 */
@RestController
@RequestMapping("/uploadTestData")
public class UploadTestDataController {
	
	
	private static final String XLSX = ".xlsx";

	public  String fileStorageBasePath;
	
	private final IStorageService storageService;
	private CompanyRepository companyRepo;
	
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	public UploadTestDataController(@Value( "${testcase.data.file.basePath}" )String fileStorageBasePath,IStorageService storageService,CompanyRepository companyRepo) {
		this.storageService=storageService;
		this.companyRepo=companyRepo;
		this.fileStorageBasePath=fileStorageBasePath;
	}
	
	@PostMapping("/")
    public boolean handleFileUpload(@RequestParam("testData") MultipartFile file,
    		@RequestParam("applicationName")String applicationName,
    		@RequestParam("environmentName")String environmentName,
    		@RequestParam("testCaseName")String testCaseName,
    		@AuthenticationPrincipal final LoginUser loggedInUser) throws IOException {
		String username = loggedInUser.getUsername();
		String companyName = companyRepo.getName(loggedInUser.getCompanyId());
		Path masterDirLocation = Util.getCompleteFilePath(fileStorageBasePath,companyName,applicationName,environmentName,Util.MASTER,testCaseName);
        Util.uploadTestCaseFile(file, masterDirLocation);
        logger.info("pat build to ="+masterDirLocation);
        
        Path userDirLocation = Util.getCompleteFilePath(fileStorageBasePath,companyName,applicationName,environmentName,username,testCaseName);
        Util.uploadTestCaseFile(file, userDirLocation);
        logger.info("pat build to ="+userDirLocation);
		
        return true;
    }
	
	@ExceptionHandler(FileNotFoundException.class)
    public ResponseEntity<?> handleStorageFileNotFound(FileNotFoundException exc) {
        return ResponseEntity.notFound().build();
    }
	
}
