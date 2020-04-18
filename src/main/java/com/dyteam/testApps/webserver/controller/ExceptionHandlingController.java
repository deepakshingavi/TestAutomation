package com.dyteam.testApps.webserver.controller;

import java.sql.SQLIntegrityConstraintViolationException;

import javax.validation.ValidationException;

import org.hibernate.exception.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.dyteam.testApps.webserver.TestAppException;

/**
 * This controller takes care of handling all exception occurred on Server side.
 * @author deepak
 */
@ControllerAdvice
public class ExceptionHandlingController extends ResponseEntityExceptionHandler {

	
	/**
	 * Handles all exception related to SQL foreign key constraint & unique keys
	 * @param ex The DataIntegrityViolationException object which has occurred while CRUD operation on Server side
	 * @param request - The request for which the error has occurred
	 * @return
	 */
	@ExceptionHandler(value 
		      = { DataIntegrityViolationException.class})
		    protected ResponseEntity<Object> handleConflict(
		    		RuntimeException ex, WebRequest request) {
		ConstraintViolationException cause = (ConstraintViolationException)ex.getCause();
		String constraintName = cause.getConstraintName();
		String bodyOfResponse = "Duplicate name.";
		if(null==constraintName) {
			String message = ((SQLIntegrityConstraintViolationException)cause.getCause()).getMessage();
			if(message.contains("environment_id_fk_ceu")) {
				constraintName = "environment_id_fk_ceu";
			}else if(message.contains("environment_id_fk_lg")) {
				constraintName = "environment_id_fk_lg";
			}else if(message.contains("environment_id_fk_er")) {
				constraintName = "environment_id_fk_er";
			}else if(message.contains("environment_id_fk_se")) {
				constraintName = "environment_id_fk_se";
			}else if(message.contains("environment_id_fk_se_bk")) {
				constraintName = "environment_id_fk_se_bk";
			}
		}
		switch(constraintName) {
		case "environment_uniq":
			bodyOfResponse = "Duplicate Environment name.";
			break;
		case "browser_uniq":
			bodyOfResponse = "Duplicate Browser name.";
			break;
		case "user_uk":
			bodyOfResponse = "Duplicate Email id.";
			break;
		case "user_uk1":
			bodyOfResponse = "Duplicate User name.";
			break;
		case "application_uk":
			bodyOfResponse = "Duplicate Application name.";
			break;
		case "testcase_uk":
			bodyOfResponse = "Duplicate Test case name.";
			break;
		case "execution_user_uk":
			bodyOfResponse = "Duplicate Execution User name.";
			break;
		case "company_name":
			bodyOfResponse = "Duplicate Company name.";
			break;
		case "email":
			bodyOfResponse = "Duplicate Company email name.";
			break;
		case "environment_id_fk_ceu":
			bodyOfResponse = "Environment is being used in one or more Comapny URLs.";
		case "environment_id_fk_lg":
			bodyOfResponse = "Environment is being used in one or more Comapny URLs.";
		case "environment_id_fk_er":
			bodyOfResponse = "Environment is being used in one or more Execution results.";
		case "environment_id_fk_se":
			bodyOfResponse = "Environment is being used in one or more Scheduled executions.";
		case "environment_id_fk_se_bk":
			bodyOfResponse = "Environment is being used in one or more completed Schedule executions.";
		default :
			break;
		}
		
		return handleExceptionInternal(ex, bodyOfResponse, 
				new HttpHeaders(), HttpStatus.CONFLICT, request);
	}
	
	/**
	 * Handles the basic validation and displays the error message with HTTP response status as 409
	 * @param ex The DataIntegrityViolationException object which has occurred while CRUD operation on Server side
	 * @param request - The request for which the error has occurred
	 * @return
	 */
	@ExceptionHandler(value 
		      = { javax.validation.ConstraintViolationException.class})
		    protected ResponseEntity<Object> handleUiValidation(
		    		javax.validation.ConstraintViolationException ex, WebRequest request) {
		StringBuffer sbf = new StringBuffer();
		ex.getConstraintViolations().forEach( cv -> {
			sbf.append(cv.getPropertyPath()).append(" ").append(cv.getMessage()).append("\n");
		});;
		String errorMessage=sbf.toString();
		return handleExceptionInternal(ex, errorMessage, 
				new HttpHeaders(), HttpStatus.CONFLICT, request);
	}
	
	/**
	 * Handles the business validation and displays the error message with HTTP response status as 409
	 * @param ex The TestAppException object which has occurred while CRUD operation on Server side
	 * @param request - The request for which the error has occurred
	 * @return
	 */
	@ExceptionHandler(value 
		      = { TestAppException.class})
		    protected ResponseEntity<Object> handleCustomException(
		    		TestAppException ex, WebRequest request) {
		
		String bodyOfResponse = ex.getErrorMessage();
		
		return handleExceptionInternal(ex, bodyOfResponse, 
				new HttpHeaders(), HttpStatus.CONFLICT, request);
	}
	
	/**
	 * Handles the business validation and displays the error message with HTTP response status as 409
	 * @param ex The TestAppException object which has occurred while CRUD operation on Server side
	 * @param request - The request for which the error has occurred
	 * @return
	 */
	@ExceptionHandler(value 
		      = { ValidationException.class})
		    protected ResponseEntity<Object> handleValidationException(
		    		ValidationException ex, WebRequest request) {
		String bodyOfResponse = ex.getMessage();
		return handleExceptionInternal(ex, bodyOfResponse, 
				new HttpHeaders(), HttpStatus.CONFLICT, request);
	}

}
