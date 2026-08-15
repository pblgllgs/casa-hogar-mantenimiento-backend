package com.casahogar.mantenimiento.hr.service;

import com.casahogar.mantenimiento.common.dto.PageResponse;
import com.casahogar.mantenimiento.hr.dto.ShiftRequest;
import com.casahogar.mantenimiento.hr.dto.ShiftResponse;
import com.casahogar.mantenimiento.hr.entity.Shift;
import com.casahogar.mantenimiento.hr.entity.StaffShift;
import com.casahogar.mantenimiento.hr.repository.ShiftRepository;
import com.casahogar.mantenimiento.hr.repository.StaffShiftRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ShiftService {

    private final ShiftRepository shiftRepository;
    private final StaffShiftRepository staffShiftRepository;

    public ShiftService(ShiftRepository shiftRepository, StaffShiftRepository staffShiftRepository) {
        this.shiftRepository = shiftRepository;
        this.staffShiftRepository = staffShiftRepository;
    }

    @Transactional
    public ShiftResponse create(ShiftRequest request) {
        Shift shift = new Shift();
        shift.setName(request.getName());
        shift.setStartTime(request.getStartTime());
        shift.setEndTime(request.getEndTime());
        shift.setDaysOfWeek(request.getDaysOfWeek());
        shift.setIsActive(true);

        shiftRepository.save(shift);
        return ShiftResponse.of(shift);
    }

    @Transactional
    public ShiftResponse update(Long id, ShiftRequest request) {
        Shift shift = shiftRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Turno no encontrado"));

        shift.setName(request.getName());
        shift.setStartTime(request.getStartTime());
        shift.setEndTime(request.getEndTime());
        shift.setDaysOfWeek(request.getDaysOfWeek());

        shiftRepository.save(shift);
        return ShiftResponse.of(shift);
    }

    @Transactional
    public void delete(Long id) {
        Shift shift = shiftRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Turno no encontrado"));
        shift.setIsActive(false);
        shiftRepository.save(shift);
    }

    public ShiftResponse getById(Long id) {
        Shift shift = shiftRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Turno no encontrado"));
        return ShiftResponse.of(shift);
    }

    public List<ShiftResponse> getAll() {
        return shiftRepository.findAllActive().stream()
                .map(ShiftResponse::of)
                .collect(Collectors.toList());
    }

    public PageResponse<ShiftResponse> search(String search, Pageable pageable) {
        if (search != null && !search.isBlank()) {
            return PageResponse.of(shiftRepository.searchPaged(search, pageable).map(ShiftResponse::of));
        }
        return PageResponse.of(shiftRepository.findAllActivePaged(pageable).map(ShiftResponse::of));
    }

    @Transactional
    public void assignShiftToStaff(Long staffId, Long shiftId, java.time.LocalDate startDate) {
        List<StaffShift> existing = staffShiftRepository.findByStaffIdAndShiftIdAndStartDate(staffId, shiftId, startDate);
        StaffShift staffShift;
        if (!existing.isEmpty()) {
            staffShift = existing.get(0);
            staffShift.setIsActive(true);
        } else {
            staffShift = new StaffShift();
            staffShift.setStaffId(staffId);
            staffShift.setShiftId(shiftId);
            staffShift.setStartDate(startDate);
            staffShift.setIsActive(true);
        }
        staffShiftRepository.save(staffShift);
    }

    @Transactional
    public void removeShiftFromStaff(Long staffId, Long shiftId) {
        List<StaffShift> assignments = staffShiftRepository.findByStaffIdAndShiftId(staffId, shiftId);
        for (StaffShift ss : assignments) {
            ss.setIsActive(false);
            staffShiftRepository.save(ss);
        }
    }

    public List<StaffShift> getStaffShifts(Long staffId) {
        return staffShiftRepository.findActiveByStaffId(staffId);
    }

    public List<StaffShift> getAllActiveAssignments() {
        return staffShiftRepository.findAllByIsActiveTrue();
    }
}
