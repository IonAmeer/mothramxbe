package com.ionidea.mothramxbe.tasks.service;

import com.ionidea.mothramxbe.tasks.model.JiraEntry;
import com.ionidea.mothramxbe.tasks.model.LeaveEntry;
import com.ionidea.mothramxbe.tasks.model.Report;
import com.ionidea.mothramxbe.tasks.repository.ReportRepository;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.poi.ss.usermodel.*;

@Service
public class ExcelExportService {

    private final ReportRepository reportRepository;

    @Autowired
    public ExcelExportService(ReportRepository reportRepository) {
        this.reportRepository = reportRepository;
    }

    public ByteArrayInputStream exportReports(List<Report> reports) {

        try (Workbook workbook = new XSSFWorkbook()) {

            Sheet sheet = workbook.createSheet("Reports");

            int rowIdx = 0;

            // ===== GROUP BY DEVELOPER =====
            Map<Long, List<Report>> grouped = reports.stream().collect(Collectors.groupingBy(r -> r.getUser().getId()));

            // ===== STYLES =====
            CellStyle headerStyle = workbook.createCellStyle();
            Font boldFont = workbook.createFont();
            boldFont.setBold(true);
            headerStyle.setFont(boldFont);
            CellStyle wrapStyle = workbook.createCellStyle();
            wrapStyle.setWrapText(true);

            for (Map.Entry<Long, List<Report>> entry : grouped.entrySet()) {

                List<Report> devReports = entry.getValue();

                if (devReports == null || devReports.isEmpty()) {
                    continue;
                }

                String developerName = devReports.get(0).getUser().getName();

                // ===== HEADER =====
                Row header = sheet.createRow(rowIdx++);
                String[] cols = {
                        "Sl No", "Task Name", "Assigned To",
                        "Estimated", "Days Spent", "Remaining", "Leaves"
                };

                for (int i = 0; i < cols.length; i++) {
                    Cell cell = header.createCell(i);
                    cell.setCellValue(cols[i]);
                    cell.setCellStyle(headerStyle);
                }

                int slNo = 1;

                for (Report report : devReports) {
                    List<JiraEntry> jiraList = report.getJiraEntries() != null ? report.getJiraEntries() : List.of();

                    List<LeaveEntry> leaveList = report.getLeaveEntries() != null ? report.getLeaveEntries() : List.of();

                    int maxRows = Math.max(jiraList.size(), leaveList.size());

                    for (int i = 0; i < maxRows; i++) {
                        Row row = sheet.createRow(rowIdx++);

                        if (i < jiraList.size()) {
                            JiraEntry jira = jiraList.get(i);

                            int sp = jira.getStoryPoints() != null ? jira.getStoryPoints() : 0;
                            int spent = jira.getDaysSpent() != null ? jira.getDaysSpent() : 0;
                            int remaining = sp - spent;

                            String task = (jira.getTicketId() != null ? jira.getTicketId() : "") + " - " + (jira.getDescription() != null ? jira.getDescription() : "");

                            row.createCell(0).setCellValue(slNo++);
                            row.createCell(1).setCellValue(task);
                            row.createCell(2).setCellValue(developerName);
                            row.createCell(3).setCellValue(sp);
                            row.createCell(4).setCellValue(spent);
                            row.createCell(5).setCellValue(remaining);

                        }

                        if (i < leaveList.size()) {
                            LeaveEntry leave = leaveList.get(i);

                            String leaveText = (leave.getDate() != null ? leave.getDate() : "") + " - " + (leave.getReason() != null ? leave.getReason() : "");

                            Cell leaveCell = row.createCell(6);
                            leaveCell.setCellValue(leaveText);
                            leaveCell.setCellStyle(wrapStyle);
                        }
                    }
                }
                rowIdx++;
            }

            // ===== AUTO SIZE =====
            for (int i = 0; i < 7; i++) {
                sheet.autoSizeColumn(i);
            }

            ByteArrayOutputStream out = new ByteArrayOutputStream();
            workbook.write(out);

            return new ByteArrayInputStream(out.toByteArray());

        } catch (Exception e) {
            throw new RuntimeException("Failed to export Excel", e);
        }
    }

}