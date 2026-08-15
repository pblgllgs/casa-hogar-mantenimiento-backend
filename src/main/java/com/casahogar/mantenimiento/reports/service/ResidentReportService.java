package com.casahogar.mantenimiento.reports.service;

import com.casahogar.mantenimiento.clinical.entity.ClinicalRecord;
import com.casahogar.mantenimiento.clinical.repository.ClinicalRecordRepository;
import com.casahogar.mantenimiento.residents.entity.Resident;
import com.casahogar.mantenimiento.residents.repository.ResidentRepository;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.Image;
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
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class ResidentReportService {

    private final ResidentRepository residentRepository;
    private final ClinicalRecordRepository clinicalRecordRepository;

    private static final Font TITLE_FONT = new Font(Font.HELVETICA, 18, Font.BOLD, new Color(30, 41, 59));
    private static final Font SUBTITLE_FONT = new Font(Font.HELVETICA, 14, Font.BOLD, new Color(99, 102, 241));
    private static final Font SECTION_FONT = new Font(Font.HELVETICA, 12, Font.BOLD, new Color(30, 41, 59));
    private static final Font LABEL_FONT = new Font(Font.HELVETICA, 10, Font.BOLD, new Color(71, 85, 105));
    private static final Font VALUE_FONT = new Font(Font.HELVETICA, 10, Font.NORMAL, new Color(51, 65, 85));
    private static final Font TABLE_HEADER_FONT = new Font(Font.HELVETICA, 9, Font.BOLD, Color.WHITE);
    private static final Font TABLE_CELL_FONT = new Font(Font.HELVETICA, 8, Font.NORMAL, new Color(51, 65, 85));
    private static final Font TABLE_CELL_BOLD = new Font(Font.HELVETICA, 8, Font.BOLD, new Color(51, 65, 85));
    private static final Font FOOTER_FONT = new Font(Font.HELVETICA, 8, Font.ITALIC, new Color(148, 163, 184));

    public ResidentReportService(ResidentRepository residentRepository, ClinicalRecordRepository clinicalRecordRepository) {
        this.residentRepository = residentRepository;
        this.clinicalRecordRepository = clinicalRecordRepository;
    }

    public byte[] generateReport(Long residentId) {
        Resident resident = residentRepository.findByIdActive(residentId)
                .orElseThrow(() -> new IllegalArgumentException("Residente no encontrado"));

        List<ClinicalRecord> records = clinicalRecordRepository.findByResidentId(residentId);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Document document = new Document(PageSize.A4, 36, 36, 36, 36);

        try {
            PdfWriter writer = PdfWriter.getInstance(document, baos);
            document.open();

            addHeaderAndTitle(document, resident);
            addResidentData(document, resident);
            document.newPage();
            addClinicalRecords(document, records);
            addFooter(document);

            document.close();
        } catch (DocumentException e) {
            throw new RuntimeException("Error generando PDF", e);
        }

        return baos.toByteArray();
    }

    private void addHeaderAndTitle(Document document, Resident resident) throws DocumentException {
        Paragraph header = new Paragraph("CASA HOGAR - FICHA CLINICA", TITLE_FONT);
        header.setAlignment(Element.ALIGN_CENTER);
        header.setSpacingAfter(4);
        document.add(header);

        Paragraph sub = new Paragraph("Reporte generado: " + java.time.LocalDateTime.now()
                .format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")), FOOTER_FONT);
        sub.setAlignment(Element.ALIGN_CENTER);
        sub.setSpacingAfter(16);
        document.add(sub);

        Paragraph line = new Paragraph("_" .repeat(80), new Font(Font.HELVETICA, 6, Font.NORMAL, new Color(203, 213, 225)));
        line.setSpacingAfter(12);
        document.add(line);
    }

    private void addResidentData(Document document, Resident resident) throws DocumentException {
        Paragraph sectionTitle = new Paragraph("DATOS DEL RESIDENTE", SUBTITLE_FONT);
        sectionTitle.setSpacingAfter(10);
        document.add(sectionTitle);

        PdfPTable mainTable = new PdfPTable(2);
        mainTable.setWidthPercentage(100);
        mainTable.setSpacingAfter(12);
        mainTable.setWidths(new float[]{25, 75});

        if (resident.getPhotoUrl() != null && !resident.getPhotoUrl().isBlank()) {
            try {
                Image photo = Image.getInstance(new URL(resident.getPhotoUrl()));
                photo.scaleToFit(80, 80);
                photo.setAlignment(Element.ALIGN_CENTER);
                PdfPCell photoCell = new PdfPCell(photo);
                photoCell.setBorder(Rectangle.NO_BORDER);
                photoCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                photoCell.setVerticalAlignment(Element.ALIGN_TOP);
                photoCell.setPadding(5);
                mainTable.addCell(photoCell);
            } catch (Exception e) {
                PdfPCell noPhoto = new PdfPCell(new Paragraph("Sin foto", VALUE_FONT));
                noPhoto.setBorder(Rectangle.NO_BORDER);
                noPhoto.setHorizontalAlignment(Element.ALIGN_CENTER);
                noPhoto.setPadding(5);
                mainTable.addCell(noPhoto);
            }
        } else {
            PdfPCell noPhoto = new PdfPCell(new Paragraph("Sin foto", VALUE_FONT));
            noPhoto.setBorder(Rectangle.NO_BORDER);
            noPhoto.setHorizontalAlignment(Element.ALIGN_CENTER);
            noPhoto.setPadding(5);
            mainTable.addCell(noPhoto);
        }

        PdfPTable dataTable = new PdfPTable(2);
        dataTable.setWidthPercentage(100);
        dataTable.setSpacingAfter(6);
        dataTable.setWidths(new float[]{38, 62});

        Color bgEven = new Color(248, 250, 252);
        Color bgOdd = Color.WHITE;

        String[][] data = {
                {"Nombre completo", resident.getFirstName() + " " + resident.getLastName()},
                {"Codigo", resident.getCode()},
                {"Tipo documento", resident.getDocumentType() != null ? resident.getDocumentType().name() : "-"},
                {"Numero documento", resident.getDocumentNumber() != null ? resident.getDocumentNumber() : "-"},
                {"Fecha nacimiento", resident.getBirthDate() != null ? resident.getBirthDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) : "-"},
                {"Edad", resident.getBirthDate() != null ? String.valueOf(java.time.Period.between(resident.getBirthDate(), java.time.LocalDate.now()).getYears()) : "-"},
                {"Genero", resident.getGender() != null ? resident.getGender() : "-"},
                {"Fecha ingreso", resident.getEntryDate() != null ? resident.getEntryDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) : "-"},
                {"Estado", resident.getStatus() != null ? resident.getStatus().name() : "-"},
                {"Habitacion", resident.getRoom() != null ? resident.getRoom().getName() : "-"},
                {"Tutor", resident.getGuardianName() != null ? resident.getGuardianName() : "-"},
                {"Telefono tutor", resident.getGuardianPhone() != null ? resident.getGuardianPhone() : "-"},
                {"Parentesco", resident.getGuardianRelationship() != null ? resident.getGuardianRelationship() : "-"},
        };

        for (int i = 0; i < data.length; i++) {
            Color bg = (i % 2 == 0) ? bgEven : bgOdd;

            PdfPCell labelCell = new PdfPCell(new Phrase(data[i][0], LABEL_FONT));
            labelCell.setBackgroundColor(bg);
            labelCell.setPadding(3);
            labelCell.setBorder(Rectangle.NO_BORDER);
            dataTable.addCell(labelCell);

            PdfPCell valueCell = new PdfPCell(new Phrase(data[i][1], VALUE_FONT));
            valueCell.setBackgroundColor(bg);
            valueCell.setPadding(3);
            valueCell.setBorder(Rectangle.NO_BORDER);
            dataTable.addCell(valueCell);
        }

        PdfPCell dataCell = new PdfPCell(dataTable);
        dataCell.setBorder(Rectangle.NO_BORDER);
        dataCell.setPadding(0);
        mainTable.addCell(dataCell);

        document.add(mainTable);

        if (resident.getMedicalInfo() != null && !resident.getMedicalInfo().isBlank()) {
            Paragraph sectionTitle2 = new Paragraph("INFORMACION MEDICA GENERAL", SUBTITLE_FONT);
            sectionTitle2.setSpacingAfter(6);
            document.add(sectionTitle2);

            Paragraph medicalInfo = new Paragraph(resident.getMedicalInfo(), VALUE_FONT);
            medicalInfo.setSpacingAfter(10);
            document.add(medicalInfo);
        }

        if (resident.getDietaryRestrictions() != null && !resident.getDietaryRestrictions().isBlank()) {
            Paragraph dietTitle = new Paragraph("RESTRICCIONES ALIMENTARIAS", SUBTITLE_FONT);
            dietTitle.setSpacingAfter(6);
            document.add(dietTitle);

            Paragraph dietInfo = new Paragraph(resident.getDietaryRestrictions(), VALUE_FONT);
            dietInfo.setSpacingAfter(10);
            document.add(dietInfo);
        }

        if (resident.getNotes() != null && !resident.getNotes().isBlank()) {
            Paragraph notesTitle = new Paragraph("NOTAS ADICIONALES", SUBTITLE_FONT);
            notesTitle.setSpacingAfter(6);
            document.add(notesTitle);

            Paragraph notesInfo = new Paragraph(resident.getNotes(), VALUE_FONT);
            notesInfo.setSpacingAfter(10);
            document.add(notesInfo);
        }
    }

    private void addClinicalRecords(Document document, List<ClinicalRecord> records) throws DocumentException {
        Paragraph sectionTitle = new Paragraph("HISTORIAL CLINICO", SUBTITLE_FONT);
        sectionTitle.setSpacingAfter(10);
        document.add(sectionTitle);

        if (records.isEmpty()) {
            document.add(new Paragraph("Sin registros clinicos.", VALUE_FONT));
            return;
        }

        Color headerBg = new Color(99, 102, 241);

        PdfPTable table = new PdfPTable(5);
        table.setWidthPercentage(100);
        table.setSpacingAfter(12);
        table.setWidths(new float[]{12, 14, 32, 22, 20});

        String[] headers = {"Fecha", "Tipo", "Descripcion", "Diagnostico", "Medico"};
        for (String h : headers) {
            PdfPCell cell = new PdfPCell(new Phrase(h, TABLE_HEADER_FONT));
            cell.setBackgroundColor(headerBg);
            cell.setPadding(5);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);
        }

        Color evenBg = new Color(248, 250, 252);
        Color oddBg = Color.WHITE;

        for (int i = 0; i < records.size(); i++) {
            ClinicalRecord r = records.get(i);
            Color bg = (i % 2 == 0) ? evenBg : oddBg;

            addCell(table, r.getRecordDate() != null ? r.getRecordDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) : "-", bg, TABLE_CELL_BOLD, Element.ALIGN_CENTER);
            addCell(table, r.getRecordType() != null ? r.getRecordType().name() : "-", bg, TABLE_CELL_BOLD, Element.ALIGN_CENTER);
            addCell(table, r.getDescription() != null ? r.getDescription() : "-", bg, TABLE_CELL_FONT, Element.ALIGN_LEFT);
            addCell(table, r.getDiagnosis() != null ? r.getDiagnosis() : "-", bg, TABLE_CELL_FONT, Element.ALIGN_LEFT);
            addCell(table, r.getDoctorName() != null ? r.getDoctorName() : "-", bg, TABLE_CELL_FONT, Element.ALIGN_CENTER);
        }

        document.add(table);
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
        Paragraph line = new Paragraph("_" .repeat(80), new Font(Font.HELVETICA, 6, Font.NORMAL, new Color(203, 213, 225)));
        line.setSpacingBefore(20);
        document.add(line);

        Paragraph footer = new Paragraph("Casa Hogar - Sistema de Fichas Clinicas", FOOTER_FONT);
        footer.setAlignment(Element.ALIGN_CENTER);
        document.add(footer);
    }
}