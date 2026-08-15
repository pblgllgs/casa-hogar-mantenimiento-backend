package com.casahogar.mantenimiento.medications.service;

import com.casahogar.mantenimiento.medications.entity.Medication;
import com.casahogar.mantenimiento.medications.repository.MedicationRepository;
import com.casahogar.mantenimiento.residents.entity.Resident;
import com.casahogar.mantenimiento.residents.repository.ResidentRepository;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import org.springframework.stereotype.Service;

import java.awt.Color;
import java.io.ByteArrayOutputStream;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class MedicationReportService {

    private final MedicationRepository medicationRepository;
    private final ResidentRepository residentRepository;

    private static final Font TITLE_FONT = new Font(Font.HELVETICA, 18, Font.BOLD, new Color(30, 41, 59));
    private static final Font SUBTITLE_FONT = new Font(Font.HELVETICA, 14, Font.BOLD, new Color(99, 102, 241));
    private static final Font SECTION_FONT = new Font(Font.HELVETICA, 12, Font.BOLD, new Color(30, 41, 59));
    private static final Font LABEL_FONT = new Font(Font.HELVETICA, 10, Font.BOLD, new Color(71, 85, 105));
    private static final Font VALUE_FONT = new Font(Font.HELVETICA, 10, Font.NORMAL, new Color(51, 65, 85));
    private static final Font TABLE_HEADER_FONT = new Font(Font.HELVETICA, 9, Font.BOLD, Color.WHITE);
    private static final Font TABLE_CELL_FONT = new Font(Font.HELVETICA, 8, Font.NORMAL, new Color(51, 65, 85));
    private static final Font TABLE_CELL_BOLD = new Font(Font.HELVETICA, 8, Font.BOLD, new Color(51, 65, 85));
    private static final Font FOOTER_FONT = new Font(Font.HELVETICA, 8, Font.ITALIC, new Color(148, 163, 184));

    public MedicationReportService(MedicationRepository medicationRepository, ResidentRepository residentRepository) {
        this.medicationRepository = medicationRepository;
        this.residentRepository = residentRepository;
    }

    public byte[] generateReport(Long residentId) {
        Resident resident = residentRepository.findByIdActive(residentId)
                .orElseThrow(() -> new IllegalArgumentException("Residente no encontrado"));

        List<Medication> medications = medicationRepository.findByResidentIdAndDeletedFalseOrderByIdDesc(residentId);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Document document = new Document(PageSize.A4, 36, 36, 36, 36);

        try {
            PdfWriter.getInstance(document, baos);
            document.open();

            addHeaderAndTitle(document, resident);
            addPersonalData(document, resident);
            addMedicalInfo(document, resident);
            addGuardianInfo(document, resident);
            addMedications(document, medications);
            addFooter(document);

            document.close();
        } catch (DocumentException e) {
            throw new RuntimeException("Error generando PDF", e);
        }

        return baos.toByteArray();
    }

    private void addHeaderAndTitle(Document document, Resident resident) throws DocumentException {
        Paragraph header = new Paragraph("CASA HOGAR - MEDICAMENTOS PRESCRITOS", TITLE_FONT);
        header.setAlignment(Element.ALIGN_CENTER);
        header.setSpacingAfter(4);
        document.add(header);

        Paragraph sub = new Paragraph("Residente: " + resident.getFirstName() + " " + resident.getLastName()
                + "  |  Reporte: " + java.time.LocalDateTime.now()
                .format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")), FOOTER_FONT);
        sub.setAlignment(Element.ALIGN_CENTER);
        sub.setSpacingAfter(16);
        document.add(sub);

        Paragraph line = new Paragraph("_".repeat(80), new Font(Font.HELVETICA, 6, Font.NORMAL, new Color(203, 213, 225)));
        line.setSpacingAfter(12);
        document.add(line);
    }

    private void addPersonalData(Document document, Resident resident) throws DocumentException {
        Paragraph sectionTitle = new Paragraph("DATOS PERSONALES", SUBTITLE_FONT);
        sectionTitle.setSpacingAfter(10);
        document.add(sectionTitle);

        PdfPTable table = new PdfPTable(2);
        table.setWidthPercentage(100);
        table.setSpacingAfter(12);
        table.setWidths(new float[]{35, 65});

        Color bgEven = new Color(248, 250, 252);
        Color bgOdd = Color.WHITE;

        String age = resident.getBirthDate() != null
                ? Period.between(resident.getBirthDate(), java.time.LocalDate.now()).getYears() + " años" : "-";

        String docType = resident.getDocumentType() != null ? resident.getDocumentType().name() : "-";

        String status = resident.getStatus() != null ? formatResidentStatus(resident.getStatus().name()) : "-";

        String gender = resident.getGender() != null ? formatGender(resident.getGender()) : "-";

        String room = resident.getRoom() != null ? resident.getRoom().getName() : "-";

        String[][] data = {
                {"Nombre completo", resident.getFirstName() + " " + resident.getLastName()},
                {"Código", resident.getCode()},
                {"Tipo de documento", docType},
                {"Número de documento", resident.getDocumentNumber() != null ? resident.getDocumentNumber() : "-"},
                {"Fecha de nacimiento", resident.getBirthDate() != null
                        ? resident.getBirthDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) : "-"},
                {"Edad", age},
                {"Género", gender},
                {"Fecha de ingreso", resident.getEntryDate() != null
                        ? resident.getEntryDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) : "-"},
                {"Estado", status},
                {"Habitación", room},
        };

        for (int i = 0; i < data.length; i++) {
            Color bg = (i % 2 == 0) ? bgEven : bgOdd;

            PdfPCell labelCell = new PdfPCell(new Phrase(data[i][0], LABEL_FONT));
            labelCell.setBackgroundColor(bg);
            labelCell.setPadding(5);
            labelCell.setBorder(Rectangle.NO_BORDER);
            table.addCell(labelCell);

            PdfPCell valueCell = new PdfPCell(new Phrase(data[i][1], VALUE_FONT));
            valueCell.setBackgroundColor(bg);
            valueCell.setPadding(5);
            valueCell.setBorder(Rectangle.NO_BORDER);
            table.addCell(valueCell);
        }

        document.add(table);
    }

    private void addMedicalInfo(Document document, Resident resident) throws DocumentException {
        String medicalInfo = resident.getMedicalInfo() != null ? resident.getMedicalInfo() : "";
        String dietary = resident.getDietaryRestrictions() != null ? resident.getDietaryRestrictions() : "";
        String notes = resident.getNotes() != null ? resident.getNotes() : "";

        if (medicalInfo.isEmpty() && dietary.isEmpty() && notes.isEmpty()) {
            return;
        }

        Paragraph sectionTitle = new Paragraph("INFORMACIÓN MÉDICA", SUBTITLE_FONT);
        sectionTitle.setSpacingAfter(10);
        sectionTitle.setSpacingBefore(8);
        document.add(sectionTitle);

        PdfPTable table = new PdfPTable(2);
        table.setWidthPercentage(100);
        table.setSpacingAfter(12);
        table.setWidths(new float[]{35, 65});

        Color bgEven = new Color(248, 250, 252);
        Color bgOdd = Color.WHITE;

        java.util.List<String[]> rows = new java.util.ArrayList<>();
        if (!medicalInfo.isEmpty()) rows.add(new String[]{"Información médica", medicalInfo});
        if (!dietary.isEmpty()) rows.add(new String[]{"Restricciones alimentarias", dietary});
        if (!notes.isEmpty()) rows.add(new String[]{"Notas generales", notes});

        for (int i = 0; i < rows.size(); i++) {
            Color bg = (i % 2 == 0) ? bgEven : bgOdd;
            String[] row = rows.get(i);

            PdfPCell labelCell = new PdfPCell(new Phrase(row[0], LABEL_FONT));
            labelCell.setBackgroundColor(bg);
            labelCell.setPadding(5);
            labelCell.setBorder(Rectangle.NO_BORDER);
            table.addCell(labelCell);

            PdfPCell valueCell = new PdfPCell(new Phrase(row[1], VALUE_FONT));
            valueCell.setBackgroundColor(bg);
            valueCell.setPadding(5);
            valueCell.setBorder(Rectangle.NO_BORDER);
            table.addCell(valueCell);
        }

        document.add(table);
    }

    private void addGuardianInfo(Document document, Resident resident) throws DocumentException {
        String guardianName = resident.getGuardianName() != null ? resident.getGuardianName() : "";
        String guardianPhone = resident.getGuardianPhone() != null ? resident.getGuardianPhone() : "";
        String guardianEmail = resident.getGuardianEmail() != null ? resident.getGuardianEmail() : "";
        String guardianRelationship = resident.getGuardianRelationship() != null ? resident.getGuardianRelationship() : "";

        if (guardianName.isEmpty() && guardianPhone.isEmpty() && guardianEmail.isEmpty() && guardianRelationship.isEmpty()) {
            return;
        }

        Paragraph sectionTitle = new Paragraph("CONTACTO DEL APODERADO", SUBTITLE_FONT);
        sectionTitle.setSpacingAfter(10);
        sectionTitle.setSpacingBefore(8);
        document.add(sectionTitle);

        PdfPTable table = new PdfPTable(2);
        table.setWidthPercentage(100);
        table.setSpacingAfter(12);
        table.setWidths(new float[]{35, 65});

        Color bgEven = new Color(248, 250, 252);
        Color bgOdd = Color.WHITE;

        String[][] data = {
                {"Nombre", guardianName.isEmpty() ? "-" : guardianName},
                {"Parentesco", guardianRelationship.isEmpty() ? "-" : guardianRelationship},
                {"Teléfono", guardianPhone.isEmpty() ? "-" : guardianPhone},
                {"Correo electrónico", guardianEmail.isEmpty() ? "-" : guardianEmail},
        };

        for (int i = 0; i < data.length; i++) {
            Color bg = (i % 2 == 0) ? bgEven : bgOdd;

            PdfPCell labelCell = new PdfPCell(new Phrase(data[i][0], LABEL_FONT));
            labelCell.setBackgroundColor(bg);
            labelCell.setPadding(5);
            labelCell.setBorder(Rectangle.NO_BORDER);
            table.addCell(labelCell);

            PdfPCell valueCell = new PdfPCell(new Phrase(data[i][1], VALUE_FONT));
            valueCell.setBackgroundColor(bg);
            valueCell.setPadding(5);
            valueCell.setBorder(Rectangle.NO_BORDER);
            table.addCell(valueCell);
        }

        document.add(table);
    }

    private void addMedications(Document document, List<Medication> medications) throws DocumentException {
        Paragraph sectionTitle = new Paragraph("MEDICAMENTOS PRESCRITOS", SUBTITLE_FONT);
        sectionTitle.setSpacingAfter(10);
        sectionTitle.setSpacingBefore(8);
        document.add(sectionTitle);

        if (medications.isEmpty()) {
            document.add(new Paragraph("Sin medicamentos registrados.", VALUE_FONT));
            return;
        }

        Color headerBg = new Color(99, 102, 241);

        PdfPTable table = new PdfPTable(9);
        table.setWidthPercentage(100);
        table.setSpacingAfter(12);
        table.setWidths(new float[]{18, 8, 10, 10, 10, 10, 14, 12, 8});

        String[] headers = {"Medicamento", "Dosis", "Frecuencia", "Vía", "Inicio", "Fin", "Indicaciones", "Prescrito por", "Estado"};
        for (String h : headers) {
            PdfPCell cell = new PdfPCell(new Phrase(h, TABLE_HEADER_FONT));
            cell.setBackgroundColor(headerBg);
            cell.setPadding(5);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);
        }

        Color evenBg = new Color(248, 250, 252);
        Color oddBg = Color.WHITE;

        for (int i = 0; i < medications.size(); i++) {
            Medication m = medications.get(i);
            Color bg = (i % 2 == 0) ? evenBg : oddBg;

            addCell(table, m.getMedicationName(), bg, TABLE_CELL_BOLD, Element.ALIGN_LEFT);
            addCell(table, m.getDosage(), bg, TABLE_CELL_FONT, Element.ALIGN_CENTER);
            addCell(table, m.getFrequencyHours() + "h", bg, TABLE_CELL_FONT, Element.ALIGN_CENTER);
            addCell(table, formatRoute(m.getAdministrationRoute()), bg, TABLE_CELL_FONT, Element.ALIGN_CENTER);
            addCell(table, m.getStartDate() != null
                    ? m.getStartDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) : "-", bg, TABLE_CELL_FONT, Element.ALIGN_CENTER);
            addCell(table, m.getEndDate() != null
                    ? m.getEndDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) : "-", bg, TABLE_CELL_FONT, Element.ALIGN_CENTER);
            addCell(table, m.getInstructions() != null ? m.getInstructions() : "-", bg, TABLE_CELL_FONT, Element.ALIGN_LEFT);
            addCell(table, m.getPrescribedBy() != null ? m.getPrescribedBy() : "-", bg, TABLE_CELL_FONT, Element.ALIGN_LEFT);
            addCell(table, formatStatus(m.getStatus()), bg, TABLE_CELL_BOLD, Element.ALIGN_CENTER);
        }

        document.add(table);
    }

    private String formatRoute(String route) {
        if (route == null) return "-";
        return switch (route) {
            case "ORAL" -> "Oral";
            case "INTRAVENOUS" -> "Intravenosa";
            case "INTRAMUSCULAR" -> "Intramuscular";
            case "TOPICAL" -> "Tópica";
            case "SUBLINGUAL" -> "Sublingual";
            default -> route;
        };
    }

    private String formatGender(String gender) {
        if (gender == null) return "-";
        return switch (gender.toUpperCase()) {
            case "MALE" -> "Masculino";
            case "FEMALE" -> "Femenino";
            default -> gender;
        };
    }

    private String formatStatus(String status) {
        if (status == null) return "-";
        return switch (status) {
            case "ACTIVE" -> "Activo";
            case "SUSPENDED" -> "Suspendido";
            case "COMPLETED" -> "Completado";
            default -> status;
        };
    }

    private String formatResidentStatus(String status) {
        if (status == null) return "-";
        return switch (status) {
            case "ACTIVE" -> "Activo";
            case "INACTIVE" -> "Inactivo";
            case "TRANSFERRED" -> "Transferido";
            case "DISCHARGED" -> "Dado de alta";
            default -> status;
        };
    }

    private void addCell(PdfPTable table, String text, Color bg, Font font, int alignment) {
        PdfPCell cell = new PdfPCell(new Phrase(text, font));
        cell.setBackgroundColor(bg);
        cell.setPadding(4);
        cell.setHorizontalAlignment(alignment);
        cell.setBorder(Rectangle.NO_BORDER);
        table.addCell(cell);
    }

    private void addFooter(Document document) throws DocumentException {
        Paragraph line = new Paragraph("_".repeat(80), new Font(Font.HELVETICA, 6, Font.NORMAL, new Color(203, 213, 225)));
        line.setSpacingBefore(20);
        document.add(line);

        Paragraph footer = new Paragraph("Casa Hogar - Sistema de Medicamentos", FOOTER_FONT);
        footer.setAlignment(Element.ALIGN_CENTER);
        document.add(footer);
    }
}
