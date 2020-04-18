package com.dyteam.testApps.webserver.view;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dyteam.testApps.webserver.Util;
import com.dyteam.testApps.webserver.entity.ExecutionResults;
import com.dyteam.testApps.webserver.entity.ScheduledExecution;

public class GenerateExcelReport {
	private static final Logger logger = LoggerFactory.getLogger(GenerateExcelReport.class);

	
	public static ByteArrayInputStream getExecutionResultReport(Iterable<ExecutionResults> executionResults) {
		
		try (Workbook workbook = new XSSFWorkbook();) {
			CellStyle cellBorderStyle = setCellBorder(workbook);
			Sheet sheet = workbook.createSheet("Execution Report");
			
			final AtomicInteger testCaseIndex = new AtomicInteger(0);
			
			final AtomicInteger rowIndex = new AtomicInteger(0);
			Row row = sheet.createRow(rowIndex.get());
			final AtomicInteger colIndex = new AtomicInteger(0);
			createHeader(workbook, row, colIndex);
			
			
			executionResults.forEach( t-> {
				Row rowData = sheet.createRow(rowIndex.incrementAndGet());
				int colCount=0;
				
				colCount = createCellWrapper(cellBorderStyle, testCaseIndex.incrementAndGet()+"", rowData, colCount);
				colCount = createCellWrapper(cellBorderStyle, t.getExecutionName(), rowData, colCount);
				
				colCount = createCellWrapper(cellBorderStyle, t.getTestcases().getTestcaseName(), rowData, colCount);
				colCount = createCellWrapper(cellBorderStyle, t.getResult(), rowData, colCount);
				colCount = createCellWrapper(cellBorderStyle, t.getOutput(), rowData, colCount);
				colCount = createCellWrapper(cellBorderStyle, t.getElapsedTime()+"", rowData, colCount);
				
				String executionTimeStr="";
				if(null!=t.getExecutionStartDate()) {
                	executionTimeStr = new SimpleDateFormat(Util.DD_MM_YYYY_HH_MM_SS).format(t.getExecutionStartDate());
                }
				
				colCount = createCellWrapper(cellBorderStyle, executionTimeStr, rowData, colCount);
				
				/*if(null!=t.getTestcaseExeDetailList()) {
					final AtomicInteger rowInnerIndex = new AtomicInteger(0);
					t.getTestcaseExeDetailList().forEach( td -> {
						Row rowSubData = sheet.createRow(rowIndex.incrementAndGet());
						int colCount1=0;
						
						colCount1 = createCellWrapper(cellBorderStyle, testCaseIndex.intValue()+"."+rowInnerIndex.incrementAndGet(), rowSubData, colCount1);
						colCount1 = createCellWrapper(cellBorderStyle, t.getTestcases().getTestcaseName(), rowSubData, colCount1);
						colCount1 = createCellWrapper(cellBorderStyle, t.getTestcases().getTestcaseName(), rowSubData, colCount1);
						colCount1 = createCellWrapper(cellBorderStyle, td.getResult(), rowSubData, colCount1);
						colCount1 = createCellWrapper(cellBorderStyle, td.getOutput(), rowSubData, colCount1);
						colCount1 = createCellWrapper(cellBorderStyle, td.getElapsedTime()+"", rowSubData, colCount1);
						
						colCount1 = createCellWrapper(cellBorderStyle, "", rowSubData, colCount1);
						colCount1 = createCellWrapper(cellBorderStyle, "", rowSubData, colCount1);
						colCount1 = createCellWrapper(cellBorderStyle, "", rowSubData, colCount1);
					});
				}*/
			});
			
			try (ByteArrayOutputStream bos = new ByteArrayOutputStream();) {
				workbook.write(bos);
				return new ByteArrayInputStream(bos.toByteArray());
			} catch (IOException e) {
				logger.error("Error creaing ByteArrayOutputStream or ByteArrayInputStream of workbook", e);
			} 
			
		} catch (IOException e) {
			logger.error("Error creaing Execution results XLS workbook", e);
		}
		
		return null;
	}

	private static int createCellWrapper(CellStyle cellBorderStyle, final String rowIndex, Row rowData,
			int colCount) {
		Cell createCell = rowData.createCell(colCount++,CellType.STRING);
		createCell.setCellStyle(cellBorderStyle);
		createCell.setCellValue(rowIndex);
		return colCount;
	}

	public static ByteArrayInputStream getScheduledExecutionResultReport(ScheduledExecution scheduledExecution) {
		try (Workbook workbook = new XSSFWorkbook();) {
			setCellBorder(workbook);
			Sheet sheet = workbook.createSheet("Schedule Execution Report");
			final AtomicInteger rowIndex = new AtomicInteger(0);
			Row row = sheet.createRow(rowIndex.get());
			final AtomicInteger colIndex = new AtomicInteger(0);
			createScheduleHeader(workbook, row, colIndex);
			
			CellStyle cellBorderStyle = setCellBorder(workbook);
			scheduledExecution.getTestCaseList().forEach( tc -> {
				Row rowData = sheet.createRow(rowIndex.incrementAndGet());
				int colCount=0;
				
				colCount = createCellWrapper(cellBorderStyle, rowIndex.intValue()+"", rowData, colCount);
				colCount = createCellWrapper(cellBorderStyle, tc.getTestcaseName()+"", rowData, colCount);
				colCount = createCellWrapper(cellBorderStyle, tc.getClassName()+"", rowData, colCount);
				colCount = createCellWrapper(cellBorderStyle, new Boolean(tc.getIsPerfSuite()==1).toString(), rowData, colCount);
				colCount = createCellWrapper(cellBorderStyle, new Boolean(tc.getIsAvailbale()==1).toString(), rowData, colCount);
				colCount = createCellWrapper(cellBorderStyle, new Boolean(tc.getStatus()==1).toString(), rowData, colCount);
//				colCount = createCellWrapper(cellBorderStyle, "NA", rowData, colCount);
				
			});
			
			try (ByteArrayOutputStream bos = new ByteArrayOutputStream();) {
				workbook.write(bos);
				return new ByteArrayInputStream(bos.toByteArray());
			} catch (IOException e) {
				logger.error("Error creaing ByteArrayOutputStream or ByteArrayInputStream of workbook", e);
			} 
			
		} catch (IOException e) {
			logger.error("Error creaing Execution results XLS workbook", e);
		}
		return null;
	}
	
	private static void createHeader(Workbook workbook, Row row, final AtomicInteger colIndex) {
		Util.HEADERS.forEach( h -> {
			Cell createCell = row.createCell(colIndex.intValue());
			createCell.setCellValue(h);
			Font font = workbook.createFont();
			font.setBold(true);
			CellStyle style = setCellBorder(workbook);
			style.setFont(font);
			createCell.setCellStyle(style);
			colIndex.incrementAndGet();
		});
	}
	
	private static void createScheduleHeader(Workbook workbook, Row row, final AtomicInteger colIndex) {
		Util.HEADERS_SCHEDULED.forEach( h -> {
			Cell createCell = row.createCell(colIndex.intValue());
			createCell.setCellValue(h);
			Font font = workbook.createFont();
			font.setBold(true);
			CellStyle style = setCellBorder(workbook);
			style.setFont(font);
			createCell.setCellStyle(style);
			colIndex.incrementAndGet();
		});
	}
	
	private static CellStyle setCellBorder(Workbook workbook) {
		CellStyle style = workbook.createCellStyle();
		style.setBorderBottom(BorderStyle.THIN);
		style.setBorderLeft(BorderStyle.THIN);
		style.setBorderRight(BorderStyle.THIN);
		style.setBorderTop(BorderStyle.THIN);
		return style;
	}

}
