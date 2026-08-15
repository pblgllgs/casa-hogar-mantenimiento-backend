package com.casahogar.mantenimiento.hr.service;

import com.casahogar.mantenimiento.hr.dto.ShiftLogRequest;
import com.casahogar.mantenimiento.hr.dto.ShiftLogResponse;
import com.casahogar.mantenimiento.hr.entity.Shift;
import com.casahogar.mantenimiento.hr.entity.ShiftLog;
import com.casahogar.mantenimiento.hr.entity.Staff;
import com.casahogar.mantenimiento.hr.repository.ShiftLogRepository;
import com.casahogar.mantenimiento.hr.repository.ShiftRepository;
import com.casahogar.mantenimiento.hr.repository.StaffRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ShiftLogService {

    private final ShiftLogRepository shiftLogRepository;
    private final ShiftRepository shiftRepository;
    private final StaffRepository staffRepository;

    public ShiftLogService(ShiftLogRepository shiftLogRepository, ShiftRepository shiftRepository, StaffRepository staffRepository) {
        this.shiftLogRepository = shiftLogRepository;
        this.shiftRepository = shiftRepository;
        this.staffRepository = staffRepository;
    }

    @Transactional
    public ShiftLogResponse create(ShiftLogRequest request) {
        if (!shiftRepository.existsById(request.getShiftId())) {
            throw new IllegalArgumentException("Turno no encontrado");
        }
        if (!staffRepository.existsById(request.getStaffId())) {
            throw new IllegalArgumentException("Personal no encontrado");
        }
        ShiftLog log = new ShiftLog();
        log.setShiftId(request.getShiftId());
        log.setStaffId(request.getStaffId());
        log.setLogDate(request.getLogDate());
        log.setComment(request.getComment());
        log.setUpdatedAt(LocalDateTime.now());
        shiftLogRepository.save(log);
        return toResponse(log);
    }

    @Transactional
    public void delete(Long id) {
        shiftLogRepository.deleteById(id);
    }

    @Transactional
    public ShiftLogResponse update(Long id, ShiftLogRequest request) {
        ShiftLog log = shiftLogRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Registro no encontrado"));
        if (request.getShiftId() != null) log.setShiftId(request.getShiftId());
        if (request.getStaffId() != null) log.setStaffId(request.getStaffId());
        if (request.getLogDate() != null) log.setLogDate(request.getLogDate());
        if (request.getComment() != null) log.setComment(request.getComment());
        log.setUpdatedAt(LocalDateTime.now());
        shiftLogRepository.save(log);
        return toResponse(log);
    }

    public List<ShiftLogResponse> getByShiftAndDate(Long shiftId, LocalDate date) {
        return shiftLogRepository.findByShiftIdAndLogDateOrderByCreatedAtAsc(shiftId, date)
                .stream().map(this::toResponse).collect(Collectors.toList());
    }

    public List<ShiftLogResponse> getByDateRange(LocalDate start, LocalDate end) {
        return shiftLogRepository.findByLogDateBetweenOrderByCreatedAtAsc(start, end)
                .stream().map(this::toResponse).collect(Collectors.toList());
    }

    public List<ShiftLogResponse> getAll() {
        List<ShiftLog> logs = shiftLogRepository.findAll();
        logs.sort(Comparator.comparing(ShiftLog::getCreatedAt).reversed());
        return logs.stream().map(this::toResponse).collect(Collectors.toList());
    }

    private ShiftLogResponse toResponse(ShiftLog log) {
        String shiftName = shiftRepository.findById(log.getShiftId())
                .map(Shift::getName).orElse("");
        String staffName = staffRepository.findById(log.getStaffId())
                .map(s -> s.getFirstName() + " " + s.getLastName()).orElse("");
        return ShiftLogResponse.of(log, shiftName, staffName);
    }
}