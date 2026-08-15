package com.casahogar.mantenimiento.residents.controller;

import com.casahogar.mantenimiento.common.dto.ApiResponse;
import com.casahogar.mantenimiento.residents.dto.ResidentDailyHistoryRequest;
import com.casahogar.mantenimiento.residents.dto.ResidentDailyHistoryResponse;
import com.casahogar.mantenimiento.residents.service.ResidentDailyHistoryService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/residents/{residentId}/history")
public class ResidentDailyHistoryController {

    private final ResidentDailyHistoryService service;

    public ResidentDailyHistoryController(ResidentDailyHistoryService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<ResidentDailyHistoryResponse>>> getByDate(
            @PathVariable Long residentId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        List<ResidentDailyHistoryResponse> response = service.getByResidentAndDate(residentId, date);
        return ResponseEntity.ok(ApiResponse.ok(response));
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'HR')")
    public ResponseEntity<ApiResponse<ResidentDailyHistoryResponse>> upsert(
            @PathVariable Long residentId,
            @RequestBody ResidentDailyHistoryRequest request) {
        ResidentDailyHistoryResponse response = service.upsert(residentId, request);
        return ResponseEntity.ok(ApiResponse.ok(response, "Historial guardado"));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'HR')")
    public ResponseEntity<ApiResponse<Void>> delete(
            @PathVariable Long residentId,
            @PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.ok(ApiResponse.ok(null, "Historial eliminado"));
    }
}
