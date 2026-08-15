package com.casahogar.mantenimiento.clinical.service;

import com.casahogar.mantenimiento.clinical.dto.AttachmentRequest;
import com.casahogar.mantenimiento.clinical.dto.AttachmentResponse;
import com.casahogar.mantenimiento.clinical.entity.ClinicalRecordAttachment;
import com.casahogar.mantenimiento.clinical.repository.ClinicalRecordAttachmentRepository;
import com.casahogar.mantenimiento.clinical.repository.ClinicalRecordRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AttachmentService {

    private final ClinicalRecordAttachmentRepository attachmentRepository;
    private final ClinicalRecordRepository clinicalRecordRepository;

    public AttachmentService(ClinicalRecordAttachmentRepository attachmentRepository, ClinicalRecordRepository clinicalRecordRepository) {
        this.attachmentRepository = attachmentRepository;
        this.clinicalRecordRepository = clinicalRecordRepository;
    }

    @Transactional
    public AttachmentResponse create(AttachmentRequest request, String currentUser) {
        clinicalRecordRepository.findById(request.getClinicalRecordId())
                .orElseThrow(() -> new IllegalArgumentException("Registro clínico no encontrado"));

        ClinicalRecordAttachment entity = new ClinicalRecordAttachment();
        entity.setClinicalRecordId(request.getClinicalRecordId());
        entity.setFileUrl(request.getFileUrl());
        entity.setFileName(request.getFileName());
        entity.setFileType(request.getFileType());
        attachmentRepository.save(entity);
        return AttachmentResponse.of(entity);
    }

    @Transactional
    public void delete(Long id, String currentUser) {
        attachmentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Adjunto no encontrado"));
        attachmentRepository.softDeleteById(id, currentUser);
    }

    public List<AttachmentResponse> getByClinicalRecord(Long clinicalRecordId) {
        return attachmentRepository.findByClinicalRecordIdAndDeletedFalse(clinicalRecordId).stream()
                .map(AttachmentResponse::of)
                .collect(Collectors.toList());
    }
}
