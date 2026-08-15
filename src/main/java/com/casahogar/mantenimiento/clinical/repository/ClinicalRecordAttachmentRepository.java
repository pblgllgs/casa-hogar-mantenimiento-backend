package com.casahogar.mantenimiento.clinical.repository;

import com.casahogar.mantenimiento.clinical.entity.ClinicalRecordAttachment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClinicalRecordAttachmentRepository extends JpaRepository<ClinicalRecordAttachment, Long> {

    List<ClinicalRecordAttachment> findByClinicalRecordIdAndDeletedFalse(Long clinicalRecordId);

    @Modifying
    @Query("UPDATE ClinicalRecordAttachment a SET a.deleted = true, a.updatedBy = :user WHERE a.id = :id")
    void softDeleteById(@Param("id") Long id, @Param("user") String user);
}
