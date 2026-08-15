package com.casahogar.mantenimiento.residents.controller;

import com.casahogar.mantenimiento.common.dto.ApiResponse;
import com.casahogar.mantenimiento.common.dto.PageResponse;
import com.casahogar.mantenimiento.common.dto.SearchCriteria;
import com.casahogar.mantenimiento.medications.service.MedicationReportService;
import com.casahogar.mantenimiento.reports.service.ResidentReportService;
import com.casahogar.mantenimiento.residents.dto.ResidentRequest;
import com.casahogar.mantenimiento.residents.dto.ResidentResponse;
import com.casahogar.mantenimiento.residents.service.ResidentService;
import jakarta.validation.Valid;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/residents")
public class ResidentController {

    private final ResidentService residentService;
    private final ResidentReportService residentReportService;
    private final MedicationReportService medicationReportService;

    public ResidentController(ResidentService residentService, ResidentReportService residentReportService,
                              MedicationReportService medicationReportService) {
        this.residentService = residentService;
        this.residentReportService = residentReportService;
        this.medicationReportService = medicationReportService;
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'RESIDENTS')")
    public ResponseEntity<ApiResponse<ResidentResponse>> create(
            @Valid @RequestBody ResidentRequest request,
            Authentication auth) {
        ResidentResponse response = residentService.create(request, auth.getName());
        return ResponseEntity.ok(ApiResponse.ok(response, "Residente creado"));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'RESIDENTS')")
    public ResponseEntity<ApiResponse<ResidentResponse>> update(
            @PathVariable Long id,
            @Valid @RequestBody ResidentRequest request,
            Authentication auth) {
        ResidentResponse response = residentService.update(id, request, auth.getName());
        return ResponseEntity.ok(ApiResponse.ok(response, "Residente actualizado"));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> delete(
            @PathVariable Long id,
            Authentication auth) {
        residentService.delete(id, auth.getName());
        return ResponseEntity.ok(ApiResponse.ok(null, "Residente eliminado"));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ResidentResponse>> getById(@PathVariable Long id) {
        ResidentResponse response = residentService.getById(id);
        return ResponseEntity.ok(ApiResponse.ok(response));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<PageResponse<ResidentResponse>>> getAll(SearchCriteria criteria) {
        PageResponse<ResidentResponse> response = residentService.search(criteria.getSearch(), criteria.toPageRequest());
        return ResponseEntity.ok(ApiResponse.ok(response));
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<ApiResponse<List<ResidentResponse>>> getByStatus(@PathVariable String status) {
        List<ResidentResponse> response = residentService.getByStatus(status);
        return ResponseEntity.ok(ApiResponse.ok(response));
    }

    @GetMapping("/room/{roomId}")
    public ResponseEntity<ApiResponse<List<ResidentResponse>>> getByRoom(@PathVariable Long roomId) {
        List<ResidentResponse> response = residentService.getByRoom(roomId);
        return ResponseEntity.ok(ApiResponse.ok(response));
    }

    @GetMapping("/{id}/report")
    public ResponseEntity<byte[]> generateReport(@PathVariable Long id) {
        byte[] pdfBytes = residentReportService.generateReport(id);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData("filename", "ficha-" + id + ".pdf");
        return ResponseEntity.ok().headers(headers).body(pdfBytes);
    }

    @GetMapping("/{id}/medications-report")
    public ResponseEntity<byte[]> generateMedicationsReport(@PathVariable Long id) {
        byte[] pdfBytes = medicationReportService.generateReport(id);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData("filename", "medicamentos-" + id + ".pdf");
        return ResponseEntity.ok().headers(headers).body(pdfBytes);
    }
}
