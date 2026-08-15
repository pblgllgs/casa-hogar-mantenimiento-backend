package com.casahogar.mantenimiento.medications.repository;

import com.casahogar.mantenimiento.medications.entity.Medication;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MedicationRepository extends JpaRepository<Medication, Long> {
    List<Medication> findByResidentIdAndDeletedFalseOrderByIdDesc(Long residentId);
}
