package com.casahogar.mantenimiento.medications.controller;

import com.casahogar.mantenimiento.common.dto.ApiResponse;
import com.casahogar.mantenimiento.medications.dto.MedicationRequest;
import com.casahogar.mantenimiento.medications.dto.MedicationResponse;
import com.casahogar.mantenimiento.medications.service.MedicationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/medications")
public class MedicationController {

    private final MedicationService medicationService;

    public MedicationController(MedicationService medicationService) {
        this.medicationService = medicationService;
    }

    @GetMapping("/resident/{residentId}")
    public ResponseEntity<ApiResponse<List<MedicationResponse>>> getByResident(@PathVariable Long residentId) {
        List<MedicationResponse> meds = medicationService.getByResident(residentId);
        return ResponseEntity.ok(ApiResponse.ok(meds));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<MedicationResponse>> create(@RequestBody MedicationRequest req) {
        MedicationResponse med = medicationService.create(req);
        return ResponseEntity.ok(ApiResponse.ok(med, "Medicamento registrado"));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<MedicationResponse>> update(@PathVariable Long id, @RequestBody MedicationRequest req) {
        MedicationResponse med = medicationService.update(id, req);
        return ResponseEntity.ok(ApiResponse.ok(med, "Medicamento actualizado"));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        medicationService.delete(id);
        return ResponseEntity.ok(ApiResponse.ok(null, "Medicamento eliminado"));
    }
}
