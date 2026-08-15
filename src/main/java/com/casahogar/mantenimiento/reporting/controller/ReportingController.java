package com.casahogar.mantenimiento.reporting.controller;

import com.casahogar.mantenimiento.assets.entity.Asset;
import com.casahogar.mantenimiento.assets.repository.AssetRepository;
import com.casahogar.mantenimiento.common.dto.ApiResponse;
import com.casahogar.mantenimiento.hr.repository.ShiftRepository;
import com.casahogar.mantenimiento.hr.repository.StaffRepository;
import com.casahogar.mantenimiento.inventory.entity.InventoryItem;
import com.casahogar.mantenimiento.inventory.repository.InventoryItemRepository;
import com.casahogar.mantenimiento.maintenance.entity.WorkOrder;
import com.casahogar.mantenimiento.maintenance.repository.WorkOrderRepository;
import com.casahogar.mantenimiento.reporting.service.AssetReportService;
import com.casahogar.mantenimiento.reporting.service.InventoryReportService;
import com.casahogar.mantenimiento.reporting.service.ShiftReportService;
import com.casahogar.mantenimiento.reporting.service.StaffReportService;
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
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.awt.Color;
import java.io.ByteArrayOutputStream;
import java.net.URL;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/reports")
public class ReportingController {

    private static final Font TITLE_FONT = new Font(Font.HELVETICA, 18, Font.BOLD, new Color(30, 41, 59));
    private static final Font SUBTITLE_FONT = new Font(Font.HELVETICA, 13, Font.BOLD, new Color(99, 102, 241));
    private static final Font HEADER_FONT = new Font(Font.HELVETICA, 8, Font.BOLD, Color.WHITE);
    private static final Font CELL_FONT = new Font(Font.HELVETICA, 8, Font.NORMAL, new Color(51, 65, 85));
    private static final Font FOOTER_FONT = new Font(Font.HELVETICA, 8, Font.ITALIC, new Color(148, 163, 184));

    private final WorkOrderRepository workOrderRepository;
    private final AssetRepository assetRepository;
    private final InventoryItemRepository inventoryItemRepository;
    private final ResidentRepository residentRepository;
    private final StaffRepository staffRepository;
    private final ShiftRepository shiftRepository;
    private final AssetReportService assetReportService;
    private final InventoryReportService inventoryReportService;
    private final StaffReportService staffReportService;
    private final ShiftReportService shiftReportService;

    public ReportingController(WorkOrderRepository workOrderRepository,
                               AssetRepository assetRepository,
                               InventoryItemRepository inventoryItemRepository,
                               ResidentRepository residentRepository,
                               StaffRepository staffRepository,
                               ShiftRepository shiftRepository,
                               AssetReportService assetReportService,
                               InventoryReportService inventoryReportService,
                               StaffReportService staffReportService,
                               ShiftReportService shiftReportService) {
        this.workOrderRepository = workOrderRepository;
        this.assetRepository = assetRepository;
        this.inventoryItemRepository = inventoryItemRepository;
        this.residentRepository = residentRepository;
        this.staffRepository = staffRepository;
        this.shiftRepository = shiftRepository;
        this.assetReportService = assetReportService;
        this.inventoryReportService = inventoryReportService;
        this.staffReportService = staffReportService;
        this.shiftReportService = shiftReportService;
    }

    @GetMapping("/dashboard")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getDashboard() {
        Map<String, Object> dashboard = new HashMap<>();
        dashboard.put("totalWorkOrders", workOrderRepository.count());
        dashboard.put("totalAssets", assetRepository.count());
        dashboard.put("totalResidents", residentRepository.findAllActive().size());
        dashboard.put("totalStaff", staffRepository.findAllActive().size());
        dashboard.put("pendingWorkOrders", workOrderRepository.countByStatusAndDeletedFalse(WorkOrder.WorkOrderStatus.PENDING));
        dashboard.put("inProgressWorkOrders", workOrderRepository.countByStatusAndDeletedFalse(WorkOrder.WorkOrderStatus.IN_PROGRESS));
        dashboard.put("completedWorkOrders", workOrderRepository.countByStatusAndDeletedFalse(WorkOrder.WorkOrderStatus.COMPLETED));
        dashboard.put("operationalAssets", assetRepository.countByStatusAndDeletedFalse(Asset.AssetStatus.OPERATIONAL));
        dashboard.put("maintenanceNeededAssets", assetRepository.findDueForMaintenance(java.time.LocalDate.now()).size());
        dashboard.put("lowStockItems", inventoryItemRepository.findBelowReorderPoint().size());
        return ResponseEntity.ok(ApiResponse.ok(dashboard));
    }

    @GetMapping("/maintenance-summary")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getMaintenanceSummary() {
        Map<String, Object> summary = new HashMap<>();
        summary.put("pending", workOrderRepository.countByStatusAndDeletedFalse(WorkOrder.WorkOrderStatus.PENDING));
        summary.put("assigned", workOrderRepository.countByStatusAndDeletedFalse(WorkOrder.WorkOrderStatus.ASSIGNED));
        summary.put("inProgress", workOrderRepository.countByStatusAndDeletedFalse(WorkOrder.WorkOrderStatus.IN_PROGRESS));
        summary.put("onHold", workOrderRepository.countByStatusAndDeletedFalse(WorkOrder.WorkOrderStatus.ON_HOLD));
        summary.put("pendingReview", workOrderRepository.countByStatusAndDeletedFalse(WorkOrder.WorkOrderStatus.PENDING_REVIEW));
        summary.put("completed", workOrderRepository.countByStatusAndDeletedFalse(WorkOrder.WorkOrderStatus.COMPLETED));
        summary.put("cancelled", workOrderRepository.countByStatusAndDeletedFalse(WorkOrder.WorkOrderStatus.CANCELLED));
        summary.put("reopened", workOrderRepository.countByStatusAndDeletedFalse(WorkOrder.WorkOrderStatus.REOPENED));
        return ResponseEntity.ok(ApiResponse.ok(summary));
    }

    @GetMapping("/inventory-alerts")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getInventoryAlerts() {
        Map<String, Object> alerts = new HashMap<>();
        List<InventoryItem> belowReorder = inventoryItemRepository.findBelowReorderPoint();
        List<InventoryItem> belowMinimum = inventoryItemRepository.findBelowMinimumStock();
        alerts.put("belowReorderPoint", belowReorder.size());
        alerts.put("belowMinimumStock", belowMinimum.size());
        alerts.put("itemsBelowReorder", belowReorder.stream()
                .map(item -> {
                    Map<String, Object> itemMap = new HashMap<>();
                    itemMap.put("id", item.getId());
                    itemMap.put("code", item.getCode());
                    itemMap.put("name", item.getName());
                    itemMap.put("currentStock", item.getCurrentStock());
                    itemMap.put("reorderPoint", item.getReorderPoint());
                    return itemMap;
                })
                .collect(Collectors.toList()));
        return ResponseEntity.ok(ApiResponse.ok(alerts));
    }

    @GetMapping("/work-orders-by-status")
    public ResponseEntity<ApiResponse<Map<String, Long>>> getWorkOrdersByStatus() {
        Map<String, Long> byStatus = new HashMap<>();
        byStatus.put("PENDING", workOrderRepository.countByStatusAndDeletedFalse(WorkOrder.WorkOrderStatus.PENDING));
        byStatus.put("ASSIGNED", workOrderRepository.countByStatusAndDeletedFalse(WorkOrder.WorkOrderStatus.ASSIGNED));
        byStatus.put("IN_PROGRESS", workOrderRepository.countByStatusAndDeletedFalse(WorkOrder.WorkOrderStatus.IN_PROGRESS));
        byStatus.put("ON_HOLD", workOrderRepository.countByStatusAndDeletedFalse(WorkOrder.WorkOrderStatus.ON_HOLD));
        byStatus.put("PENDING_REVIEW", workOrderRepository.countByStatusAndDeletedFalse(WorkOrder.WorkOrderStatus.PENDING_REVIEW));
        byStatus.put("COMPLETED", workOrderRepository.countByStatusAndDeletedFalse(WorkOrder.WorkOrderStatus.COMPLETED));
        byStatus.put("CANCELLED", workOrderRepository.countByStatusAndDeletedFalse(WorkOrder.WorkOrderStatus.CANCELLED));
        byStatus.put("REOPENED", workOrderRepository.countByStatusAndDeletedFalse(WorkOrder.WorkOrderStatus.REOPENED));
        return ResponseEntity.ok(ApiResponse.ok(byStatus));
    }

    @GetMapping("/work-orders-by-priority")
    public ResponseEntity<ApiResponse<Map<String, Long>>> getWorkOrdersByPriority() {
        Map<String, Long> byPriority = new HashMap<>();
        byPriority.put("LOW", workOrderRepository.countByPriorityAndDeletedFalse(WorkOrder.Priority.LOW));
        byPriority.put("MEDIUM", workOrderRepository.countByPriorityAndDeletedFalse(WorkOrder.Priority.MEDIUM));
        byPriority.put("HIGH", workOrderRepository.countByPriorityAndDeletedFalse(WorkOrder.Priority.HIGH));
        byPriority.put("URGENT", workOrderRepository.countByPriorityAndDeletedFalse(WorkOrder.Priority.URGENT));
        byPriority.put("CRITICAL", workOrderRepository.countByPriorityAndDeletedFalse(WorkOrder.Priority.CRITICAL));
        return ResponseEntity.ok(ApiResponse.ok(byPriority));
    }

    @GetMapping("/assets-pdf")
    public ResponseEntity<byte[]> getAssetsPdf() {
        byte[] pdf = assetReportService.generateReport();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData("filename", "reporte-activos.pdf");
        return ResponseEntity.ok().headers(headers).body(pdf);
    }

    @GetMapping("/inventory-pdf")
    public ResponseEntity<byte[]> getInventoryPdf() {
        byte[] pdf = inventoryReportService.generateReport();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData("filename", "reporte-inventario.pdf");
        return ResponseEntity.ok().headers(headers).body(pdf);
    }

    @GetMapping("/residents-pdf")
    public ResponseEntity<byte[]> getResidentsPdf() {
        List<Resident> residents = residentRepository.findAllActive();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Document document = new Document(PageSize.A4, 36, 36, 36, 36);

        try {
            PdfWriter.getInstance(document, baos);
            document.open();
            addResidentsHeader(document);
            addResidentsTable(document, residents);
            addResidentsFooter(document);
            document.close();
        } catch (DocumentException e) {
            throw new RuntimeException("Error generando PDF de residentes", e);
        }

        byte[] pdf = baos.toByteArray();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData("filename", "reporte-residentes.pdf");
        return ResponseEntity.ok().headers(headers).body(pdf);
    }

    @GetMapping("/staff-pdf")
    public ResponseEntity<byte[]> getStaffPdf() {
        byte[] pdf = staffReportService.generateReport();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData("filename", "reporte-personal.pdf");
        return ResponseEntity.ok().headers(headers).body(pdf);
    }

    @GetMapping("/shifts-pdf")
    public ResponseEntity<byte[]> getShiftsPdf() {
        byte[] pdf = shiftReportService.generateReport();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData("filename", "reporte-turnos.pdf");
        return ResponseEntity.ok().headers(headers).body(pdf);
    }

    private void addResidentsHeader(Document document) throws DocumentException {
        Paragraph title = new Paragraph("CASA HOGAR - REPORTE DE RESIDENTES", TITLE_FONT);
        title.setAlignment(Element.ALIGN_CENTER);
        title.setSpacingAfter(4);
        document.add(title);

        Paragraph sub = new Paragraph("Generado: " + java.time.LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")), FOOTER_FONT);
        sub.setAlignment(Element.ALIGN_CENTER);
        sub.setSpacingAfter(16);
        document.add(sub);
    }

    private void addResidentsTable(Document document, List<Resident> residents) throws DocumentException {
        PdfPTable table = new PdfPTable(6);
        table.setWidthPercentage(100);
        table.setSpacingAfter(12);
        table.setWidths(new float[]{16, 14, 22, 14, 16, 18});

        String[] headers = {"Foto", "Codigo", "Nombre", "Documento", "Habitacion", "Estado"};
        Color headerBg = new Color(99, 102, 241);
        for (String h : headers) {
            PdfPCell cell = new PdfPCell(new Phrase(h, HEADER_FONT));
            cell.setBackgroundColor(headerBg);
            cell.setPadding(4);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);
        }

        Color evenBg = new Color(248, 250, 252);
        for (int i = 0; i < residents.size(); i++) {
            Resident r = residents.get(i);
            Color bg = (i % 2 == 0) ? evenBg : Color.WHITE;

            addPhotoCell(table, r.getPhotoUrl(), bg);
            addCell(table, r.getCode() != null ? r.getCode() : "-", bg, CELL_FONT, Element.ALIGN_CENTER);
            addCell(table, (r.getFirstName() != null ? r.getFirstName() : "") + " " + (r.getLastName() != null ? r.getLastName() : ""), bg, CELL_FONT, Element.ALIGN_LEFT);
            addCell(table, r.getDocumentNumber() != null ? r.getDocumentNumber() : "-", bg, CELL_FONT, Element.ALIGN_CENTER);
            addCell(table, r.getRoom() != null ? r.getRoom().getName() : "-", bg, CELL_FONT, Element.ALIGN_CENTER);
            addCell(table, r.getStatus() != null ? r.getStatus().name() : "-", bg, CELL_FONT, Element.ALIGN_CENTER);
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

    private void addResidentsFooter(Document document) throws DocumentException {
        Paragraph line = new Paragraph("_".repeat(80), new Font(Font.HELVETICA, 6, Font.NORMAL, new Color(203, 213, 225)));
        line.setSpacingBefore(16);
        document.add(line);

        Paragraph footer = new Paragraph("Casa Hogar - Sistema de Residentes", FOOTER_FONT);
        footer.setAlignment(Element.ALIGN_CENTER);
        document.add(footer);
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
}
