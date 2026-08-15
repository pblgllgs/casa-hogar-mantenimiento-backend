package com.casahogar.mantenimiento.clinical.controller;

import com.casahogar.mantenimiento.clinical.dto.ClinicalRecordRequest;
import com.casahogar.mantenimiento.clinical.dto.ClinicalRecordResponse;
import com.casahogar.mantenimiento.clinical.service.ClinicalRecordService;
import com.casahogar.mantenimiento.common.dto.ApiResponse;
import com.casahogar.mantenimiento.common.dto.PageResponse;
import com.casahogar.mantenimiento.common.dto.SearchCriteria;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/clinical-records")
public class ClinicalRecordController {

    private final ClinicalRecordService clinicalRecordService;

    public ClinicalRecordController(ClinicalRecordService clinicalRecordService) {
        this.clinicalRecordService = clinicalRecordService;
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'RESIDENTS')")
    public ResponseEntity<ApiResponse<ClinicalRecordResponse>> create(
            @Valid @RequestBody ClinicalRecordRequest request,
            Authentication auth) {
        ClinicalRecordResponse response = clinicalRecordService.create(request, auth.getName());
        return ResponseEntity.ok(ApiResponse.ok(response, "Registro clínico creado"));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'RESIDENTS')")
    public ResponseEntity<ApiResponse<ClinicalRecordResponse>> update(
            @PathVariable Long id,
            @Valid @RequestBody ClinicalRecordRequest request,
            Authentication auth) {
        ClinicalRecordResponse response = clinicalRecordService.update(id, request, auth.getName());
        return ResponseEntity.ok(ApiResponse.ok(response, "Registro clínico actualizado"));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> delete(
            @PathVariable Long id,
            Authentication auth) {
        clinicalRecordService.delete(id, auth.getName());
        return ResponseEntity.ok(ApiResponse.ok(null, "Registro clínico eliminado"));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ClinicalRecordResponse>> getById(@PathVariable Long id) {
        ClinicalRecordResponse response = clinicalRecordService.getById(id);
        return ResponseEntity.ok(ApiResponse.ok(response));
    }

    @GetMapping("/resident/{residentId}")
    public ResponseEntity<ApiResponse<PageResponse<ClinicalRecordResponse>>> getByResident(
            @PathVariable Long residentId, SearchCriteria criteria) {
        PageResponse<ClinicalRecordResponse> response = clinicalRecordService.getByResidentPaged(residentId, criteria.toPageRequest());
        return ResponseEntity.ok(ApiResponse.ok(response));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<PageResponse<ClinicalRecordResponse>>> getAll(SearchCriteria criteria) {
        PageResponse<ClinicalRecordResponse> response = clinicalRecordService.getAllPaged(criteria.toPageRequest());
        return ResponseEntity.ok(ApiResponse.ok(response));
    }
}