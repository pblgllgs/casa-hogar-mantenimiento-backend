package com.casahogar.mantenimiento.location.controller;

import com.casahogar.mantenimiento.common.dto.ApiResponse;
import com.casahogar.mantenimiento.location.dto.LocationPhotoRequest;
import com.casahogar.mantenimiento.location.dto.LocationPhotoResponse;
import com.casahogar.mantenimiento.location.service.LocationPhotoService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/location-photos")
public class LocationPhotoController {

    private final LocationPhotoService service;

    public LocationPhotoController(LocationPhotoService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<LocationPhotoResponse>>> getAll() {
        return ResponseEntity.ok(ApiResponse.ok(service.getAll()));
    }

    @GetMapping("/active")
    public ResponseEntity<ApiResponse<List<LocationPhotoResponse>>> getActive() {
        return ResponseEntity.ok(ApiResponse.ok(service.getActive()));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<LocationPhotoResponse>> create(
            @RequestBody LocationPhotoRequest request,
            Authentication auth) {
        return ResponseEntity.ok(ApiResponse.ok(service.create(request, auth.getName()), "Foto creada"));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<LocationPhotoResponse>> update(
            @PathVariable Long id,
            @RequestBody LocationPhotoRequest request,
            Authentication auth) {
        return ResponseEntity.ok(ApiResponse.ok(service.update(id, request, auth.getName()), "Foto actualizada"));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id, Authentication auth) {
        service.delete(id, auth.getName());
        return ResponseEntity.ok(ApiResponse.ok(null, "Foto eliminada"));
    }
}
