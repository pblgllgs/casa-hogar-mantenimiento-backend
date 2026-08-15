package com.casahogar.mantenimiento.hr.service;

import com.casahogar.mantenimiento.common.dto.PageResponse;
import com.casahogar.mantenimiento.common.service.CloudinaryService;
import com.casahogar.mantenimiento.hr.dto.StaffRequest;
import com.casahogar.mantenimiento.hr.dto.StaffResponse;
import com.casahogar.mantenimiento.hr.entity.Staff;
import com.casahogar.mantenimiento.hr.repository.StaffRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class StaffService {

    private final StaffRepository staffRepository;
    private final CloudinaryService cloudinaryService;

    public StaffService(StaffRepository staffRepository, CloudinaryService cloudinaryService) {
        this.staffRepository = staffRepository;
        this.cloudinaryService = cloudinaryService;
    }

    @Transactional
    public StaffResponse create(StaffRequest request, String currentUser) {
        if (staffRepository.existsByEmployeeCode(request.getEmployeeCode())) {
            throw new IllegalArgumentException("Ya existe un empleado con el código: " + request.getEmployeeCode());
        }
        if (staffRepository.existsByDocumentNumber(request.getDocumentNumber())) {
            throw new IllegalArgumentException("Ya existe un empleado con el número de documento: " + request.getDocumentNumber());
        }

        Staff staff = new Staff();
        staff.setEmployeeCode(request.getEmployeeCode());
        staff.setUserId(request.getUserId());
        staff.setFirstName(request.getFirstName());
        staff.setLastName(request.getLastName());
        staff.setDocumentType(Staff.StaffDocumentType.valueOf(request.getDocumentType()));
        staff.setDocumentNumber(request.getDocumentNumber());
        staff.setBirthDate(request.getBirthDate());
        staff.setGender(request.getGender());
        staff.setHireDate(request.getHireDate());
        staff.setPosition(request.getPosition());
        staff.setDepartment(request.getDepartment());
        if (request.getShift() != null) {
            staff.setShift(Staff.StaffShiftType.valueOf(request.getShift()));
        }
        staff.setPhone(request.getPhone());
        staff.setEmergencyContactName(request.getEmergencyContactName());
        staff.setEmergencyContactPhone(request.getEmergencyContactPhone());
        staff.setBankAccount(request.getBankAccount());
        staff.setSalary(request.getSalary());
        staff.setStatus(Staff.StaffStatus.valueOf(request.getStatus()));
        staff.setIsActive(true);
        staff.setPhotoUrl(request.getPhotoUrl());

        staffRepository.save(staff);
        return StaffResponse.of(staff);
    }

    @Transactional
    public StaffResponse update(Long id, StaffRequest request, String currentUser) {
        Staff staff = staffRepository.findByIdActive(id)
                .orElseThrow(() -> new IllegalArgumentException("Empleado no encontrado"));

        String oldPhotoUrl = staff.getPhotoUrl();
        String newPhotoUrl = request.getPhotoUrl();

        if (newPhotoUrl == null || newPhotoUrl.isBlank()) {
            if (oldPhotoUrl != null && !oldPhotoUrl.isBlank()) {
                String oldPublicId = cloudinaryService.extractPublicId(oldPhotoUrl);
                cloudinaryService.deleteImage(oldPublicId);
            }
        } else if (oldPhotoUrl != null && !oldPhotoUrl.isBlank() && !oldPhotoUrl.equals(newPhotoUrl)) {
            String oldPublicId = cloudinaryService.extractPublicId(oldPhotoUrl);
            cloudinaryService.deleteImage(oldPublicId);
        }

        staff.setFirstName(request.getFirstName());
        staff.setLastName(request.getLastName());
        staff.setDocumentType(Staff.StaffDocumentType.valueOf(request.getDocumentType()));
        staff.setDocumentNumber(request.getDocumentNumber());
        staff.setBirthDate(request.getBirthDate());
        staff.setGender(request.getGender());
        staff.setHireDate(request.getHireDate());
        staff.setPosition(request.getPosition());
        staff.setDepartment(request.getDepartment());
        if (request.getShift() != null) {
            staff.setShift(Staff.StaffShiftType.valueOf(request.getShift()));
        }
        staff.setPhone(request.getPhone());
        staff.setEmergencyContactName(request.getEmergencyContactName());
        staff.setEmergencyContactPhone(request.getEmergencyContactPhone());
        staff.setBankAccount(request.getBankAccount());
        staff.setSalary(request.getSalary());
        staff.setStatus(Staff.StaffStatus.valueOf(request.getStatus()));
        staff.setPhotoUrl(newPhotoUrl);

        staffRepository.save(staff);
        return StaffResponse.of(staff);
    }

    @Transactional
    public void delete(Long id, String currentUser) {
        Staff staff = staffRepository.findByIdActive(id)
                .orElseThrow(() -> new IllegalArgumentException("Empleado no encontrado"));
        if (staff.getPhotoUrl() != null && !staff.getPhotoUrl().isBlank()) {
            String publicId = cloudinaryService.extractPublicId(staff.getPhotoUrl());
            cloudinaryService.deleteImage(publicId);
        }
        staffRepository.softDeleteById(id, currentUser);
    }

    public StaffResponse getById(Long id) {
        Staff staff = staffRepository.findByIdActive(id)
                .orElseThrow(() -> new IllegalArgumentException("Empleado no encontrado"));
        return StaffResponse.of(staff);
    }

    public PageResponse<StaffResponse> search(String search, String department, Pageable pageable) {
        if (search != null && !search.isBlank()) {
            return PageResponse.of(staffRepository.searchPaged(search, pageable).map(StaffResponse::of));
        }
        if (department != null && !department.isBlank()) {
            return PageResponse.of(staffRepository.findByDepartmentPaged(department, pageable).map(StaffResponse::of));
        }
        return PageResponse.of(staffRepository.findAllActivePaged(pageable).map(StaffResponse::of));
    }

    public List<StaffResponse> getAll() {
        return staffRepository.findAllActive().stream()
                .map(StaffResponse::of)
                .collect(Collectors.toList());
    }

    public List<StaffResponse> getByStatus(String status) {
        return staffRepository.findByStatus(Staff.StaffStatus.valueOf(status)).stream()
                .filter(s -> !Boolean.TRUE.equals(s.getDeleted()))
                .map(StaffResponse::of)
                .collect(Collectors.toList());
    }

    public List<StaffResponse> getByDepartment(String department) {
        return staffRepository.findByDepartment(department).stream()
                .filter(s -> !Boolean.TRUE.equals(s.getDeleted()))
                .map(StaffResponse::of)
                .collect(Collectors.toList());
    }

    public List<StaffResponse> search(String query) {
        return staffRepository.search(query).stream()
                .map(StaffResponse::of)
                .collect(Collectors.toList());
    }
}
