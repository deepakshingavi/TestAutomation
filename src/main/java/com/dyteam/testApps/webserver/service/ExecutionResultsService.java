package com.dyteam.testApps.webserver.service;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.validation.ValidationException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.dom.DOMSource;

import org.apache.poi.util.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.dyteam.testApps.webserver.Util;
import com.dyteam.testApps.webserver.entity.Application;
import com.dyteam.testApps.webserver.entity.Company;
import com.dyteam.testApps.webserver.entity.ExecutionResults;
import com.dyteam.testApps.webserver.entity.ScheduledExecution;
import com.dyteam.testApps.webserver.entity.ScheduledExecutionBk;
import com.dyteam.testApps.webserver.entity.Testcases;
import com.dyteam.testApps.webserver.entity.User;
import com.dyteam.testApps.webserver.model.EXECUTION_RESULT;
import com.dyteam.testApps.webserver.model.ExecutionResultsUI;
import com.dyteam.testApps.webserver.repository.BrowserRepository;
import com.dyteam.testApps.webserver.repository.CompanyRepository;
import com.dyteam.testApps.webserver.repository.EnvironmentRepository;
import com.dyteam.testApps.webserver.repository.ExecutionResultsRepository;
import com.dyteam.testApps.webserver.repository.TestcasesRepository;
import com.dyteam.testApps.webserver.repository.UserRepository;
import com.dyteam.testApps.webserver.security.LoginUser;

@Service
public class ExecutionResultsService implements IExecutionResultsService{

	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
    TestcasesRepository testcasesRepo;
	
	@Autowired
    CompanyRepository companyRepo;
	
	@Autowired
    BrowserRepository browserRepo;
	
	@Autowired
    EnvironmentRepository environmentRepo;
	
	@Value("${email.password.key}") 
    String key;
	
	@Value("${selenium.home.default.path}") 
    String seleniumHomePath;
	
	@Value("${test.execution.mode.asynch}") 
    Boolean asynchMode;
	
	@Value("${execution.xml.methods}") 
    String methods;
	
	@Value("${execution.xml.thread.count}") 
    String threadCount;
	
	@Value("${execution.xml.config.failure.policy}") 
    String configFailurePolicy;
	
	@Value("${execution.xml.default.class}")
	String defaultClass;
	
	@Autowired
    IEmailService emailService;
	
	@Autowired
    UserRepository userRepo;
	
	@Autowired
    IExecutionResultsService executionResultsService;
	
	@Autowired
    ExecutionResultsRepository executionResultsRepo;
	
	@Value("#{systemProperties['user.dir']}")
	String userDir;
	
	/**
	 * Execute the list of Test cases
	 */
	@Override
	public Iterable<ExecutionResults> executeAll(List<ExecutionResultsUI> executionResultsUIs, LoginUser loggedInUser, ScheduledExecutionBk scheduledExecutionBk,
			String executionName, boolean isLogical) {
		Long runnerId = Util.getEpochTimeInMillis();
    	List<ExecutionResults> saveAll= new ArrayList<>();
    	executionResultsUIs.forEach( executionResultsUI -> {
    		try {
    			List<ExecutionResults> executionResults = new ArrayList<>();
    			
    			if(null==executionResultsUI.getTestcaseIds() || executionResultsUI.getTestcaseIds().isEmpty()) {
    				throw new ValidationException("Please select at least once test case");
    			}
    			
    			executionResultsUI.getTestcaseIds().forEach(tId -> {
    				ExecutionResults er = new ExecutionResults();
    				er.setExecutionName(executionResultsUI.getExecutionName());
    				er.setBrowserId(executionResultsUI.getBrowserId());
    				er.setEnvironmentId(executionResultsUI.getEnvironmentId());
    				er.setCompanyId(loggedInUser.getCompanyId());
    				er.setResult(EXECUTION_RESULT.QUEUED.toString());
    				er.setExecutedBy(new User(loggedInUser.getUserId()));
    				er.setTestcases(new Testcases(tId));
    				er.setRunnerId(runnerId);
    				er.setExecutionUserId(executionResultsUI.getExecutionUserId());
    				er.setScheduled(null==executionResultsUI.getScheduled() ? false: executionResultsUI.getScheduled());
    				er.setStatus(true);
    				er.setEmailSent(false);
    				executionResults.add(er);
    			});
    			
    			List<Testcases> testcases = testcasesRepo.getIdNameClassForAll(executionResultsUI.getTestcaseIds());
    			executionResultsUI.setTestcases(testcases);
    			saveAll.addAll((Collection<? extends ExecutionResults>) executionResultsRepo.saveAll(executionResults));
    			logger.info("Execution data saved");
    		} catch (Exception e) {
    			logger.error("Error occured while saving the executon test case records.",e);
    			throw e;
    		}
    	});
    	User user = userRepo.findById(loggedInUser.getUserId()).get();
    	String email = user.getEmail();
    	Company company = createSeleniumFolderAndExe(executionResultsUIs, loggedInUser, runnerId,email,executionName,isLogical, false);

    	shootEmail(executionResultsUIs, runnerId, company,user,executionName,isLogical);
    	
    	if(null!=scheduledExecutionBk) {
    		scheduledExecutionBk.setRunnerId(runnerId);
    	}
    	return saveAll;
	}
	
	/**
	 * Creates Selenium folder and files and runs the selenium bat file.
	 * @param executionResultsUIList
	 * @param loggedInUser
	 * @param runnerId
	 * @param email
	 * @param executionName
	 * @param isLogical
	 * @param isReRun
	 * @return
	 */
	private Company createSeleniumFolderAndExe(List<ExecutionResultsUI> executionResultsUIList, final LoginUser loggedInUser,
			Long runnerId,  String email,String executionName,boolean isLogical, boolean isReRun) {
		Company company = null;
		try {
			String seleniumHome = null;
			company = companyRepo.findById(loggedInUser.getCompanyId()).get();
			if(org.apache.commons.lang3.StringUtils.isBlank(company.getSeleniumHome())) {
				seleniumHome = seleniumHomePath;
			} else {
				seleniumHome = company.getSeleniumHome();
			}

			Company companyInner = company;
			String seleniumHomeInner = seleniumHome;

			Util.createFolders(Paths.get(seleniumHome, company.getCompanyName(),loggedInUser.getUsername(),executionName,Util.LOG));
			Util.createFolders(Paths.get(seleniumHome, company.getCompanyName(),loggedInUser.getUsername(),executionName,Util.SCREENSHOT));

			logger.info("Log & screenshot folder created");

			Path path = Paths.get(seleniumHome, company.getCompanyName(),loggedInUser.getUsername(),executionName);
			Path relativePath = Paths.get( company.getCompanyName(),loggedInUser.getUsername(),executionName);
			try {
				DOMSource executionXml = getExecutionXml(executionName,relativePath.toString(),executionResultsUIList);
				String executionConfig = getExecutionConfig(executionResultsUIList,companyInner.getCompanyName(),runnerId,loggedInUser.getUsername(),email,isLogical,executionName);
				String executionBat = getExecutionBat(seleniumHomeInner, Util.getExecutorXmlFilePath(path));
				String batchFilePath = Util.createExecutionFiles(executionXml,executionConfig,executionBat,path, isReRun);
				
				logger.info("Automation file created path="+batchFilePath);
				Util.executebatchFile(batchFilePath,asynchMode,userDir);
				logger.info("Initiated the automation for executionResultsUI="+executionResultsUIList);
			} catch (TransformerConfigurationException  e) {
				logger.error("Error : Generating config XML string for executionResultsUI="+executionResultsUIList,e);
			} catch (TransformerException e) {
				logger.error("Error : Generating config XML string or creating the execution files for executionResultsUI="+executionResultsUIList,e);
			} catch (IOException e) {
				logger.error("Error : Executing he batch file.for executionResultsUI="+executionResultsUIList,e);
			}

			/*
			 * seleniumhome\companies\<company-name>\<user-name>\<execution-name>\log\
			 * seleniumhome\companies\<company-name>\<user-name>\<execution-name>\screenshot\
			 * Trigger batch file. <on trigger execute batch.
			 * EMAIL should send : from:<company-configuration-email-id>, to: <triggered-execution-user>
			 */
		} catch (Exception e) {
			logger.error("Error occured while creating and executing the automtion files.",e);
		}
		return company;
	}

	/**
	 * Triggers email with list of Triggered Test cases executions
	 * @param executionResultsUIs
	 * @param runnerId
	 * @param company
	 * @param user
	 * @param executionName
	 * @param isLogical
	 */
	private void shootEmail(List<ExecutionResultsUI> executionResultsUIs, Long runnerId,
			Company company, User user,String executionName, boolean isLogical) {
		try {
			StringBuffer strbfSubject = new StringBuffer();
			
			strbfSubject.append("Execution : ").append(executionName);
			if(null!=executionResultsUIs && executionResultsUIs.size()==1) {
				strbfSubject.append(" Triggered on ").append(executionResultsUIs.get(0).getEnvironmentName());
			}
    		StringBuffer strbf = new StringBuffer();
    		StringBuffer testCasesNameStrBuff = new StringBuffer();
    		executionResultsUIs.forEach(executionResultsUI-> executionResultsUI.getTestcases().forEach(tc -> {
    			testCasesNameStrBuff.append("- ").append(tc.getTestcaseName()).append(Util.HTML_NEXT_LINE_STR);
    		}));

    		String browserList = executionResultsUIs.stream().map( er -> er.getBrowserName()).distinct().collect(Collectors.joining(","));
			strbf.append("Dear ").append(user.getfName()).append(" ").append(user.getlName()).append(",").append(Util.HTML_NEXT_LINE_STR).
    		append(Util.HTML_NEXT_LINE_STR).
    		append("Execution Name :").append(executionName).append(Util.HTML_NEXT_LINE_STR).
    		append("Browser Name : ").append(browserList).append(Util.HTML_NEXT_LINE_STR).
    		append("Selected Test Cases : ").append(Util.HTML_NEXT_LINE_STR).
    		append(testCasesNameStrBuff).
    		append("No. of Selected Test Cases : ").append(executionResultsUIs.stream().map(ExecutionResultsUI::getTestcaseIds).filter(tc -> tc!=null).mapToInt(List::size).sum()).append(Util.HTML_NEXT_LINE_STR).
    		append("Triggered By : ").append(user.getfName()).append(" ").append(user.getlName()).append(Util.HTML_NEXT_LINE_STR).
    		append("Triggered At : ").append(new SimpleDateFormat(Util.DD_MM_YYYY_HH_MM_SS).format(new Date(runnerId))).append(Util.HTML_NEXT_LINE_STR).
    		append(Util.HTML_NEXT_LINE_STR).
    		append("Thanks,").append(Util.HTML_NEXT_LINE_STR).
    		append("Team GetAutomationDone");
    		emailService.sendSimpleMessage(user.getEmail(), strbfSubject.toString(), strbf.toString(), 
    				company);
    		logger.info("Email send successfully");
    	} catch (Exception e) {
    		logger.error("Error occured emailing.",e);
    	}
	}
	
	/**
     * set projectLocation=C:\SeleniumAutomation_V1
		cd %projectLocation%
		C:
		java -cp %projectLocation%\source\3rdParty\*;%projectLocation%\class\ org.testng.TestNG Companies\AHS\AHS\SmartIT_Dev_1\TestNGExecutor.xml
		
		Executed the bat file
     * @param seleniumHome 
     * @param xmlFilePath 
     * @return
     */
    private String getExecutionBat(String seleniumHome, String xmlFilePath) {
    	return new StringBuffer().append("set projectLocation=").append(seleniumHome).append(Util.NEXT_LINE_STR).
    	append("cd /d %projectLocation%").append(Util.NEXT_LINE_STR).
    	append("java -cp %projectLocation%\\source\\3rdParty\\*;%projectLocation%\\class\\ org.testng.TestNG ").append(xmlFilePath).toString();
	}

    /**
     * ExecutionName:SmartIT_Dev_1
		IsLogicalGroup:false
		EnvironmentName:Development
		BrowserName:Chrome
		FailureAttempt:1
		RemedyITSM=https://ahs-dev.onbmc.com 
		SmartIT=https://ahs-dev-smartit.onbmc.com 
		MyIT=https://ahs-dev-myit.onbmc.com 
		TRIGGEREDBY:AHS
		TRIGGEREDWHEN:04-08-2018 02:40:52
		COMPANY:AHS
		EMAIL:rahul41085@gmail.com
		runner_id:817
		Creates the Execution Config file
     * @param executionResultsUIList 
     * @param runnerId 
     * @param email 
     * @param executionName 
     * @param b 
     * @param appUrlInfo 
     * @param string 
     * @param string 
     * @return
     */
	private String getExecutionConfig(List<ExecutionResultsUI> executionResultsUIList, String companyName, Long runnerId, String triggeredBy, String email, Boolean isLogicalGroup, 
			String executionName) {
		StringBuffer logicalGroupBf = new StringBuffer();
		if(null!=isLogicalGroup && isLogicalGroup) {
			executionResultsUIList.forEach(executionResultsUI -> executionResultsUI.getTestcases().forEach(tcId -> {
				logicalGroupBf.append(tcId.getTestcaseName()).append(":").append(executionResultsUI.getEnvironmentId()).append(":").append(executionResultsUI.getExecutionUserId()).append(Util.NEXT_LINE_STR);
			}));
		} else {
			isLogicalGroup=false;
		}
		StringBuffer stringBuffer = new StringBuffer();
		ExecutionResultsUI executionResultsUI = isLogicalGroup?new ExecutionResultsUI():executionResultsUIList.get(0);
		
		stringBuffer.append("ExecutionName:").append(executionName).append(Util.NEXT_LINE_STR);
		stringBuffer.append("IsLogicalGroup:").append(isLogicalGroup.toString()).append(Util.NEXT_LINE_STR);
		if(!isLogicalGroup) {
			stringBuffer.append("EnvironmentId:").append(executionResultsUI.getEnvironmentId()).append(Util.NEXT_LINE_STR).
			append("BrowserId:").append(executionResultsUI.getBrowserId()).append(Util.NEXT_LINE_STR).
			append("ExecuteUser:").append(executionResultsUI.getExecutionUserId()).append(Util.NEXT_LINE_STR);
		}
		stringBuffer.append(isLogicalGroup?logicalGroupBf:"").append(Util.NEXT_LINE_STR).
		append("TRIGGEREDBY:").append(triggeredBy).append(Util.NEXT_LINE_STR).
		append("TRIGGEREDWHEN:").append(new SimpleDateFormat(Util.DD_MM_YYYY_HH_MM_SS).format(new Date(runnerId))).append(Util.NEXT_LINE_STR).
		append("COMPANY:").append(companyName).append(Util.NEXT_LINE_STR).
		append("EMAIL:").append(email).append(Util.NEXT_LINE_STR).
		append("runner_id:").append(runnerId).append(Util.NEXT_LINE_STR);
		return stringBuffer.toString();
	}

	/**
	 * Create XML dom object for selenium test cases execution
	 * @param executionName
	 * @param fileLocation
	 * @param executionResultsUIs
	 * @return
	 * @throws ParserConfigurationException
	 * @throws TransformerConfigurationException
	 */
	private DOMSource getExecutionXml(String executionName,String fileLocation, List<ExecutionResultsUI> executionResultsUIs) throws ParserConfigurationException, TransformerConfigurationException {

		DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
	    DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

	    // root elements
	    Document doc = docBuilder.newDocument();
	    //<suite name="Suite" parallel="methods" thread-count="3">
	    Element rootElement = doc.createElement("suite");
	    rootElement.setAttribute("name", executionName);
	    rootElement.setAttribute("configfailurepolicy", configFailurePolicy);
	    rootElement.setAttribute("parallel", methods);
	    rootElement.setAttribute("thread-count", threadCount);
	    doc.appendChild(rootElement);
//	    rootElement.
	    Element listenersElement = doc.createElement("listeners");
	    Element listener = doc.createElement("listener") ;
	    listener.setAttribute("class-name", "com.RetryFailed.RetryListenerClass");
		listenersElement.appendChild(listener);
		rootElement.appendChild(listenersElement);
		
		
		Element parameter = doc.createElement("parameter");
		parameter.setAttribute("name", "CallerFilePath");
		parameter.setAttribute("value", fileLocation);
		rootElement.appendChild(parameter);
	    
		
		Element test = doc.createElement("test");
		test.setAttribute("name", executionName);
		rootElement.appendChild(test);
		
		Element classesElement = doc.createElement("classes");
		test.appendChild(classesElement);
		
		if(null!=defaultClass && !defaultClass.trim().isEmpty() && !"none".equalsIgnoreCase(defaultClass)) {
			Element defaultClassElement = doc.createElement("class");
			defaultClassElement.setAttribute("name", defaultClass);
			classesElement.appendChild(defaultClassElement);
		}
		
		
		executionResultsUIs.forEach(executionResultsUI->executionResultsUI.getTestcases().forEach(tc ->{
			Element classElement = doc.createElement("class");
			classElement.setAttribute("name", tc.getClassName());
			classesElement.appendChild(classElement);
		}));
	    
	    DOMSource source = new DOMSource(doc);
		return source;
	}

	@Override
	public void save(ScheduledExecution scheduledExecution) {
		
	}

	/**
	 * Zips the directory and get the byte array of the same.
	 */
	@Override
	public byte[] getZipIS(File dirToZip) throws IOException {
		if (dirToZip.isHidden() || !dirToZip.isDirectory()) {
            return null;
        }
		String sourceFolderPathName = dirToZip.getAbsolutePath();
		List<String> fileList = new ArrayList<>();
		generateFileList(dirToZip, sourceFolderPathName, fileList);
		
		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(byteArrayOutputStream);
        ZipOutputStream zipOutputStream = new ZipOutputStream(bufferedOutputStream);
		
        fileList.forEach( f -> {
        	ZipEntry zipEntry = new ZipEntry(f);
        	try {
				zipOutputStream.putNextEntry(zipEntry);
				FileInputStream fileInputStream = new FileInputStream(Paths.get(dirToZip.getAbsolutePath(),f).toFile());
	            IOUtils.copy(fileInputStream, zipOutputStream);
	            fileInputStream.close();
	            zipOutputStream.closeEntry();
			} catch (IOException e) {
				logger.error("Error occured while zipping file="+f,e);
			}
        });
        if (zipOutputStream != null) {
            zipOutputStream.finish();
            zipOutputStream.flush();
            IOUtils.closeQuietly(zipOutputStream);
        }
        IOUtils.closeQuietly(bufferedOutputStream);
        IOUtils.closeQuietly(byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
	}
	
	/**
	 * Recursive list of function for fetching list of files in dir and its sub dir.
	 * @param node
	 * @param sourceFolder
	 * @param fileList
	 */
	public void generateFileList(File node,String sourceFolder,List<String> fileList) {
        // add file only
        if (node.isFile()) {
            fileList.add(generateZipEntry(node.toString(),sourceFolder));
        }

        if (node.isDirectory()) {
            String[] subNote = node.list();
            for (String filename: subNote) {
                generateFileList(new File(node, filename),sourceFolder,fileList);
            }
        }
    }

    private String generateZipEntry(String file,String sourceFolder) {
        return file.substring(sourceFolder.length() + 1, file.length());
    }

	@Override
	@Transactional
	/**
	 * Rerty executing the failed test cases
	 */
	public boolean reRunFailedTests(Long runnerId,LoginUser loggedInUser) {
		logger.info("runnerId=="+runnerId+" EXECUTION_RESULT.FAILED.toString()=="+EXECUTION_RESULT.FAILED.toString());
		List<ExecutionResults> exeResults = (List<ExecutionResults>) executionResultsRepo.findAllFailedByRunnerId(runnerId,EXECUTION_RESULT.FAILED.toString());
		logger.info("exeResults=="+exeResults);
		if(null!=exeResults && !exeResults.isEmpty()) {
			logger.info("exeResults=="+exeResults);
			List<Long> executionResultIds = exeResults.stream().map(ExecutionResults::getExecutionResultsId).collect(Collectors.toList());
			logger.info("updating to QUEUED executionResultIds=="+executionResultIds);
			executionResultsRepo.updateResetResult(executionResultIds,EXECUTION_RESULT.QUEUED.toString());
			List<ExecutionResultsUI> executionResultsUIList = exeResults.stream().collect(Collectors.groupingBy(er -> (er.getBrowserId()+"_"+er.getEnvironmentId()))).
					values().stream().filter(v->null!=v && !v.isEmpty()).map( exeResultList -> {
						ExecutionResults executionResults = exeResultList.get(0);
						Long browserId = executionResults.getBrowserId();
						Long environmentId = executionResults.getEnvironmentId();
						executionResults.getTestcases().getApplicationId();
						Stream<Testcases> map = exeResultList.stream().map(er -> er.getTestcases());
						List<Long> testcaseIds = exeResultList.stream().map(er -> er.getTestcases().getTestcasesId()).collect(Collectors.toList());
						List<String> testcaseNames = exeResultList.stream().map(er -> er.getTestcases().getTestcaseName()).collect(Collectors.toList());
						List<Testcases> testcases = map.collect(Collectors.toList());
						List<Application> applications = exeResultList.stream().map(er -> new Application(er.getTestcases().getApplicationId(),null)).collect(Collectors.toList());
						
						String browserName = browserRepo.findById(browserId).get().getBrowserName();
						String envName = environmentRepo.findById(environmentId).get().getEnvironmentName();
						ExecutionResultsUI executionResultsUI = new ExecutionResultsUI(executionResults.getExecutionName(), executionResults.getEnvironmentId(), 
								executionResults.getBrowserId(), testcaseIds, browserName, 
								envName, applications, testcaseNames, loggedInUser.getUserId(), false);
						executionResultsUI.setTestcases(testcases);
						return executionResultsUI;
					}).collect(Collectors.toList());
			logger.info("executionResultsUIList=="+executionResultsUIList);
			if(null!=executionResultsUIList && !executionResultsUIList.isEmpty()) {
				boolean isLogical = executionResultsUIList.size()>1;
				ExecutionResultsUI executionResultsUI = executionResultsUIList.get(0);
				User user = userRepo.findById(loggedInUser.getUserId()).get();
				Company company = companyRepo.findById(user.getCompanyId()).get();
				createSeleniumFolderAndExe(executionResultsUIList, loggedInUser, runnerId, user.getEmail(), executionResultsUI.getExecutionName(), isLogical, true);
				shootEmail(executionResultsUIList, runnerId, company,user,executionResultsUI.getExecutionName(),isLogical);
				return true;
			}
		}
		return false;
	}
	
	
	
	
}
