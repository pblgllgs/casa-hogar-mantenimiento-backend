package com.casahogar.mantenimiento.assets.service;

import com.casahogar.mantenimiento.assets.dto.LocationRequest;
import com.casahogar.mantenimiento.assets.dto.LocationResponse;
import com.casahogar.mantenimiento.assets.entity.Location;
import com.casahogar.mantenimiento.assets.repository.LocationRepository;
import com.casahogar.mantenimiento.common.dto.PageResponse;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class LocationService {

    private final LocationRepository locationRepository;

    public LocationService(LocationRepository locationRepository) {
        this.locationRepository = locationRepository;
    }

    @Transactional
    public LocationResponse create(LocationRequest request, String currentUser) {
        if (locationRepository.existsByCode(request.getCode())) {
            throw new IllegalArgumentException("Ya existe una ubicación con el código: " + request.getCode());
        }

        Location location = new Location();
        location.setCode(request.getCode());
        location.setName(request.getName());
        location.setDescription(request.getDescription());
        location.setType(Location.LocationType.valueOf(request.getType()));
        location.setParentId(request.getParentId());
        location.setFloor(request.getFloor());
        location.setWing(request.getWing());
        location.setRoomNumber(request.getRoomNumber());
        location.setCapacity(request.getCapacity());
        location.setAreaSqm(request.getAreaSqm());
        location.setIsActive(true);
        location.setMapX(request.getMapX());
        location.setMapY(request.getMapY());
        location.setMapImage(request.getMapImage());

        locationRepository.save(location);
        return LocationResponse.of(location);
    }

    @Transactional
    public LocationResponse update(Long id, LocationRequest request, String currentUser) {
        Location location = locationRepository.findByIdActive(id)
                .orElseThrow(() -> new IllegalArgumentException("Ubicación no encontrada"));

        location.setName(request.getName());
        location.setDescription(request.getDescription());
        location.setType(Location.LocationType.valueOf(request.getType()));
        location.setParentId(request.getParentId());
        location.setFloor(request.getFloor());
        location.setWing(request.getWing());
        location.setRoomNumber(request.getRoomNumber());
        location.setCapacity(request.getCapacity());
        location.setAreaSqm(request.getAreaSqm());
        location.setMapX(request.getMapX());
        location.setMapY(request.getMapY());
        location.setMapImage(request.getMapImage());

        locationRepository.save(location);
        return LocationResponse.of(location);
    }

    @Transactional
    public void delete(Long id, String currentUser) {
        locationRepository.softDeleteById(id, currentUser);
    }

    public LocationResponse getById(Long id) {
        Location location = locationRepository.findByIdActive(id)
                .orElseThrow(() -> new IllegalArgumentException("Ubicación no encontrada"));
        return LocationResponse.of(location);
    }

    public List<LocationResponse> getAll() {
        return locationRepository.findAllActive().stream()
                .map(LocationResponse::of)
                .collect(Collectors.toList());
    }

    public List<LocationResponse> getByType(String type) {
        return locationRepository.findByType(Location.LocationType.valueOf(type)).stream()
                .filter(l -> !Boolean.TRUE.equals(l.getDeleted()))
                .map(LocationResponse::of)
                .collect(Collectors.toList());
    }

    public List<LocationResponse> getByParentId(Long parentId) {
        return locationRepository.findByParentId(parentId).stream()
                .filter(l -> !Boolean.TRUE.equals(l.getDeleted()))
                .map(LocationResponse::of)
                .collect(Collectors.toList());
    }

    public List<LocationResponse> search(String query) {
        return locationRepository.search(query).stream()
                .map(LocationResponse::of)
                .collect(Collectors.toList());
    }

    public PageResponse<LocationResponse> searchPaged(String search, Pageable pageable) {
        if (search != null && !search.isBlank()) {
            return PageResponse.of(locationRepository.searchPaged(search, pageable).map(LocationResponse::of));
        }
        return PageResponse.of(locationRepository.findAllActivePaged(pageable).map(LocationResponse::of));
    }
}
