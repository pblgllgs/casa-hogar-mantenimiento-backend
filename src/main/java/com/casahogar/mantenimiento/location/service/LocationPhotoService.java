package com.casahogar.mantenimiento.location.service;

import com.casahogar.mantenimiento.common.service.CloudinaryService;
import com.casahogar.mantenimiento.location.dto.LocationPhotoRequest;
import com.casahogar.mantenimiento.location.dto.LocationPhotoResponse;
import com.casahogar.mantenimiento.location.entity.LocationPhoto;
import com.casahogar.mantenimiento.location.repository.LocationPhotoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class LocationPhotoService {

    private final LocationPhotoRepository repository;
    private final CloudinaryService cloudinaryService;

    public LocationPhotoService(LocationPhotoRepository repository, CloudinaryService cloudinaryService) {
        this.repository = repository;
        this.cloudinaryService = cloudinaryService;
    }

    public List<LocationPhotoResponse> getAll() {
        return repository.findAllNotDeleted().stream()
                .map(LocationPhotoResponse::of)
                .collect(Collectors.toList());
    }

    public List<LocationPhotoResponse> getActive() {
        return repository.findAllActive().stream()
                .map(LocationPhotoResponse::of)
                .collect(Collectors.toList());
    }

    @Transactional
    public LocationPhotoResponse create(LocationPhotoRequest request, String currentUser) {
        LocationPhoto entity = new LocationPhoto();
        entity.setTitle(request.getTitle());
        entity.setCategory(request.getCategory());
        entity.setPhotoUrl(request.getPhotoUrl());
        entity.setDisplayOrder(request.getDisplayOrder() != null ? request.getDisplayOrder() : 0);
        entity.setIsActive(true);
        repository.save(entity);
        return LocationPhotoResponse.of(entity);
    }

    @Transactional
    public LocationPhotoResponse update(Long id, LocationPhotoRequest request, String currentUser) {
        LocationPhoto entity = repository.findById(id)
                .filter(e -> !Boolean.TRUE.equals(e.getDeleted()))
                .orElseThrow(() -> new IllegalArgumentException("Foto no encontrada"));

        String oldPhotoUrl = entity.getPhotoUrl();
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

        entity.setTitle(request.getTitle());
        entity.setCategory(request.getCategory());
        entity.setPhotoUrl(newPhotoUrl);
        if (request.getDisplayOrder() != null) {
            entity.setDisplayOrder(request.getDisplayOrder());
        }
        repository.save(entity);
        return LocationPhotoResponse.of(entity);
    }

    @Transactional
    public void delete(Long id, String currentUser) {
        LocationPhoto entity = repository.findById(id)
                .filter(e -> !Boolean.TRUE.equals(e.getDeleted()))
                .orElseThrow(() -> new IllegalArgumentException("Foto no encontrada"));
        if (entity.getPhotoUrl() != null && !entity.getPhotoUrl().isBlank()) {
            String publicId = cloudinaryService.extractPublicId(entity.getPhotoUrl());
            cloudinaryService.deleteImage(publicId);
        }
        entity.softDelete(currentUser);
        repository.save(entity);
    }
}
