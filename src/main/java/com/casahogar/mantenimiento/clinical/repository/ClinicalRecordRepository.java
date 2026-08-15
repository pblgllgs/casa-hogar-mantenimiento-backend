package com.casahogar.mantenimiento.clinical.repository;

import com.casahogar.mantenimiento.clinical.entity.ClinicalRecord;
import com.casahogar.mantenimiento.common.repository.BaseRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClinicalRecordRepository extends BaseRepository<ClinicalRecord, Long> {

    @Query("SELECT c FROM ClinicalRecord c WHERE c.residentId = :residentId AND c.deleted = false ORDER BY c.recordDate DESC")
    List<ClinicalRecord> findByResidentId(@Param("residentId") Long residentId);

    @Query("SELECT c FROM ClinicalRecord c WHERE c.residentId = :residentId AND c.deleted = false ORDER BY c.recordDate DESC")
    Page<ClinicalRecord> findByResidentIdPaged(@Param("residentId") Long residentId, Pageable pageable);

    @Query("SELECT c FROM ClinicalRecord c WHERE c.deleted = false ORDER BY c.recordDate DESC")
    Page<ClinicalRecord> findAllActivePaged(Pageable pageable);
}