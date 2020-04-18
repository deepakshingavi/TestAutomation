package com.dyteam.testApps.webserver.service;

import javax.mail.MessagingException;

import org.springframework.mail.javamail.JavaMailSender;

import com.dyteam.testApps.webserver.entity.Company;

public interface IEmailService {

	void sendSimpleMessage(String to, String subject, String text, Company company) throws MessagingException;

	void sendMessageWithAttachment(String to, String subject, String text, String pathToAttachment,
			JavaMailSender emailSender) throws MessagingException;

	void testEmail(String host, int port, String userName, String password, String protocol, String toEmail)
			throws MessagingException;

}
