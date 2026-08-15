package com.casahogar.mantenimiento.reporting.service;

import com.casahogar.mantenimiento.assets.entity.Asset;
import com.casahogar.mantenimiento.assets.repository.AssetRepository;
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

@Service
public class AssetReportService {

    private final AssetRepository assetRepository;

    private static final Font TITLE_FONT = new Font(Font.HELVETICA, 18, Font.BOLD, new Color(30, 41, 59));
    private static final Font SUBTITLE_FONT = new Font(Font.HELVETICA, 13, Font.BOLD, new Color(99, 102, 241));
    private static final Font HEADER_FONT = new Font(Font.HELVETICA, 8, Font.BOLD, Color.WHITE);
    private static final Font CELL_FONT = new Font(Font.HELVETICA, 8, Font.NORMAL, new Color(51, 65, 85));
    private static final Font SECTION_FONT = new Font(Font.HELVETICA, 10, Font.BOLD, new Color(30, 41, 59));
    private static final Font FOOTER_FONT = new Font(Font.HELVETICA, 8, Font.ITALIC, new Color(148, 163, 184));

    public AssetReportService(AssetRepository assetRepository) {
        this.assetRepository = assetRepository;
    }

    public byte[] generateReport() {
        List<Asset> assets = assetRepository.findAllActive();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Document document = new Document(PageSize.A4, 36, 36, 36, 36);

        try {
            PdfWriter.getInstance(document, baos);
            document.open();
            addHeader(document);
            addSummarySection(document, assets);
            addAssetsTable(document, assets);
            addFooter(document);
            document.close();
        } catch (DocumentException e) {
            throw new RuntimeException("Error generando PDF de activos", e);
        }

        return baos.toByteArray();
    }

    private void addHeader(Document document) throws DocumentException {
        Paragraph title = new Paragraph("CASA HOGAR - REPORTE DE ACTIVOS", TITLE_FONT);
        title.setAlignment(Element.ALIGN_CENTER);
        title.setSpacingAfter(4);
        document.add(title);

        Paragraph sub = new Paragraph("Generado: " + LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")), FOOTER_FONT);
        sub.setAlignment(Element.ALIGN_CENTER);
        sub.setSpacingAfter(16);
        document.add(sub);
    }

    private void addSummarySection(Document document, List<Asset> assets) throws DocumentException {
        long operational = assets.stream().filter(a -> a.getStatus() == Asset.AssetStatus.OPERATIONAL).count();
        long maintenance = assets.stream().filter(a -> a.getStatus() == Asset.AssetStatus.UNDER_MAINTENANCE || a.getStatus() == Asset.AssetStatus.NEEDS_MAINTENANCE).count();
        long outOfService = assets.stream().filter(a -> a.getStatus() == Asset.AssetStatus.OUT_OF_SERVICE || a.getStatus() == Asset.AssetStatus.REPAIR_NEEDED).count();
        long retired = assets.stream().filter(a -> a.getStatus() == Asset.AssetStatus.DECOMMISSIONED || a.getStatus() == Asset.AssetStatus.PENDING_INSPECTION || a.getStatus() == Asset.AssetStatus.RETIRED).count();

        Paragraph section = new Paragraph("RESUMEN", SUBTITLE_FONT);
        section.setSpacingAfter(8);
        document.add(section);

        PdfPTable table = new PdfPTable(2);
        table.setWidthPercentage(100);
        table.setSpacingAfter(12);
        table.setWidths(new float[]{50, 50});

        addRow(table, "Total de Activos", String.valueOf(assets.size()), Color.WHITE);
        addRow(table, "Operativos", String.valueOf(operational), Color.WHITE);
        addRow(table, "En Mantenimiento", String.valueOf(maintenance), Color.WHITE);
        addRow(table, "Fuera de Servicio", String.valueOf(outOfService), Color.WHITE);
        addRow(table, "Retirados/Pendientes", String.valueOf(retired), Color.WHITE);

        document.add(table);
    }

    private void addAssetsTable(Document document, List<Asset> assets) throws DocumentException {
        Paragraph section = new Paragraph("DETALLE DE ACTIVOS", SUBTITLE_FONT);
        section.setSpacingBefore(8);
        section.setSpacingAfter(8);
        document.add(section);

        PdfPTable table = new PdfPTable(6);
        table.setWidthPercentage(100);
        table.setSpacingAfter(12);
        table.setWidths(new float[]{14, 24, 18, 14, 14, 16});

        String[] headers = {"Codigo", "Nombre", "Categoria", "Estado", "Ubicacion", "Costo"};
        Color headerBg = new Color(99, 102, 241);
        for (String h : headers) {
            PdfPCell cell = new PdfPCell(new Phrase(h, HEADER_FONT));
            cell.setBackgroundColor(headerBg);
            cell.setPadding(4);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);
        }

        Color evenBg = new Color(248, 250, 252);
        for (int i = 0; i < assets.size(); i++) {
            Asset a = assets.get(i);
            Color bg = (i % 2 == 0) ? evenBg : Color.WHITE;

            addCell(table, a.getAssetCode(), bg, CELL_FONT, Element.ALIGN_CENTER);
            addCell(table, a.getName(), bg, CELL_FONT, Element.ALIGN_LEFT);
            addCell(table, a.getCategory() != null ? a.getCategory().name() : "-", bg, CELL_FONT, Element.ALIGN_CENTER);
            addCell(table, a.getStatus() != null ? a.getStatus().name() : "-", bg, CELL_FONT, Element.ALIGN_CENTER);
            addCell(table, a.getLocation() != null ? a.getLocation().getName() : "-", bg, CELL_FONT, Element.ALIGN_CENTER);
            addCell(table, a.getPurchaseCost() != null ? a.getPurchaseCost().toString() : "-", bg, CELL_FONT, Element.ALIGN_RIGHT);
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

        Paragraph footer = new Paragraph("Casa Hogar - Sistema de Gestion de Activos", FOOTER_FONT);
        footer.setAlignment(Element.ALIGN_CENTER);
        document.add(footer);
    }
}