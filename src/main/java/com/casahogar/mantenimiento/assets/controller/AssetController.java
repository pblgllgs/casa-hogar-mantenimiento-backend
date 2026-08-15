package com.casahogar.mantenimiento.assets.controller;

import com.casahogar.mantenimiento.assets.dto.AssetRequest;
import com.casahogar.mantenimiento.assets.dto.AssetResponse;
import com.casahogar.mantenimiento.assets.service.AssetService;
import com.casahogar.mantenimiento.common.dto.ApiResponse;
import com.casahogar.mantenimiento.common.dto.PageResponse;
import com.casahogar.mantenimiento.common.dto.SearchCriteria;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/assets")
public class AssetController {

    private final AssetService assetService;

    public AssetController(AssetService assetService) {
        this.assetService = assetService;
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPERVISOR', 'MAINTENANCE')")
    public ResponseEntity<ApiResponse<AssetResponse>> create(
            @Valid @RequestBody AssetRequest request,
            Authentication auth) {
        AssetResponse response = assetService.create(request, auth.getName());
        return ResponseEntity.ok(ApiResponse.ok(response, "Activo creado"));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPERVISOR', 'MAINTENANCE')")
    public ResponseEntity<ApiResponse<AssetResponse>> update(
            @PathVariable Long id,
            @Valid @RequestBody AssetRequest request,
            Authentication auth) {
        AssetResponse response = assetService.update(id, request, auth.getName());
        return ResponseEntity.ok(ApiResponse.ok(response, "Activo actualizado"));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPERVISOR')")
    public ResponseEntity<ApiResponse<Void>> delete(
            @PathVariable Long id,
            Authentication auth) {
        assetService.delete(id, auth.getName());
        return ResponseEntity.ok(ApiResponse.ok(null, "Activo eliminado"));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<AssetResponse>> getById(@PathVariable Long id) {
        AssetResponse response = assetService.getById(id);
        return ResponseEntity.ok(ApiResponse.ok(response));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<PageResponse<AssetResponse>>> search(SearchCriteria criteria) {
        PageResponse<AssetResponse> response = assetService.search(criteria);
        return ResponseEntity.ok(ApiResponse.ok(response));
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<ApiResponse<List<AssetResponse>>> getByStatus(@PathVariable String status) {
        List<AssetResponse> response = assetService.getByStatus(status);
        return ResponseEntity.ok(ApiResponse.ok(response));
    }

    @GetMapping("/location/{locationId}")
    public ResponseEntity<ApiResponse<List<AssetResponse>>> getByLocation(@PathVariable Long locationId) {
        List<AssetResponse> response = assetService.getByLocation(locationId);
        return ResponseEntity.ok(ApiResponse.ok(response));
    }

    @GetMapping("/due-maintenance")
    public ResponseEntity<ApiResponse<List<AssetResponse>>> getDueForMaintenance() {
        List<AssetResponse> response = assetService.getDueForMaintenance();
        return ResponseEntity.ok(ApiResponse.ok(response));
    }
}
