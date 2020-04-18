alter table company_environ_url modify environment_type varchar(255) NOT NULL default 'dd',modify company_id bigint(20) NOT NULL default 1,modify company_env_id bigint(20) NOT NULL default 1;
alter table user modify added_by_name varchar(255) NOT NULL DEFAULT '';
alter table logical_group modify created_by_name varchar(60) NOT NULL DEFAULT '',modify created_by varchar(15) NOT NULL DEFAULT '',modify created_email varchar(60) NOT NULL DEFAULT '';

alter table user drop column created_at1;
alter table testcases drop column user_type;
alter table tce_testcases_results add column company_id bigint(20) NOT NULL default 1;
alter table tce_testcases_results drop column COMPANY;
update tce_testcases_results set company_id=43 where `COMPANY`='AHS'; 
update tce_testcases_results set company_id=42 where `COMPANY`='ServiceMaster'; 
update tce_testcases_results set company_id=44 where `COMPANY`='PartnerITSM'; 
--https://myaccount.google.com/lesssecureapps

alter table environment add constraint UNIQUE KEY environment_uniq (environment_name,company_id);

alter table `company`
   modify user_id bigint(20)  NULL default 1,
   modify contact_landline varchar(20) NULL default '',
   modify status int(1)  NULL default 1,
   modify is_Url_add int(1)  NULL default 1,
   modify pass_status int(1)  NULL default 1,
   modify domain varchar(255)  NULL default '',
	add `selenium_home` text NOT NULL default '',
	add `test_data_home` text NOT NULL default '',
  	add `screen_shots_home` text NOT NULL default '',
  	add `batch_file_home` text NOT NULL default '',
  	add `logs_home` text NOT NULL default '',
	add `host_name` varchar(255) NOT NULL default '',
	add `port` varchar(10) NOT NULL default '',
	add `username` varchar(255) NOT NULL default '',
	add `secuirity_protocol` varchar(255) NOT NULL default '',
	add `code` text NOT NULL default '';
	
alter table `testcases` add column `is_perf_suite` int(1) NOT NULL default 1;
  	
INSERT INTO `user` (`user_id`, `user_type`, `company_id`, `fname`, `lname`, `email`, `password`, `designation`, `user_name`, `contact`, `address`, `profile_img`, `description`, `added_by`, `ref_user_id`, `added_by_name`, `pass_status`, `createdAt`, `status`,`login_id`) VALUES
(0,2,'0', 'Samir', 'Tripathy', 'partnerit@gmail.com', '518e6c76caf51eb410caab3c21def1b4b3c07401','','partnerit', '9890344229', 'pune', '', '',0, 0, 'Super User', 0, '2017-12-08 10:36:43',0,'partnerit');

INSERT INTO `company` (`company_id`, `company_name`, `user_id`, `email`, `password`, `domain`, `contact_landline`, `contact`, `address`, `is_Url_add`, `status`, `pass_status`, `createdAt`) VALUES
(0, 'Super User Company', 0, 'partnerit@gmail.com', '518e6c76caf51eb410caab3c21def1b4b3c07401', '', '', '9890344229', 'pune', 0, 0, 0, '2017-12-08 10:36:43');

update company set host_name='ssl://smtp.googlemail.com',port=465,username='teamautoit+company@gmail.com',secuirity_protocol='SMTP',code='!1drowss@P' where company_id=42;

update company set host_name='ssl://smtp.googlemail.com',port=465,username='rahul41085@gmail.com',secuirity_protocol='SMTP',code='1' where company_id=43;

update company set host_name='ssl://smtp.googlemail.com',port=465,username='deepakshingavi@gmail.com',secuirity_protocol='SMTP',code='ydemer' where company_id=44;


CREATE TABLE IF NOT EXISTS `execution_plan` (
  `execution_plan_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `execution_plan_name` varchar(255) NOT NULL,
  `environment_id` bigint(20) NOT NULL,
  `browser_id` bigint(20) NOT NULL,
  `company_id` bigint(20) NOT NULL,
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `created_by` bigint(20) NOT NULL,
  PRIMARY KEY (`execution_plan_id`),
  UNIQUE KEY `execution_plan_name` (`execution_plan_name`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;


CREATE TABLE IF NOT EXISTS `execution_plan_test_case` (
  `execution_plan_test_case_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `execution_plan_id` bigint(20) NOT NULL,
  `testcase_id` bigint(20) NOT NULL,
  PRIMARY KEY (`execution_plan_test_case_id`),
  UNIQUE KEY `execution_plan_test_case_uk` (`execution_plan_id`,`testcase_id`),
  CONSTRAINT `fk_execution_plan` FOREIGN KEY (`execution_plan_id`) REFERENCES `execution_plan` (`execution_plan_id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_test_case` FOREIGN KEY (`testcase_id`) REFERENCES `testcases` (`testcase_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

CREATE TABLE IF NOT EXISTS `execution_plan_type` (
  `execution_plan_type_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `execution_plan_id` bigint(20) NOT NULL,
  `execution_type_id` bigint(20) NOT NULL,
  PRIMARY KEY (`execution_plan_type_id`),
  UNIQUE KEY `execution_plan_type_uk` (`execution_plan_id`,`execution_type_id`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

CREATE TABLE IF NOT EXISTS `execution_type` (
  `execution_type_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `execution_type_name` varchar(255) NOT NULL,
  PRIMARY KEY (`execution_type_id`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

insert into execution_type values (1,"Logical Group");
insert into execution_type values (2,"Scheduled Execution");

CREATE TABLE `execution_results` (
  `execution_results_id` bigint(50) NOT NULL AUTO_INCREMENT,
  `runner_id` bigint(20) NOT NULL DEFAULT '0',
  `execution_name` varchar(200) NOT NULL,
  `testcase_id` bigint(20) NOT NULL,
  `execution_start_date` timestamp DEFAULT CURRENT_TIMESTAMP,
  `execution_end_date` timestamp DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `result` varchar(200) NOT NULL default 'QUEUED',
  `executed_by_id` bigint(20) NOT NULL,
  `company_id` bigint(20) NOT NULL,
  `reason` varchar(200) default '',
  `added_date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `output` varchar(2000) default '',
  PRIMARY KEY (`execution_results_id`)
  UNIQUE KEY `execution_results_uk` (`company_id`,`execution_name`)
) ENGINE=InnoDB AUTO_INCREMENT=2049 DEFAULT CHARSET=latin1;


CREATE TABLE IF NOT EXISTS `test_cases_logical_group` (
  `test_cases_logical_group_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `testcase_id` bigint(20) NOT NULL,
  `logical_group_id` bigint(20) NOT NULL,
  PRIMARY KEY (`test_cases_logical_group_id`),
  UNIQUE KEY `test_cases_logical_group_uk` (`testcase_id`,`logical_group_id`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=1;

CREATE TABLE IF NOT EXISTS `execution_results_detail` (
  `execution_results_detail_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `execution_results_id` bigint(20) NOT NULL,
  `execution_start_date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `execution_end_date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `RESULT` varchar(20) NOT NULL,
  `REASON` varchar(200) default '',
  `output` varchar(2000) default '',
  PRIMARY KEY (`execution_results_detail_id`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

CREATE TABLE IF NOT EXISTS `scheduled_execution` (
  `scheduled_execution_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `scheduled_execution_name` varchar(200) NOT NULL,
  `scheduled_date` timestamp NOT NULL,
  `browser_id` bigint(20) NOT NULL,
  `environment_id` bigint(20) NOT NULL,
  `scheduled_by_id` bigint(20) NOT NULL,
  `company_id` bigint(20) NOT NULL,
  `execution_user_id` bigint(20) NOT NULL,
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`scheduled_execution_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;


CREATE TABLE IF NOT EXISTS `test_cases_scheduled` (
  `test_cases_scheduled_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `testcase_id` bigint(20) NOT NULL,
  `scheduled_execution_id` bigint(20) NOT NULL,
  PRIMARY KEY (`test_cases_scheduled_id`),
  UNIQUE KEY `test_cases_scheduled_uk` (`testcase_id`,`scheduled_execution_id`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=1;

CREATE TABLE IF NOT EXISTS `scheduled_execution_bk` (
  `scheduled_execution_bk_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `scheduled_execution_name` varchar(200) NOT NULL,
  `scheduled_date` timestamp NOT NULL,
  `browser_id` bigint(20) NOT NULL,
  `environment_id` bigint(20) NOT NULL,
  `scheduled_by_id` bigint(20) NOT NULL,
  `company_id` bigint(20) NOT NULL,
  `execution_user_id` bigint(20) NOT NULL,
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`scheduled_execution_bk_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

CREATE TABLE IF NOT EXISTS `test_cases_scheduled_bk` (
  `test_cases_scheduled_bk_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `testcase_id` bigint(20) NOT NULL,
  `scheduled_execution_id` bigint(20) NOT NULL,
  PRIMARY KEY (`test_cases_scheduled_bk_id`),
  UNIQUE KEY `test_cases_scheduled_bk_uk` (`testcase_id`,`scheduled_execution_id`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=1;

--Added on 24th August	

CREATE TABLE IF NOT EXISTS `testcase_exe_detail` (
  `testcase_exe_detail_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `execution_results_id` bigint(20) NOT NULL,
  `execution_start_date` timestamp ,
  `execution_end_date` timestamp,
  `result` varchar(20) NOT NULL,
  `reason` varchar(20) NOT NULL,
  `output` varchar(2000) default '',
  `added_by` bigint(20) NOT NULL default 0,
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`testcase_exe_detail_id`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=1;

CREATE TABLE IF NOT EXISTS `execution_user` (
  `execution_user_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(20) NOT NULL,
  `password` varchar(200)  NOT NULL,
  `role` varchar(100)  NOT NULL,
  `company_id` bigint(20) NOT NULL,
  `added_by` bigint(20) NOT NULL default 0,
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`execution_user_id`),
  UNIQUE KEY `execution_user_uk` (`company_id`,`name`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=1;

--28 august
alter table logical_group add execution_user_id bigint(20) NOT NULL default 1;
alter table logical_group add UNIQUE KEY `logical_group_uk` (`company_id`,`logical_group_name`);
alter table scheduled_execution add UNIQUE KEY `scheduled_execution_uk` (`company_id`,`scheduled_execution_name`);
alter table application add UNIQUE KEY `application_uk` (`application_name`,`company_id`);
alter table testcases add UNIQUE KEY `testcase_uk` (`testcase_name`,`application_id`);
alter table `user` add UNIQUE KEY `user_uk` (`email`),add UNIQUE KEY `user_uk1` (`user_name`);
alter table testcases add UNIQUE KEY `testcase_uk` (`testcase_name`,`application_id`);

--7 September
alter table user drop column designation,drop column profile_img,drop column description,drop column pass_status;
alter table environment drop column added_by_name;
alter table browser drop column added_by_name;