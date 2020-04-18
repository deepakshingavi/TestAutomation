package com.dyteam.testApps.webserver.service;

import java.io.File;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.dyteam.testApps.webserver.Util;
import com.dyteam.testApps.webserver.entity.Company;
import com.dyteam.testApps.webserver.repository.CompanyRepository;

/**
 * Handle email related logic 
 * @author deepak
 */
@Service
public class EmailServiceImpl implements IEmailService {
  
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	CompanyRepository companyRepo;
	
	@Value("${email.password.key}") 
    String key;
	
	/**
     * Sends simple email with body as HTML
     */
	@Override
    @Async
    public void sendSimpleMessage(
      String to, String subject, String text, Company company) throws MessagingException {
    	String decodePassword = companyRepo.getDecodePassword(company.getPassword(),key);
    	if(null!=decodePassword) {
    		String password = Util.getString(decodePassword);
    		
    		JavaMailSender emailSender = Util.buildJavaMailSender(company.getHostName(), 
    				Integer.parseInt(company.getPort()), 
    				company.getEmail(), password, company.getSecuirityProtocol());
    		
    		MimeMessage message = emailSender.createMimeMessage();
            MimeMessageHelper messageHelper = new MimeMessageHelper(message);
    		
            messageHelper.setTo(to); 
            messageHelper.setSubject(subject); 
            messageHelper.setText(text,true);
    		emailSender.send(message);
    	} else {
    		logger.error("Could decrypt email password from the given key.");
    	}
    }
    
	/**
     * Sends simple email with body as text and with an attachment
     */
    @Override
    public void sendMessageWithAttachment(
      String to, String subject, String text, String pathToAttachment,JavaMailSender emailSender) throws MessagingException {
         
        MimeMessage message = emailSender.createMimeMessage();
          
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
         
        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(text);
             
        FileSystemResource file 
          = new FileSystemResource(new File(pathToAttachment));
        helper.addAttachment("Invoice", file);
     
        emailSender.send(message);
    }
    
    @Override
    public void testEmail(String host, int port, String userName, String password, String protocol, String toEmail) throws MessagingException {
    	JavaMailSender emailSender = Util.buildJavaMailSender(host, port, userName, password, protocol);
    	
    	MimeMessage message = emailSender.createMimeMessage();
        MimeMessageHelper messageHelper = new MimeMessageHelper(message);
		
        messageHelper.setTo(toEmail); 
        messageHelper.setSubject("Test mail"); 
        messageHelper.setText("Test email for tsting email configs",true);
		emailSender.send(message);
    	
    }

}
