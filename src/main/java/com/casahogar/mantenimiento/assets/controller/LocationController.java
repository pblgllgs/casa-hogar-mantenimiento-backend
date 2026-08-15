package com.casahogar.mantenimiento.assets.controller;

import com.casahogar.mantenimiento.assets.dto.LocationRequest;
import com.casahogar.mantenimiento.assets.dto.LocationResponse;
import com.casahogar.mantenimiento.assets.service.LocationService;
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
@RequestMapping("/assets/locations")
public class LocationController {

    private final LocationService locationService;

    public LocationController(LocationService locationService) {
        this.locationService = locationService;
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPERVISOR')")
    public ResponseEntity<ApiResponse<LocationResponse>> create(
            @Valid @RequestBody LocationRequest request,
            Authentication auth) {
        LocationResponse response = locationService.create(request, auth.getName());
        return ResponseEntity.ok(ApiResponse.ok(response, "Ubicación creada"));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPERVISOR')")
    public ResponseEntity<ApiResponse<LocationResponse>> update(
            @PathVariable Long id,
            @Valid @RequestBody LocationRequest request,
            Authentication auth) {
        LocationResponse response = locationService.update(id, request, auth.getName());
        return ResponseEntity.ok(ApiResponse.ok(response, "Ubicación actualizada"));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> delete(
            @PathVariable Long id,
            Authentication auth) {
        locationService.delete(id, auth.getName());
        return ResponseEntity.ok(ApiResponse.ok(null, "Ubicación eliminada"));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<LocationResponse>> getById(@PathVariable Long id) {
        LocationResponse response = locationService.getById(id);
        return ResponseEntity.ok(ApiResponse.ok(response));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<PageResponse<LocationResponse>>> getAll(SearchCriteria criteria) {
        PageResponse<LocationResponse> response = locationService.searchPaged(criteria.getSearch(), criteria.toPageRequest());
        return ResponseEntity.ok(ApiResponse.ok(response));
    }

    @GetMapping("/type/{type}")
    public ResponseEntity<ApiResponse<List<LocationResponse>>> getByType(@PathVariable String type) {
        List<LocationResponse> response = locationService.getByType(type);
        return ResponseEntity.ok(ApiResponse.ok(response));
    }

    @GetMapping("/parent/{parentId}")
    public ResponseEntity<ApiResponse<List<LocationResponse>>> getByParentId(@PathVariable Long parentId) {
        List<LocationResponse> response = locationService.getByParentId(parentId);
        return ResponseEntity.ok(ApiResponse.ok(response));
    }

    @GetMapping("/search")
    public ResponseEntity<ApiResponse<List<LocationResponse>>> search(@RequestParam String q) {
        List<LocationResponse> response = locationService.search(q);
        return ResponseEntity.ok(ApiResponse.ok(response));
    }
}
