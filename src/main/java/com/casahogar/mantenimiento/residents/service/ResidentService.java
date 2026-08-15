package com.casahogar.mantenimiento.residents.service;

import com.casahogar.mantenimiento.assets.repository.LocationRepository;
import com.casahogar.mantenimiento.common.dto.PageResponse;
import com.casahogar.mantenimiento.common.service.CloudinaryService;
import com.casahogar.mantenimiento.residents.dto.ResidentRequest;
import com.casahogar.mantenimiento.residents.dto.ResidentResponse;
import com.casahogar.mantenimiento.residents.entity.Resident;
import com.casahogar.mantenimiento.residents.repository.ResidentRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ResidentService {

    private final ResidentRepository residentRepository;
    private final LocationRepository locationRepository;
    private final CloudinaryService cloudinaryService;

    public ResidentService(ResidentRepository residentRepository, LocationRepository locationRepository, CloudinaryService cloudinaryService) {
        this.residentRepository = residentRepository;
        this.locationRepository = locationRepository;
        this.cloudinaryService = cloudinaryService;
    }

    @Transactional
    public ResidentResponse create(ResidentRequest request, String currentUser) {
        if (residentRepository.existsByCode(request.getCode())) {
            throw new IllegalArgumentException("Ya existe un residente con el código: " + request.getCode());
        }
        if (residentRepository.existsByDocumentNumber(request.getDocumentNumber())) {
            throw new IllegalArgumentException("Ya existe un residente con el número de documento: " + request.getDocumentNumber());
        }

        Resident resident = new Resident();
        resident.setCode(request.getCode());
        resident.setFirstName(request.getFirstName());
        resident.setLastName(request.getLastName());
        resident.setDocumentType(Resident.DocumentType.valueOf(request.getDocumentType()));
        resident.setDocumentNumber(request.getDocumentNumber());
        resident.setBirthDate(request.getBirthDate());
        resident.setGender(request.getGender());
        resident.setEntryDate(request.getEntryDate());
        resident.setStatus(Resident.ResidentStatus.valueOf(request.getStatus()));
        resident.setRoomId(request.getRoomId());
        resident.setGuardianName(request.getGuardianName());
        resident.setGuardianPhone(request.getGuardianPhone());
        resident.setGuardianEmail(request.getGuardianEmail());
        resident.setGuardianRelationship(request.getGuardianRelationship());
        resident.setMedicalInfo(request.getMedicalInfo());
        resident.setDietaryRestrictions(request.getDietaryRestrictions());
        resident.setNotes(request.getNotes());
        resident.setPhotoUrl(request.getPhotoUrl());
        resident.setIsActive(true);

        residentRepository.save(resident);
        return ResidentResponse.of(resident);
    }

    @Transactional
    public ResidentResponse update(Long id, ResidentRequest request, String currentUser) {
        Resident resident = residentRepository.findByIdActive(id)
                .orElseThrow(() -> new IllegalArgumentException("Residente no encontrado"));

        String oldPhotoUrl = resident.getPhotoUrl();
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

        resident.setFirstName(request.getFirstName());
        resident.setLastName(request.getLastName());
        resident.setDocumentType(Resident.DocumentType.valueOf(request.getDocumentType()));
        resident.setDocumentNumber(request.getDocumentNumber());
        resident.setBirthDate(request.getBirthDate());
        resident.setGender(request.getGender());
        resident.setEntryDate(request.getEntryDate());
        resident.setExitDate(resident.getExitDate());
        resident.setStatus(Resident.ResidentStatus.valueOf(request.getStatus()));
        resident.setRoomId(request.getRoomId());
        resident.setGuardianName(request.getGuardianName());
        resident.setGuardianPhone(request.getGuardianPhone());
        resident.setGuardianEmail(request.getGuardianEmail());
        resident.setGuardianRelationship(request.getGuardianRelationship());
        resident.setMedicalInfo(request.getMedicalInfo());
        resident.setDietaryRestrictions(request.getDietaryRestrictions());
        resident.setNotes(request.getNotes());
        resident.setPhotoUrl(newPhotoUrl);

        residentRepository.save(resident);
        return ResidentResponse.of(resident);
    }

    @Transactional
    public void delete(Long id, String currentUser) {
        Resident resident = residentRepository.findByIdActive(id)
                .orElseThrow(() -> new IllegalArgumentException("Residente no encontrado"));
        if (resident.getPhotoUrl() != null && !resident.getPhotoUrl().isBlank()) {
            String publicId = cloudinaryService.extractPublicId(resident.getPhotoUrl());
            cloudinaryService.deleteImage(publicId);
        }
        residentRepository.softDeleteById(id, currentUser);
    }

    public ResidentResponse getById(Long id) {
        Resident resident = residentRepository.findByIdActive(id)
                .orElseThrow(() -> new IllegalArgumentException("Residente no encontrado"));
        return ResidentResponse.of(resident);
    }

    public PageResponse<ResidentResponse> search(String search, Pageable pageable) {
        if (search != null && !search.isBlank()) {
            return PageResponse.of(residentRepository.searchPaged(search, pageable).map(ResidentResponse::of));
        }
        return PageResponse.of(residentRepository.findAllActivePaged(pageable).map(ResidentResponse::of));
    }

    public List<ResidentResponse> getAll() {
        return residentRepository.findAllActive().stream()
                .map(ResidentResponse::of)
                .collect(Collectors.toList());
    }

    public List<ResidentResponse> getByStatus(String status) {
        return residentRepository.findByStatus(Resident.ResidentStatus.valueOf(status)).stream()
                .filter(r -> !Boolean.TRUE.equals(r.getDeleted()))
                .map(ResidentResponse::of)
                .collect(Collectors.toList());
    }

    public List<ResidentResponse> getByRoom(Long roomId) {
        return residentRepository.findByRoomId(roomId).stream()
                .filter(r -> !Boolean.TRUE.equals(r.getDeleted()))
                .map(ResidentResponse::of)
                .collect(Collectors.toList());
    }

    public List<ResidentResponse> search(String query) {
        return residentRepository.search(query).stream()
                .map(ResidentResponse::of)
                .collect(Collectors.toList());
    }
}
