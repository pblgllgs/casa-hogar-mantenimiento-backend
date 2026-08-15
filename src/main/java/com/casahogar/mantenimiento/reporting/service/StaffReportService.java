package com.casahogar.mantenimiento.reporting.service;

import com.casahogar.mantenimiento.hr.entity.Staff;
import com.casahogar.mantenimiento.hr.repository.StaffRepository;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.Image;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import org.springframework.stereotype.Service;

import java.awt.Color;
import java.io.ByteArrayOutputStream;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class StaffReportService {

    private final StaffRepository staffRepository;

    private static final Font TITLE_FONT = new Font(Font.HELVETICA, 18, Font.BOLD, new Color(30, 41, 59));
    private static final Font SUBTITLE_FONT = new Font(Font.HELVETICA, 13, Font.BOLD, new Color(99, 102, 241));
    private static final Font HEADER_FONT = new Font(Font.HELVETICA, 8, Font.BOLD, Color.WHITE);
    private static final Font CELL_FONT = new Font(Font.HELVETICA, 8, Font.NORMAL, new Color(51, 65, 85));
    private static final Font FOOTER_FONT = new Font(Font.HELVETICA, 8, Font.ITALIC, new Color(148, 163, 184));

    public StaffReportService(StaffRepository staffRepository) {
        this.staffRepository = staffRepository;
    }

    public byte[] generateReport() {
        List<Staff> staffList = staffRepository.findAllActive();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Document document = new Document(PageSize.A4, 36, 36, 36, 36);

        try {
            PdfWriter.getInstance(document, baos);
            document.open();
            addHeader(document);
            addSummarySection(document, staffList);
            addStaffTable(document, staffList);
            addFooter(document);
            document.close();
        } catch (DocumentException e) {
            throw new RuntimeException("Error generando PDF de personal", e);
        }

        return baos.toByteArray();
    }

    private void addHeader(Document document) throws DocumentException {
        Paragraph title = new Paragraph("CASA HOGAR - REPORTE DE PERSONAL", TITLE_FONT);
        title.setAlignment(Element.ALIGN_CENTER);
        title.setSpacingAfter(4);
        document.add(title);

        Paragraph sub = new Paragraph("Generado: " + LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")), FOOTER_FONT);
        sub.setAlignment(Element.ALIGN_CENTER);
        sub.setSpacingAfter(16);
        document.add(sub);
    }

    private void addSummarySection(Document document, List<Staff> staffList) throws DocumentException {
        long active = staffList.stream().filter(s -> s.getStatus() == Staff.StaffStatus.ACTIVE).count();
        long onLeave = staffList.stream().filter(s -> s.getStatus() == Staff.StaffStatus.ON_LEAVE).count();
        long inactive = staffList.stream().filter(s -> s.getStatus() == Staff.StaffStatus.INACTIVE || s.getStatus() == Staff.StaffStatus.TERMINATED).count();

        Paragraph section = new Paragraph("RESUMEN", SUBTITLE_FONT);
        section.setSpacingAfter(8);
        document.add(section);

        PdfPTable table = new PdfPTable(2);
        table.setWidthPercentage(100);
        table.setSpacingAfter(12);
        table.setWidths(new float[]{50, 50});

        addRow(table, "Total de Personal", String.valueOf(staffList.size()), Color.WHITE);
        addRow(table, "Activos", String.valueOf(active), Color.WHITE);
        addRow(table, "En Licencia", String.valueOf(onLeave), Color.WHITE);
        addRow(table, "Inactivos/Terminados", String.valueOf(inactive), Color.WHITE);

        document.add(table);
    }

    private void addStaffTable(Document document, List<Staff> staffList) throws DocumentException {
        Paragraph section = new Paragraph("DETALLE DE PERSONAL", SUBTITLE_FONT);
        section.setSpacingBefore(8);
        section.setSpacingAfter(8);
        document.add(section);

        PdfPTable table = new PdfPTable(7);
        table.setWidthPercentage(100);
        table.setSpacingAfter(12);
        table.setWidths(new float[]{16, 12, 20, 16, 12, 12, 12});

        String[] headers = {"Foto", "Legajo", "Nombre", "Cargo", "Departamento", "Turno", "Estado"};
        Color headerBg = new Color(99, 102, 241);
        for (String h : headers) {
            PdfPCell cell = new PdfPCell(new Phrase(h, HEADER_FONT));
            cell.setBackgroundColor(headerBg);
            cell.setPadding(4);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);
        }

        Color evenBg = new Color(248, 250, 252);
        for (int i = 0; i < staffList.size(); i++) {
            Staff s = staffList.get(i);
            Color bg = (i % 2 == 0) ? evenBg : Color.WHITE;

            addPhotoCell(table, s.getPhotoUrl(), bg);
            addCell(table, s.getEmployeeCode(), bg, CELL_FONT, Element.ALIGN_CENTER);
            addCell(table, (s.getFirstName() != null ? s.getFirstName() : "") + " " + (s.getLastName() != null ? s.getLastName() : ""), bg, CELL_FONT, Element.ALIGN_LEFT);
            addCell(table, s.getPosition() != null ? s.getPosition() : "-", bg, CELL_FONT, Element.ALIGN_LEFT);
            addCell(table, s.getDepartment() != null ? s.getDepartment() : "-", bg, CELL_FONT, Element.ALIGN_CENTER);
            addCell(table, s.getShift() != null ? s.getShift().name() : "-", bg, CELL_FONT, Element.ALIGN_CENTER);
            addCell(table, s.getStatus() != null ? s.getStatus().name() : "-", bg, CELL_FONT, Element.ALIGN_CENTER);
        }

        document.add(table);
    }

    private void addPhotoCell(PdfPTable table, String photoUrl, Color bg) {
        if (photoUrl != null && !photoUrl.isBlank()) {
            try {
                Image img = Image.getInstance(new URL(photoUrl));
                img.scaleToFit(56, 56);
                PdfPCell cell = new PdfPCell(img);
                cell.setBackgroundColor(bg);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                cell.setPadding(2);
                cell.setBorder(0);
                table.addCell(cell);
                return;
            } catch (Exception e) {
            }
        }
        PdfPCell cell = new PdfPCell(new Phrase("-", new Font(Font.HELVETICA, 7, Font.NORMAL, new Color(203, 213, 225))));
        cell.setBackgroundColor(bg);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setPadding(3);
        cell.setBorder(0);
        table.addCell(cell);
    }

    private void addRow(PdfPTable table, String label, String value, Color bg) throws DocumentException {
        PdfPCell labelCell = new PdfPCell(new Phrase(label, new Font(Font.HELVETICA, 9, Font.BOLD, new Color(71, 85, 105))));
        labelCell.setBackgroundColor(bg);
        labelCell.setPadding(3);
        labelCell.setBorder(0);
        table.addCell(labelCell);

        PdfPCell valueCell = new PdfPCell(new Phrase(value, new Font(Font.HELVETICA, 9, Font.NORMAL, new Color(51, 65, 85))));
        valueCell.setBackgroundColor(bg);
        valueCell.setPadding(3);
        valueCell.setBorder(0);
        table.addCell(valueCell);
    }

    private void addCell(PdfPTable table, String text, Color bg, Font font, int alignment) {
        PdfPCell cell = new PdfPCell(new Phrase(text, font));
        cell.setBackgroundColor(bg);
        cell.setPadding(3);
        cell.setHorizontalAlignment(alignment);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setBorder(0);
        table.addCell(cell);
    }

    private void addFooter(Document document) throws DocumentException {
        Paragraph line = new Paragraph("_".repeat(80), new Font(Font.HELVETICA, 6, Font.NORMAL, new Color(203, 213, 225)));
        line.setSpacingBefore(16);
        document.add(line);

        Paragraph footer = new Paragraph("Casa Hogar - Sistema de Personal", FOOTER_FONT);
        footer.setAlignment(Element.ALIGN_CENTER);
        document.add(footer);
    }
}