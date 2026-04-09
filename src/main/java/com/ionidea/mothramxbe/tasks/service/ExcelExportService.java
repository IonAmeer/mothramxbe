package com.ionidea.mothramxbe.tasks.service;

import com.ionidea.mothramxbe.tasks.model.Report;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.List;

@Service
public class ExcelExportService {

    public ByteArrayInputStream exportReports(List<Report> reports) {

        try (Workbook workbook = new XSSFWorkbook()) {

            //sheet name is Reports
            Sheet sheet = workbook.createSheet("Reports");

            //header row index = 0
            Row header = sheet.createRow(0);

            //column names
            header.createCell(0).setCellValue("Developer Name");
            header.createCell(1).setCellValue("Status");
            header.createCell(2).setCellValue("Month");
            header.createCell(3).setCellValue("Approved By");

            int rowIdx = 1;

            for (Report report : reports) {
                //for each report create new row
                Row row = sheet.createRow(rowIdx++);

                //cl : 0 developer name
                row.createCell(0).setCellValue(
                        report.getUser().getName()
                );

                //cl : 1 report status
                row.createCell(1).setCellValue(
                        report.getStatus()
                );

                //cl : 2 month reference
                row.createCell(2).setCellValue(
                        report.getRefMonthId()
                );

                //cl : 3 approved by
                row.createCell(3).setCellValue(
                        report.getApprovedBy() != null ? report.getApprovedBy() : "-"
                );
            }

            //create output stream to hold excel data in memory
            ByteArrayOutputStream out = new ByteArrayOutputStream();

            //write workbook content into the output stream
            workbook.write(out);

            return new ByteArrayInputStream(out.toByteArray());

        } catch (Exception e) {
            throw new RuntimeException("Failed to export Excel", e);
        }
    }

}