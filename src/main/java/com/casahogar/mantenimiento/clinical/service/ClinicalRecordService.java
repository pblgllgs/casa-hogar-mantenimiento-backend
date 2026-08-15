package com.casahogar.mantenimiento.clinical.service;

import com.casahogar.mantenimiento.clinical.dto.ClinicalRecordRequest;
import com.casahogar.mantenimiento.clinical.dto.ClinicalRecordResponse;
import com.casahogar.mantenimiento.clinical.entity.ClinicalRecord;
import com.casahogar.mantenimiento.clinical.repository.ClinicalRecordRepository;
import com.casahogar.mantenimiento.common.dto.PageResponse;
import com.casahogar.mantenimiento.residents.entity.Resident;
import com.casahogar.mantenimiento.residents.repository.ResidentRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ClinicalRecordService {

    private final ClinicalRecordRepository clinicalRecordRepository;
    private final ResidentRepository residentRepository;

    public ClinicalRecordService(ClinicalRecordRepository clinicalRecordRepository, ResidentRepository residentRepository) {
        this.clinicalRecordRepository = clinicalRecordRepository;
        this.residentRepository = residentRepository;
    }

    @Transactional
    public ClinicalRecordResponse create(ClinicalRecordRequest request, String currentUser) {
        residentRepository.findByIdActive(request.getResidentId())
                .orElseThrow(() -> new IllegalArgumentException("Residente no encontrado"));

        ClinicalRecord record = new ClinicalRecord();
        record.setResidentId(request.getResidentId());
        record.setRecordDate(request.getRecordDate());
        record.setRecordType(ClinicalRecord.RecordType.valueOf(request.getRecordType()));
        record.setDescription(request.getDescription());
        record.setDiagnosis(request.getDiagnosis());
        record.setTreatment(request.getTreatment());
        record.setMedication(request.getMedication());
        record.setDosage(request.getDosage());
        record.setDoctorName(request.getDoctorName());
        record.setNotes(request.getNotes());

        clinicalRecordRepository.save(record);
        String residentName = getResidentName(request.getResidentId());
        return ClinicalRecordResponse.of(record, residentName);
    }

    @Transactional
    public ClinicalRecordResponse update(Long id, ClinicalRecordRequest request, String currentUser) {
        ClinicalRecord record = clinicalRecordRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Registro clínico no encontrado"));

        record.setRecordDate(request.getRecordDate());
        record.setRecordType(ClinicalRecord.RecordType.valueOf(request.getRecordType()));
        record.setDescription(request.getDescription());
        record.setDiagnosis(request.getDiagnosis());
        record.setTreatment(request.getTreatment());
        record.setMedication(request.getMedication());
        record.setDosage(request.getDosage());
        record.setDoctorName(request.getDoctorName());
        record.setNotes(request.getNotes());

        clinicalRecordRepository.save(record);
        String residentName = getResidentName(record.getResidentId());
        return ClinicalRecordResponse.of(record, residentName);
    }

    @Transactional
    public void delete(Long id, String currentUser) {
        ClinicalRecord record = clinicalRecordRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Registro clínico no encontrado"));
        clinicalRecordRepository.softDeleteById(id, currentUser);
    }

    public ClinicalRecordResponse getById(Long id) {
        ClinicalRecord record = clinicalRecordRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Registro clínico no encontrado"));
        String residentName = getResidentName(record.getResidentId());
        return ClinicalRecordResponse.of(record, residentName);
    }

    public List<ClinicalRecordResponse> getByResident(Long residentId) {
        return clinicalRecordRepository.findByResidentId(residentId).stream()
                .map(r -> ClinicalRecordResponse.of(r, getResidentName(r.getResidentId())))
                .collect(Collectors.toList());
    }

    public PageResponse<ClinicalRecordResponse> getByResidentPaged(Long residentId, Pageable pageable) {
        return PageResponse.of(
                clinicalRecordRepository.findByResidentIdPaged(residentId, pageable)
                        .map(r -> ClinicalRecordResponse.of(r, getResidentName(r.getResidentId())))
        );
    }

    public PageResponse<ClinicalRecordResponse> getAllPaged(Pageable pageable) {
        return PageResponse.of(
                clinicalRecordRepository.findAllActivePaged(pageable)
                        .map(r -> ClinicalRecordResponse.of(r, getResidentName(r.getResidentId())))
        );
    }

    private String getResidentName(Long residentId) {
        return residentRepository.findByIdActive(residentId)
                .map(r -> r.getFirstName() + " " + r.getLastName())
                .orElse("Desconocido");
    }
}