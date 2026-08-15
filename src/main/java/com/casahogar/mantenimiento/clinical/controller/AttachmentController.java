package com.casahogar.mantenimiento.clinical.controller;

import com.casahogar.mantenimiento.clinical.dto.AttachmentRequest;
import com.casahogar.mantenimiento.clinical.dto.AttachmentResponse;
import com.casahogar.mantenimiento.clinical.service.AttachmentService;
import com.casahogar.mantenimiento.common.dto.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/clinical-records/attachments")
public class AttachmentController {

    private final AttachmentService attachmentService;

    public AttachmentController(AttachmentService attachmentService) {
        this.attachmentService = attachmentService;
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'RESIDENTS')")
    public ResponseEntity<ApiResponse<AttachmentResponse>> create(
            @Valid @RequestBody AttachmentRequest request,
            Authentication auth) {
        AttachmentResponse response = attachmentService.create(request, auth.getName());
        return ResponseEntity.ok(ApiResponse.ok(response, "Adjunto creado"));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'RESIDENTS')")
    public ResponseEntity<ApiResponse<Void>> delete(
            @PathVariable Long id,
            Authentication auth) {
        attachmentService.delete(id, auth.getName());
        return ResponseEntity.ok(ApiResponse.ok(null, "Adjunto eliminado"));
    }

    @GetMapping("/record/{clinicalRecordId}")
    public ResponseEntity<ApiResponse<List<AttachmentResponse>>> getByRecord(
            @PathVariable Long clinicalRecordId) {
        List<AttachmentResponse> response = attachmentService.getByClinicalRecord(clinicalRecordId);
        return ResponseEntity.ok(ApiResponse.ok(response));
    }
}
