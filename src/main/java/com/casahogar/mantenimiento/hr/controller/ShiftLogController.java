package com.casahogar.mantenimiento.hr.controller;

import com.casahogar.mantenimiento.common.dto.ApiResponse;
import com.casahogar.mantenimiento.hr.dto.ShiftLogRequest;
import com.casahogar.mantenimiento.hr.dto.ShiftLogResponse;
import com.casahogar.mantenimiento.hr.service.ShiftLogService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/hr/shift-logs")
public class ShiftLogController {

    private final ShiftLogService shiftLogService;

    public ShiftLogController(ShiftLogService shiftLogService) {
        this.shiftLogService = shiftLogService;
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'HR')")
    public ResponseEntity<ApiResponse<ShiftLogResponse>> create(@RequestBody ShiftLogRequest request) {
        ShiftLogResponse response = shiftLogService.create(request);
        return ResponseEntity.ok(ApiResponse.ok(response, "Comentario registrado"));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'HR')")
    public ResponseEntity<ApiResponse<ShiftLogResponse>> update(@PathVariable Long id, @RequestBody ShiftLogRequest request) {
        ShiftLogResponse response = shiftLogService.update(id, request);
        return ResponseEntity.ok(ApiResponse.ok(response, "Comentario actualizado"));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'HR')")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        shiftLogService.delete(id);
        return ResponseEntity.ok(ApiResponse.ok(null, "Comentario eliminado"));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<ShiftLogResponse>>> getAll(
            @RequestParam(required = false) Long shiftId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate start,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate end) {
        List<ShiftLogResponse> response;
        if (shiftId != null && date != null) {
            response = shiftLogService.getByShiftAndDate(shiftId, date);
        } else if (start != null && end != null) {
            response = shiftLogService.getByDateRange(start, end);
        } else {
            response = shiftLogService.getAll();
        }
        return ResponseEntity.ok(ApiResponse.ok(response));
    }
}