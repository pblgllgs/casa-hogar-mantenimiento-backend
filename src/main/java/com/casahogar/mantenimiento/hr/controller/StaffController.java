package com.casahogar.mantenimiento.hr.controller;

import com.casahogar.mantenimiento.common.dto.ApiResponse;
import com.casahogar.mantenimiento.common.dto.PageResponse;
import com.casahogar.mantenimiento.common.dto.SearchCriteria;
import com.casahogar.mantenimiento.hr.dto.StaffRequest;
import com.casahogar.mantenimiento.hr.dto.StaffResponse;
import com.casahogar.mantenimiento.hr.service.StaffService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/hr/staff")
public class StaffController {

    private final StaffService staffService;

    public StaffController(StaffService staffService) {
        this.staffService = staffService;
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'HR')")
    public ResponseEntity<ApiResponse<StaffResponse>> create(
            @Valid @RequestBody StaffRequest request,
            Authentication auth) {
        StaffResponse response = staffService.create(request, auth.getName());
        return ResponseEntity.ok(ApiResponse.ok(response, "Empleado creado"));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'HR')")
    public ResponseEntity<ApiResponse<StaffResponse>> update(
            @PathVariable Long id,
            @Valid @RequestBody StaffRequest request,
            Authentication auth) {
        StaffResponse response = staffService.update(id, request, auth.getName());
        return ResponseEntity.ok(ApiResponse.ok(response, "Empleado actualizado"));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> delete(
            @PathVariable Long id,
            Authentication auth) {
        staffService.delete(id, auth.getName());
        return ResponseEntity.ok(ApiResponse.ok(null, "Empleado eliminado"));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<StaffResponse>> getById(@PathVariable Long id) {
        StaffResponse response = staffService.getById(id);
        return ResponseEntity.ok(ApiResponse.ok(response));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<PageResponse<StaffResponse>>> getAll(SearchCriteria criteria,
            @RequestParam(required = false) String department) {
        PageResponse<StaffResponse> response = staffService.search(criteria.getSearch(), department, criteria.toPageRequest());
        return ResponseEntity.ok(ApiResponse.ok(response));
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<ApiResponse<List<StaffResponse>>> getByStatus(@PathVariable String status) {
        List<StaffResponse> response = staffService.getByStatus(status);
        return ResponseEntity.ok(ApiResponse.ok(response));
    }

    @GetMapping("/department/{department}")
    public ResponseEntity<ApiResponse<List<StaffResponse>>> getByDepartment(@PathVariable String department) {
        List<StaffResponse> response = staffService.getByDepartment(department);
        return ResponseEntity.ok(ApiResponse.ok(response));
    }

    @GetMapping("/search")
    public ResponseEntity<ApiResponse<List<StaffResponse>>> search(@RequestParam String q) {
        List<StaffResponse> response = staffService.search(q);
        return ResponseEntity.ok(ApiResponse.ok(response));
    }
}
