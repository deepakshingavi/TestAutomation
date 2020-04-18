package com.dyteam.testApps.webserver;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.web.multipart.MultipartFile;

import com.dyteam.testApps.webserver.model.EXECUTION_RESULT;
import com.dyteam.testApps.webserver.model.ExecutionResultRunnerInfo;
import com.dyteam.testApps.webserver.model.LatestExecutionExtraSummary;
import com.dyteam.testApps.webserver.model.LatestExecutionSummary;


public class Util {

	
	private static final String EXECUTION_XML_FILE_NAME = "TestNGExecutor.xml";

	private static final String EXECUTION_CONFIG_FILE_NAME = "configuration.txt";

	private static final String EXECUTION_BAT_FILE_NAME = "TriggerExecution.bat";

	private static Logger logger = LoggerFactory.getLogger(Util.class);
	
	private static String OPERATING_SYSTEM = System.getProperty("os.name").toLowerCase();
	
	private static String SALTCHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
	
	private static final List<String> ALLOWED_EXT = Arrays.asList(new String[]{".xlsx"});
	
	public static void createFolders(Path path) throws IOException {
		if (!Files.exists(path)) {
			Files.createDirectories(path);
		}
	}
	
	public static void moveFolders(Path src,Path dest) throws IOException {
		Files.move(src, dest);
	}
	
	public static void uploadTestCaseFile(MultipartFile file,Path path) throws IOException {
		logger.info("Creating parent dirs at ="+path.getParent());
		Files.createDirectories(path.getParent());
		String fileExtension = getFileExtensionWithDot(file.getOriginalFilename());
		Path resolve = path.getParent().resolve(path.getFileName()+fileExtension);
		if(resolve.toString().contains(Util.MASTER)) {
			if(!Files.exists(resolve)) {
				Files.copy(file.getInputStream(), resolve);
			}
		} else {
			Files.copy(file.getInputStream(), resolve,StandardCopyOption.REPLACE_EXISTING);
		}
		logger.info("Creating file at ="+resolve);
	}
	
	private static String getFileExtensionWithDot(String fileName) {
        if(fileName.lastIndexOf(".") != -1 && fileName.lastIndexOf(".") != 0) {
			String fileExt = fileName.substring(fileName.lastIndexOf("."));
			if(ALLOWED_EXT.contains(fileExt.toLowerCase())) {
				return fileExt;
			}
		}
        throw new RuntimeException("Ivnalid Test case file type.Allowed file types are "+ALLOWED_EXT)
        ;
    }

	public static Path getCompleteFilePath(String fileStorageBasePath,String companyName, String applicationName,String environmentName,String username, 
			String testCaseName) {
		return Paths.get(fileStorageBasePath, Util.COMPANIES_BASE_FOLDER_NAME,
				companyName,applicationName,Util.TEST_DATA,username,environmentName,testCaseName);
	}

	public static final String MASTER = "MASTER";

	public static final String TEST_DATA = "TestData";

	public static synchronized Long getEpochTimeInMillis() {
		return System.currentTimeMillis();
	}

	/**
	 * seleniumhome\companies\<company-name>\<user-name>\<execution-name>\3 files
	 * @param executionXml
	 * @param executionConfig
	 * @param executionBat
	 * @param isReRun TODO
	 * @param path 
	 * @throws TransformerException 
	 */
	public static String createExecutionFiles(DOMSource executionXml, String executionConfig, String executionBat, Path sourcePath, boolean isReRun) throws TransformerException {
		//TestNGExecutor.xml file creation
		////<!DOCTYPE suite SYSTEM "http://testng.org/testng-1.0.dtd">
		
		String executorXmlFilePath = getExecutorXmlFilePath(sourcePath);
		if(isReRun) {
			backup(sourcePath);
		}
		
		TransformerFactory transformerFactory = TransformerFactory.newInstance();
	    Transformer transformer = transformerFactory.newTransformer();
	    transformer.setOutputProperty(OutputKeys.INDENT, "yes");
	    transformer.setOutputProperty(OutputKeys.DOCTYPE_SYSTEM, "http://testng.org/testng-1.0.dtd");
	    transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
		File f = new File(executorXmlFilePath);
		StreamResult result = new StreamResult(f);
		transformer.transform(executionXml, result);
		
		
		try (OutputStream outStream = new FileOutputStream(new File(Paths.get(sourcePath.toString(), EXECUTION_CONFIG_FILE_NAME).toString()));){
			outStream.write(executionConfig.getBytes(),0,executionConfig.length());
		} catch (IOException e) {
			logger.error("Error occured while creating configuration file at "+sourcePath,e);
		}
		
		String batchFilePath = Paths.get(sourcePath.toString(), EXECUTION_BAT_FILE_NAME).toString();
		try (OutputStream outStream = new FileOutputStream(new File(batchFilePath));){
			outStream.write(executionBat.getBytes(),0,executionBat.length());
		} catch (IOException e) {
			logger.error("Error occured while creating the batch file at "+sourcePath,e);
		}
		
		return batchFilePath;
		
	}

	public static void backup(Path sourcePath) {
		try {
			Path destPath = Paths.get(sourcePath.toString(), String.valueOf(new Date().getTime()));
			Util.createFolders(destPath);
			
			Files.copy(Paths.get(sourcePath.toString(), EXECUTION_BAT_FILE_NAME), 
					Paths.get(destPath.toString(), EXECUTION_BAT_FILE_NAME));
			Files.deleteIfExists(Paths.get(sourcePath.toString(), EXECUTION_BAT_FILE_NAME));
			
			Files.copy(Paths.get(sourcePath.toString(), EXECUTION_CONFIG_FILE_NAME), 
					Paths.get(destPath.toString(), EXECUTION_CONFIG_FILE_NAME));
			Files.deleteIfExists(Paths.get(sourcePath.toString(), EXECUTION_CONFIG_FILE_NAME));
			
			Files.copy(Paths.get(sourcePath.toString(), EXECUTION_XML_FILE_NAME), 
					Paths.get(destPath.toString(), EXECUTION_XML_FILE_NAME));
			Files.deleteIfExists(Paths.get(sourcePath.toString(), EXECUTION_XML_FILE_NAME));
		} catch (IOException e) {
			logger.error("Error occured while backing up the file before re run files.",e);
		}
	}

	public static String getExecutorXmlFilePath(Path sourcePath) {
		return Paths.get(sourcePath.toString(), EXECUTION_XML_FILE_NAME).toString();
	}

	public static JavaMailSender buildJavaMailSender(String host, int port, String userName, 
			String password, 
			String protocol){
		logger.info("host="+host+" port="+port+" userName="+userName+" password="+password+" protocol="+protocol);
		JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
	    /*String host = "smtp.gmail.com";
	    int port = 587;
	    String userName = "dipak.pravin.87@gmail.com";
	    String password = "password";
	    String protocol = "smtp";*/
	    
		mailSender.setHost(host);
		mailSender.setPort(port);
		mailSender.setUsername(userName);
	    mailSender.setPassword(password);
	     
	    Properties props = mailSender.getJavaMailProperties();
		props.put("mail.transport.protocol", protocol.toLowerCase());
	    props.put("mail.smtp.auth", "true");
	    props.put("mail.smtp.starttls.enable", "true");
	    props.put("mail.debug", "true");
	    return mailSender;
	}

	public static final String NEXT_LINE_STR = System.getProperty("line.separator");
	
	public static final String HTML_NEXT_LINE_STR = "<br/>";

	public static void executebatchFile(String batchFilePath, Boolean asynchMode, String userDir) throws IOException {
		logger.info("start of batch file execution");

		boolean isIsTest = StringUtils.isBlank(batchFilePath) ;

		ProcessBuilder pb = null;
		String randomStr = getRandomStr(9);
		File processOutputFile = Paths.get(userDir,"logs", randomStr+"."+LOG).toFile();
		logger.info("Redirecting the Automation job logs to path="+processOutputFile.toPath().toString());
		if (OPERATING_SYSTEM.indexOf("win") >= 0) {
			logger.info("You are using Windows");
			List<String> windCmds = new ArrayList<>();
			windCmds.add("cmd");
			windCmds.add("/B /c");
			if(isIsTest) {
				windCmds.add("c:/start.bat");
			} else {
				windCmds.add(batchFilePath);
			}
			if(asynchMode) {
				windCmds.add("--headless");
			}
			pb = new ProcessBuilder( windCmds);
			processOutputFile.createNewFile();
			pb.redirectOutput(processOutputFile);
			pb.start();
		} else if (OPERATING_SYSTEM.indexOf("mac") >= 0) {
			logger.info("You are using Mac");
			String[] cmdScript = null;
			if(isIsTest) {
				cmdScript = new String[]{"/bin/bash", "/Users/amrutashingavi/start.sh"};
			}else {
				cmdScript = new String[]{"/bin/bash", batchFilePath};
			}
			//			Runtime.getRuntime().exec(cmdScript);
			pb = new ProcessBuilder(cmdScript);
			pb.redirectOutput(processOutputFile);
			processOutputFile.createNewFile();
			pb.start();
		} else if (OPERATING_SYSTEM.indexOf("nix") >= 0 || OPERATING_SYSTEM.indexOf("nux") >= 0 || OPERATING_SYSTEM.indexOf("aix") > 0 ) {
			logger.info("You are using Unix or Linux");
		} else if (OPERATING_SYSTEM.indexOf("sunos") >= 0) {
			logger.info("You are using Solaris");
		} else {
			logger.info("We can not find your OS!!");
		}
		
		logger.info("Batch file executed");

	}

	public static String getString(String commaSeparatedAsciiInt) {
		if(null==commaSeparatedAsciiInt || commaSeparatedAsciiInt.trim().isEmpty()) {
			return "";
		}
		String finalStr = Arrays.stream(commaSeparatedAsciiInt.split(",")).
		map(s -> String.valueOf((char)Integer.parseInt(s))).collect(Collectors.joining(""));
		return finalStr;
	}
	
	public static String getExecutionNamePostFix() {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("_YYYY_MM_dd_HH_mm");
    	return simpleDateFormat.format(new Date());
	}

	public static final String DD_MM_YYYY_HH_MM_SS = "dd-MM-YYYY HH:mm:ss";
	
	public static final String YYYY_MM_DD_HH_MM_SS = "YYYY_MM_dd_HH_mm_ss";

	public static final List<String> HEADERS=Arrays.asList(new String[] {"SR. NO.","RUN NAME","TEST NAME","RESULT","OUTPUT","ELAPSE TIME","EXECUTION TIME"});
	public static final List<String> HEADERS_SCHEDULED=Arrays.asList(new String[] {"SR. NO.","TEST NAME","CLASS NAME","PERFORMACE SUITE","Available","STATUS"});
	
	public static String getRandomStr(int strLength){
        StringBuilder salt = new StringBuilder();
        Random rnd = new Random();
        while (salt.length() < strLength) { // length of the random string.
            int index = (int) (rnd.nextFloat() * SALTCHARS.length());
            salt.append(SALTCHARS.charAt(index));
        }
        String saltStr = salt.toString();
        return saltStr;
	}

	public static final String SCREENSHOT = "screenshot";

	public static final String LOG = "log";

	public static final String FAILED_TC_SCREENSHOT_EXT = ".png";
	public static final String TC_LOG_FILE_EXT = ".log";

	public static final String COMPANIES_BASE_FOLDER_NAME = "Companies";

	public static final String MASTER_FOLDER_NAME = "Master";

	public static final String TEST_DATA_FOLDER_NAME = "TestData";

	public static void populateResults(List<ExecutionResultRunnerInfo> findAllRunner, Map<Long, LatestExecutionSummary> map,
			boolean isFull) {
		findAllRunner.forEach(er -> {
			if (!map.containsKey(er.getRunnerId())) {
				LatestExecutionSummary value = null;
				if (isFull) {
					value = new LatestExecutionSummary(er.getRunnerId(), er.getRunName(), er.getExecutedBy(),
							er.getExecutedOn(), 0l, 0l, 0l);
				} else {
					value = new LatestExecutionSummary(er.getRunnerId(), 0l, 0l, 0l);
				}
				map.put(er.getRunnerId(), value);
			}
			LatestExecutionSummary latestExecutionSummary = map.get(er.getRunnerId());
			EXECUTION_RESULT valueOf = EXECUTION_RESULT.valueOf(er.getResult().toUpperCase());
			switch (valueOf) {
			case QUEUED:
				latestExecutionSummary.setQueuedTestCasesCount(er.getCompletionPercentage());
				break;
			case PASSED:
				latestExecutionSummary.setPassedTestCasesCount(er.getCompletionPercentage());
				break;
			case FAILED:
				latestExecutionSummary.setFailedTestCasesCount(er.getCompletionPercentage());
				break;
			default:
				break;
			}
	
		});
	}

	public static String getTestCaseResultHtml(LatestExecutionExtraSummary expandedER, Iterable<Object[]> testCaseInfo) {
		StringBuffer sf = new StringBuffer();
		sf.append("Dear ").append(expandedER.getExecutedBy()).append(",").append(HTML_NEXT_LINE_STR).
		append("Execution Name : ").append(expandedER.getRunName()).append(HTML_NEXT_LINE_STR).
		append("Environment Name : ").append(expandedER.getEnvName()).append(HTML_NEXT_LINE_STR).
		append("Browser Name : ").append(expandedER.getBrowserName()).append(HTML_NEXT_LINE_STR).
		append("Triggered By : ").append(expandedER.getExecutedBy()).append(HTML_NEXT_LINE_STR).
		append("Triggered At : ").append(expandedER.getExecutedOn()).append(HTML_NEXT_LINE_STR).
		append("<html>").
		append("<table border='1'>").
		append(" <thead>").
		append(" <tr>").
		append(" <th>Sr.No.</th>").
		append(" <th>Test Name</th>").
		append(" <th>Result</th>").
		append(" </tr>").
		append(" </thead>").
		append(" <tbody>");
		AtomicInteger srNo = new AtomicInteger(0);
		testCaseInfo.forEach(tc -> {
			String color = EXECUTION_RESULT.PASSED.toString().equalsIgnoreCase((String) tc[1])?"green":"red";
			sf.append(" <tr>").
			append("<td>").append(srNo.incrementAndGet()).append("</td>").
			append("<td>").append(tc[0]).append("</td>").
			append("<td style='color:").append(color).append("'><b>").append(tc[1]).append("</td>").
			append(" </tr>");
		});
		sf.append(" </tbody>").
		append(" </table>").
		append("<html>").append(HTML_NEXT_LINE_STR).append(HTML_NEXT_LINE_STR).
		append("Thanks").append(HTML_NEXT_LINE_STR).
		append("Team GetAutomationDone");
		return sf.toString();
	}

}
