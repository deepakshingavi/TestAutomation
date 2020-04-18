package com.dyteam.testApps.webserver.view;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.dyteam.testApps.webserver.Util;
import com.dyteam.testApps.webserver.entity.ExecutionResults;
import com.dyteam.testApps.webserver.entity.ScheduledExecution;
import com.dyteam.testApps.webserver.entity.ScheduledExecutionBk;
import com.dyteam.testApps.webserver.entity.Testcases;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;

public class GeneratePdfReport {

	public static ByteArrayInputStream getExecutionResults(Iterable<ExecutionResults> executionResults) {
		Document document = new Document();
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        try {
            PdfPTable table = new PdfPTable(Util.HEADERS.size());
            table.setWidthPercentage(100);
            
            Font headFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD);

            Util.HEADERS.forEach( h -> {
            	PdfPCell hcell = new PdfPCell(new Phrase(h, headFont));
                hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(hcell);
            });
            
            AtomicInteger count=new AtomicInteger(1);
            executionResults.forEach(e -> {
            	PdfPCell srNocell = new PdfPCell(new Phrase(""+count.get()));
                srNocell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                srNocell.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(srNocell);
                
                PdfPCell runNameCell = new PdfPCell(new Phrase(e.getExecutionName()));
                runNameCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                runNameCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(runNameCell);
                
                PdfPCell testNameCell = new PdfPCell(new Phrase(e.getTestcases().getTestcaseName()));
                testNameCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                testNameCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(testNameCell);
                
                PdfPCell resultCell = new PdfPCell(new Phrase(e.getResult()));
                resultCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                resultCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(resultCell);
                
                PdfPCell outputCell = new PdfPCell(new Phrase(e.getOutput()));
                outputCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                outputCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(outputCell);
                
                PdfPCell elaspedTimeCell = new PdfPCell(new Phrase(e.getElapsedTime()+""));
                elaspedTimeCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                elaspedTimeCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(elaspedTimeCell);
                
                String executionTimeStr="";
                
                if(null!=e.getExecutionStartDate()) {
                	executionTimeStr = new SimpleDateFormat(Util.DD_MM_YYYY_HH_MM_SS).format(e.getExecutionStartDate());
                }
                PdfPCell executionTimeCell = new PdfPCell(new Phrase(executionTimeStr));
                executionTimeCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                executionTimeCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(executionTimeCell);
                /*if(null != e.getTestcaseExeDetailList()) {
                	AtomicInteger innerCount=new AtomicInteger(0);
                	e.getTestcaseExeDetailList().forEach(ted -> {
                		
                		PdfPCell srNoInnercell = new PdfPCell(new Phrase(count.get()+"."+innerCount.incrementAndGet()));
                        srNoInnercell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                        srNoInnercell.setHorizontalAlignment(Element.ALIGN_CENTER);
                        table.addCell(srNoInnercell);
                		
                		PdfPCell elapsedTimeCell = new PdfPCell(new Phrase(ted.getElapsedTime()));
                        elapsedTimeCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                        elapsedTimeCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                        table.addCell(elapsedTimeCell);
                        
                        PdfPCell outputInnerCell = new PdfPCell(new Phrase(ted.getOutput()));
                        outputInnerCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                        outputInnerCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                        table.addCell(outputInnerCell);
                        
                        PdfPCell resultInnerCell = new PdfPCell(new Phrase(ted.getResult()));
                        resultInnerCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                        resultInnerCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                        table.addCell(resultInnerCell);
                        
                        table.addCell("");
                        table.addCell("");
                        table.addCell("");
                	});
                }*/
                count.incrementAndGet();
                
            });
            
            PdfWriter.getInstance(document, out);
            document.open();
            document.add(table);
            document.close();
            
            
        } catch (DocumentException ex) {
            
            Logger.getLogger(GeneratePdfReport.class.getName()).log(Level.SEVERE, null, ex);
        }
            
        return new ByteArrayInputStream(out.toByteArray());
	}

	public static ByteArrayInputStream getScheduledExecutionResults(ScheduledExecution scheduledExecution) {
		Document document = new Document();
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        try {
            PdfPTable table = new PdfPTable(Util.HEADERS_SCHEDULED.size());
            table.setWidthPercentage(100);
            
            Font headFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD);

            Util.HEADERS_SCHEDULED.forEach( h -> {
            	PdfPCell hcell = new PdfPCell(new Phrase(h, headFont));
                hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(hcell);
            });
            
            AtomicInteger count=new AtomicInteger(1);
            List<Testcases> testCaseList = scheduledExecution.getTestCaseList();
            testCaseList.forEach(tc -> {
            	PdfPCell srNocell = new PdfPCell(new Phrase(count.get()+""));
                srNocell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                srNocell.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(srNocell);
                
                PdfPCell testcaseNameCell = new PdfPCell(new Phrase(tc.getTestcaseName()));
                testcaseNameCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                testcaseNameCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(testcaseNameCell);
                
                PdfPCell classNameCell = new PdfPCell(new Phrase(tc.getClassName()));
                classNameCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                classNameCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(classNameCell);
                
                PdfPCell isPerfSuiteCell = new PdfPCell(new Phrase(new Boolean(tc.getIsPerfSuite()==1).toString()));
                isPerfSuiteCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                isPerfSuiteCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(isPerfSuiteCell);
                
                PdfPCell isAvailableCell = new PdfPCell(new Phrase(new Boolean(tc.getIsAvailbale()==1).toString()));
                isAvailableCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                isAvailableCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(isAvailableCell);
                
                PdfPCell statusCell = new PdfPCell(new Phrase(new Boolean(tc.getStatus()==1).toString()));
                statusCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                statusCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(statusCell);
                
//                table.addCell("NA");
                
                count.incrementAndGet();
                
            });
            
            PdfWriter.getInstance(document, out);
            document.open();
            document.add(table);
            document.close();
            
            
        } catch (DocumentException ex) {
            
            Logger.getLogger(GeneratePdfReport.class.getName()).log(Level.SEVERE, null, ex);
        }
            
        return new ByteArrayInputStream(out.toByteArray());
	}

	public static ByteArrayInputStream getScheduledExecutionBkResults(ScheduledExecutionBk scheduledExecutionBk) {
		// TODO Auto-generated method stub
		return null;
	}
	
}
