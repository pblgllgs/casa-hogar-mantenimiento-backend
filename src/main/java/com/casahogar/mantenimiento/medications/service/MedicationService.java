package com.casahogar.mantenimiento.medications.service;

import com.casahogar.mantenimiento.medications.dto.MedicationRequest;
import com.casahogar.mantenimiento.medications.dto.MedicationResponse;
import com.casahogar.mantenimiento.medications.entity.Medication;
import com.casahogar.mantenimiento.medications.repository.MedicationRepository;
import com.casahogar.mantenimiento.residents.entity.Resident;
import com.casahogar.mantenimiento.residents.repository.ResidentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MedicationService {

    private final MedicationRepository medicationRepository;
    private final ResidentRepository residentRepository;

    public MedicationService(MedicationRepository medicationRepository, ResidentRepository residentRepository) {
        this.medicationRepository = medicationRepository;
        this.residentRepository = residentRepository;
    }

    public List<MedicationResponse> getByResident(Long residentId) {
        return medicationRepository.findByResidentIdAndDeletedFalseOrderByIdDesc(residentId)
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public MedicationResponse create(MedicationRequest req) {
        Resident resident = residentRepository.findById(req.getResidentId())
                .orElseThrow(() -> new RuntimeException("Residente no encontrado"));
        Medication m = new Medication();
        m.setResident(resident);
        applyFields(m, req);
        return toResponse(medicationRepository.save(m));
    }

    @Transactional
    public MedicationResponse update(Long id, MedicationRequest req) {
        Medication m = medicationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Medicamento no encontrado"));
        applyFields(m, req);
        return toResponse(medicationRepository.save(m));
    }

    @Transactional
    public void delete(Long id) {
        Medication m = medicationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Medicamento no encontrado"));
        m.softDelete("system");
        medicationRepository.save(m);
    }

    private void applyFields(Medication m, MedicationRequest req) {
        m.setMedicationName(req.getMedicationName());
        m.setDosage(req.getDosage());
        m.setFrequencyHours(req.getFrequencyHours());
        m.setAdministrationRoute(req.getAdministrationRoute() != null ? req.getAdministrationRoute() : "ORAL");
        if (req.getStartDate() != null) m.setStartDate(LocalDate.parse(req.getStartDate()));
        if (req.getEndDate() != null) m.setEndDate(LocalDate.parse(req.getEndDate()));
        else m.setEndDate(null);
        m.setInstructions(req.getInstructions());
        m.setPrescribedBy(req.getPrescribedBy());
        m.setStatus(req.getStatus() != null ? req.getStatus() : "ACTIVE");
        m.setNotes(req.getNotes());
    }

    private MedicationResponse toResponse(Medication m) {
        MedicationResponse r = new MedicationResponse();
        r.setId(m.getId());
        r.setResidentId(m.getResident().getId());
        r.setResidentName(m.getResident().getFirstName() + " " + m.getResident().getLastName());
        r.setMedicationName(m.getMedicationName());
        r.setDosage(m.getDosage());
        r.setFrequencyHours(m.getFrequencyHours());
        r.setAdministrationRoute(m.getAdministrationRoute());
        r.setStartDate(m.getStartDate());
        r.setEndDate(m.getEndDate());
        r.setInstructions(m.getInstructions());
        r.setPrescribedBy(m.getPrescribedBy());
        r.setStatus(m.getStatus());
        r.setNotes(m.getNotes());
        r.setCreatedAt(m.getCreatedAt());
        return r;
    }
}
