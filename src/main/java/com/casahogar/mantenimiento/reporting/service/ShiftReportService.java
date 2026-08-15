package com.casahogar.mantenimiento.reporting.service;

import com.casahogar.mantenimiento.hr.entity.Shift;
import com.casahogar.mantenimiento.hr.entity.StaffShift;
import com.casahogar.mantenimiento.hr.repository.ShiftRepository;
import com.casahogar.mantenimiento.hr.repository.StaffShiftRepository;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import org.springframework.stereotype.Service;

import java.awt.Color;
import java.io.ByteArrayOutputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ShiftReportService {

    private final ShiftRepository shiftRepository;
    private final StaffShiftRepository staffShiftRepository;

    private static final Font TITLE_FONT = new Font(Font.HELVETICA, 18, Font.BOLD, new Color(30, 41, 59));
    private static final Font SUBTITLE_FONT = new Font(Font.HELVETICA, 13, Font.BOLD, new Color(99, 102, 241));
    private static final Font HEADER_FONT = new Font(Font.HELVETICA, 8, Font.BOLD, Color.WHITE);
    private static final Font CELL_FONT = new Font(Font.HELVETICA, 8, Font.NORMAL, new Color(51, 65, 85));
    private static final Font FOOTER_FONT = new Font(Font.HELVETICA, 8, Font.ITALIC, new Color(148, 163, 184));

    public ShiftReportService(ShiftRepository shiftRepository, StaffShiftRepository staffShiftRepository) {
        this.shiftRepository = shiftRepository;
        this.staffShiftRepository = staffShiftRepository;
    }

    public byte[] generateReport() {
        List<Shift> shifts = shiftRepository.findAll();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Document document = new Document(PageSize.A4, 36, 36, 36, 36);

        try {
            PdfWriter.getInstance(document, baos);
            document.open();
            addHeader(document);
            addSummarySection(document, shifts);
            addShiftsTable(document, shifts);
            addFooter(document);
            document.close();
        } catch (DocumentException e) {
            throw new RuntimeException("Error generando PDF de turnos", e);
        }

        return baos.toByteArray();
    }

    private void addHeader(Document document) throws DocumentException {
        Paragraph title = new Paragraph("CASA HOGAR - REPORTE DE TURNOS SEMANALES", TITLE_FONT);
        title.setAlignment(Element.ALIGN_CENTER);
        title.setSpacingAfter(4);
        document.add(title);

        Paragraph sub = new Paragraph("Generado: " + LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")), FOOTER_FONT);
        sub.setAlignment(Element.ALIGN_CENTER);
        sub.setSpacingAfter(16);
        document.add(sub);
    }

    private void addSummarySection(Document document, List<Shift> shifts) throws DocumentException {
        long activeCount = shifts.stream().filter(Shift::getIsActive).count();
        long inactiveCount = shifts.size() - activeCount;

        Paragraph section = new Paragraph("RESUMEN", SUBTITLE_FONT);
        section.setSpacingAfter(8);
        document.add(section);

        PdfPTable table = new PdfPTable(2);
        table.setWidthPercentage(100);
        table.setSpacingAfter(12);
        table.setWidths(new float[]{50, 50});

        addRow(table, "Total de Turnos", String.valueOf(shifts.size()), Color.WHITE);
        addRow(table, "Activos", String.valueOf(activeCount), Color.WHITE);
        addRow(table, "Inactivos", String.valueOf(inactiveCount), Color.WHITE);

        document.add(table);
    }

    private void addShiftsTable(Document document, List<Shift> shifts) throws DocumentException {
        Paragraph section = new Paragraph("DETALLE DE TURNOS", SUBTITLE_FONT);
        section.setSpacingBefore(8);
        section.setSpacingAfter(6);
        document.add(section);

        PdfPTable table = new PdfPTable(6);
        table.setWidthPercentage(100);
        table.setSpacingAfter(12);
        table.setWidths(new float[]{6, 18, 10, 22, 16, 28});

        String[] headers = {"#", "Nombre", "Estado", "Horario", "Dias", "Personal"};
        Color headerBg = new Color(99, 102, 241);
        for (String h : headers) {
            PdfPCell cell = new PdfPCell(new Phrase(h, HEADER_FONT));
            cell.setBackgroundColor(headerBg);
            cell.setPadding(3);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);
        }

        Color evenBg = new Color(248, 250, 252);
        for (int i = 0; i < shifts.size(); i++) {
            Shift s = shifts.get(i);
            Color bg = (i % 2 == 0) ? evenBg : Color.WHITE;

            addCell(table, String.valueOf(i + 1), bg, CELL_FONT, Element.ALIGN_CENTER);
            addCell(table, s.getName() != null ? s.getName() : "-", bg, CELL_FONT, Element.ALIGN_LEFT);
            addCell(table, s.getIsActive() != null && s.getIsActive() ? "Activo" : "Inactivo", bg, CELL_FONT, Element.ALIGN_CENTER);
            addCell(table, (s.getStartTime() != null ? s.getStartTime().toString() : "-") + " - " + (s.getEndTime() != null ? s.getEndTime().toString() : "-"), bg, CELL_FONT, Element.ALIGN_CENTER);
            addCell(table, s.getDaysOfWeek() != null ? s.getDaysOfWeek() : "-", bg, CELL_FONT, Element.ALIGN_CENTER);

            List<StaffShift> assignments = staffShiftRepository.findActiveByShiftId(s.getId());
            String staffText;
            if (assignments.isEmpty()) {
                staffText = "Sin personal";
            } else {
                staffText = assignments.stream()
                    .map(a -> a.getStaff().getEmployeeCode() + " " + a.getStaff().getFirstName() + " " + a.getStaff().getLastName())
                    .collect(Collectors.joining(", "));
            }
            addCell(table, staffText, bg, CELL_FONT, Element.ALIGN_LEFT);
        }

        document.add(table);
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
        cell.setBorder(0);
        table.addCell(cell);
    }

    private void addFooter(Document document) throws DocumentException {
        Paragraph line = new Paragraph("_".repeat(80), new Font(Font.HELVETICA, 6, Font.NORMAL, new Color(203, 213, 225)));
        line.setSpacingBefore(16);
        document.add(line);

        Paragraph footer = new Paragraph("Casa Hogar - Sistema de Turnos", FOOTER_FONT);
        footer.setAlignment(Element.ALIGN_CENTER);
        document.add(footer);
    }
}