package com.casahogar.mantenimiento.hr.controller;

import com.casahogar.mantenimiento.common.dto.ApiResponse;
import com.casahogar.mantenimiento.common.dto.PageResponse;
import com.casahogar.mantenimiento.common.dto.SearchCriteria;
import com.casahogar.mantenimiento.hr.dto.ShiftRequest;
import com.casahogar.mantenimiento.hr.dto.ShiftResponse;
import com.casahogar.mantenimiento.hr.dto.StaffShiftResponse;
import com.casahogar.mantenimiento.hr.entity.StaffShift;
import com.casahogar.mantenimiento.hr.repository.ShiftRepository;
import com.casahogar.mantenimiento.hr.repository.StaffRepository;
import com.casahogar.mantenimiento.hr.service.ShiftService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/hr/shifts")
public class ShiftController {

    private final ShiftService shiftService;
    private final StaffRepository staffRepository;
    private final ShiftRepository shiftRepository;

    public ShiftController(ShiftService shiftService, StaffRepository staffRepository, ShiftRepository shiftRepository) {
        this.shiftService = shiftService;
        this.staffRepository = staffRepository;
        this.shiftRepository = shiftRepository;
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'HR')")
    public ResponseEntity<ApiResponse<ShiftResponse>> create(@Valid @RequestBody ShiftRequest request) {
        ShiftResponse response = shiftService.create(request);
        return ResponseEntity.ok(ApiResponse.ok(response, "Turno creado"));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'HR')")
    public ResponseEntity<ApiResponse<ShiftResponse>> update(
            @PathVariable Long id,
            @Valid @RequestBody ShiftRequest request) {
        ShiftResponse response = shiftService.update(id, request);
        return ResponseEntity.ok(ApiResponse.ok(response, "Turno actualizado"));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        shiftService.delete(id);
        return ResponseEntity.ok(ApiResponse.ok(null, "Turno eliminado"));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ShiftResponse>> getById(@PathVariable Long id) {
        ShiftResponse response = shiftService.getById(id);
        return ResponseEntity.ok(ApiResponse.ok(response));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<PageResponse<ShiftResponse>>> getAll(SearchCriteria criteria) {
        PageResponse<ShiftResponse> response = shiftService.search(criteria.getSearch(), criteria.toPageRequest());
        return ResponseEntity.ok(ApiResponse.ok(response));
    }

    @GetMapping("/test")
    public ResponseEntity<String> test() {
        return ResponseEntity.ok("ShiftController is working!");
    }

    @PostMapping("/assign")
    @PreAuthorize("hasAnyRole('ADMIN', 'HR')")
    public ResponseEntity<ApiResponse<Void>> assignShift(
            @RequestParam Long staffId,
            @RequestParam Long shiftId,
            @RequestParam(required = false) LocalDate startDate) {
        shiftService.assignShiftToStaff(staffId, shiftId, startDate != null ? startDate : LocalDate.now());
        return ResponseEntity.ok(ApiResponse.ok(null, "Turno asignado"));
    }

    @PostMapping("/{shiftId}/assign/{staffId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'HR')")
    public ResponseEntity<ApiResponse<Void>> assignShiftPath(
            @PathVariable Long shiftId,
            @PathVariable Long staffId,
            @RequestParam(required = false) LocalDate startDate) {
        shiftService.assignShiftToStaff(staffId, shiftId, startDate != null ? startDate : LocalDate.now());
        return ResponseEntity.ok(ApiResponse.ok(null, "Turno asignado"));
    }

    @GetMapping("/assignments")
    public ResponseEntity<ApiResponse<List<StaffShiftResponse>>> getAssignments() {
        List<StaffShift> assignments = shiftService.getAllActiveAssignments();
        Map<Long, String> staffNames = staffRepository.findAllById(
                assignments.stream().map(StaffShift::getStaffId).distinct().collect(Collectors.toList())
        ).stream().collect(Collectors.toMap(s -> s.getId(), s -> s.getFirstName() + " " + s.getLastName()));
        Map<Long, String> shiftNames = shiftRepository.findAllById(
                assignments.stream().map(StaffShift::getShiftId).distinct().collect(Collectors.toList())
        ).stream().collect(Collectors.toMap(s -> s.getId(), s -> s.getName()));
        List<StaffShiftResponse> response = assignments.stream()
                .map(ss -> StaffShiftResponse.of(ss, staffNames.get(ss.getStaffId()), shiftNames.get(ss.getShiftId())))
                .collect(Collectors.toList());
        return ResponseEntity.ok(ApiResponse.ok(response));
    }

    @DeleteMapping("/assign")
    @PreAuthorize("hasAnyRole('ADMIN', 'HR')")
    public ResponseEntity<ApiResponse<Void>> removeShift(
            @RequestParam Long staffId,
            @RequestParam Long shiftId) {
        shiftService.removeShiftFromStaff(staffId, shiftId);
        return ResponseEntity.ok(ApiResponse.ok(null, "Turno removido"));
    }

    @GetMapping("/staff/{staffId}")
    public ResponseEntity<ApiResponse<List<StaffShift>>> getStaffShifts(@PathVariable Long staffId) {
        List<StaffShift> response = shiftService.getStaffShifts(staffId);
        return ResponseEntity.ok(ApiResponse.ok(response));
    }
}
