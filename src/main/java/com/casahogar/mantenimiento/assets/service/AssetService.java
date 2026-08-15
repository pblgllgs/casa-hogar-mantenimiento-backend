package com.casahogar.mantenimiento.assets.service;

import com.casahogar.mantenimiento.assets.dto.AssetRequest;
import com.casahogar.mantenimiento.assets.dto.AssetResponse;
import com.casahogar.mantenimiento.assets.entity.Asset;
import com.casahogar.mantenimiento.assets.entity.Location;
import com.casahogar.mantenimiento.assets.repository.AssetRepository;
import com.casahogar.mantenimiento.assets.repository.LocationRepository;
import com.casahogar.mantenimiento.common.dto.PageResponse;
import com.casahogar.mantenimiento.common.dto.SearchCriteria;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AssetService {

    private final AssetRepository assetRepository;
    private final LocationRepository locationRepository;

    public AssetService(AssetRepository assetRepository, LocationRepository locationRepository) {
        this.assetRepository = assetRepository;
        this.locationRepository = locationRepository;
    }

    @Transactional
    public AssetResponse create(AssetRequest request, String currentUser) {
        if (assetRepository.existsByAssetCode(request.getAssetCode())) {
            throw new IllegalArgumentException("Ya existe un activo con el código: " + request.getAssetCode());
        }

        if (!locationRepository.existsByIdActive(request.getLocationId())) {
            throw new IllegalArgumentException("Ubicación no encontrada");
        }

        Asset asset = new Asset();
        asset.setAssetCode(request.getAssetCode());
        asset.setName(request.getName());
        asset.setDescription(request.getDescription());
        asset.setCategory(Asset.AssetCategory.valueOf(request.getCategory()));
        if (request.getSubcategory() != null) {
            asset.setSubcategory(Asset.AssetSubcategory.valueOf(request.getSubcategory()));
        }
        asset.setBrand(request.getBrand());
        asset.setModel(request.getModel());
        asset.setSerialNumber(request.getSerialNumber());
        asset.setManufactureYear(request.getManufactureYear());
        asset.setPurchaseDate(request.getPurchaseDate());
        asset.setPurchaseCost(request.getPurchaseCost());
        asset.setWarrantyExpiryDate(request.getWarrantyExpiryDate());
        asset.setLocationId(request.getLocationId());
        asset.setStatus(Asset.AssetStatus.valueOf(request.getStatus()));
        if (request.getCriticality() != null) {
            asset.setCriticality(Asset.Criticality.valueOf(request.getCriticality()));
        }
        asset.setExpectedLifeYears(request.getExpectedLifeYears());
        asset.setMaintenanceIntervalDays(request.getMaintenanceIntervalDays());
        asset.setManualUrl(request.getManualUrl());

        assetRepository.save(asset);
        return AssetResponse.of(asset);
    }

    @Transactional
    public AssetResponse update(Long id, AssetRequest request, String currentUser) {
        Asset asset = assetRepository.findByIdActive(id)
                .orElseThrow(() -> new IllegalArgumentException("Activo no encontrado"));

        if (!locationRepository.existsByIdActive(request.getLocationId())) {
            throw new IllegalArgumentException("Ubicación no encontrada");
        }

        asset.setName(request.getName());
        asset.setDescription(request.getDescription());
        asset.setCategory(Asset.AssetCategory.valueOf(request.getCategory()));
        if (request.getSubcategory() != null) {
            asset.setSubcategory(Asset.AssetSubcategory.valueOf(request.getSubcategory()));
        }
        asset.setBrand(request.getBrand());
        asset.setModel(request.getModel());
        asset.setSerialNumber(request.getSerialNumber());
        asset.setManufactureYear(request.getManufactureYear());
        asset.setPurchaseDate(request.getPurchaseDate());
        asset.setPurchaseCost(request.getPurchaseCost());
        asset.setWarrantyExpiryDate(request.getWarrantyExpiryDate());
        asset.setLocationId(request.getLocationId());
        asset.setStatus(Asset.AssetStatus.valueOf(request.getStatus()));
        if (request.getCriticality() != null) {
            asset.setCriticality(Asset.Criticality.valueOf(request.getCriticality()));
        }
        asset.setExpectedLifeYears(request.getExpectedLifeYears());
        asset.setMaintenanceIntervalDays(request.getMaintenanceIntervalDays());
        asset.setManualUrl(request.getManualUrl());

        assetRepository.save(asset);
        return AssetResponse.of(asset);
    }

    @Transactional
    public void delete(Long id, String currentUser) {
        assetRepository.softDeleteById(id, currentUser);
    }

    public AssetResponse getById(Long id) {
        Asset asset = assetRepository.findByIdActive(id)
                .orElseThrow(() -> new IllegalArgumentException("Activo no encontrado"));
        return AssetResponse.of(asset);
    }

    public PageResponse<AssetResponse> search(SearchCriteria criteria) {
        Pageable pageable = criteria.toPageRequest();
        Page<Asset> page = (criteria.getSearch() != null && !criteria.getSearch().isBlank())
                ? assetRepository.search(criteria.getSearch(), pageable)
                : assetRepository.findAllByDeletedFalse(pageable);
        return PageResponse.of(page.map(AssetResponse::of));
    }

    public List<AssetResponse> getByStatus(String status) {
        return assetRepository.findByStatus(Asset.AssetStatus.valueOf(status)).stream()
                .filter(a -> !Boolean.TRUE.equals(a.getDeleted()))
                .map(AssetResponse::of)
                .collect(Collectors.toList());
    }

    public List<AssetResponse> getByLocation(Long locationId) {
        return assetRepository.findByLocationId(locationId).stream()
                .filter(a -> !Boolean.TRUE.equals(a.getDeleted()))
                .map(AssetResponse::of)
                .collect(Collectors.toList());
    }

    public List<AssetResponse> getDueForMaintenance() {
        return assetRepository.findDueForMaintenance(LocalDate.now()).stream()
                .map(AssetResponse::of)
                .collect(Collectors.toList());
    }
}
