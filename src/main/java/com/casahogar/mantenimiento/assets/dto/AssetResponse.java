package com.casahogar.mantenimiento.assets.dto;

import com.casahogar.mantenimiento.assets.entity.Asset;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class AssetResponse {

    private Long id;
    private String assetCode;
    private String name;
    private String description;
    private String category;
    private String subcategory;
    private String brand;
    private String model;
    private String serialNumber;
    private Integer manufactureYear;
    private LocalDate purchaseDate;
    private BigDecimal purchaseCost;
    private LocalDate warrantyExpiryDate;
    private Long locationId;
    private String locationName;
    private String status;
    private String criticality;
    private Integer expectedLifeYears;
    private LocalDate lastMaintenanceDate;
    private LocalDate nextMaintenanceDate;
    private Integer maintenanceIntervalDays;
    private String qrCode;
    private String manualUrl;
    private LocalDateTime createdAt;
    private String createdBy;
    private LocalDateTime updatedAt;

    public static AssetResponse of(Asset asset) {
        AssetResponse r = new AssetResponse();
        r.id = asset.getId();
        r.assetCode = asset.getAssetCode();
        r.name = asset.getName();
        r.description = asset.getDescription();
        r.category = asset.getCategory() != null ? asset.getCategory().name() : null;
        r.subcategory = asset.getSubcategory() != null ? asset.getSubcategory().name() : null;
        r.brand = asset.getBrand();
        r.model = asset.getModel();
        r.serialNumber = asset.getSerialNumber();
        r.manufactureYear = asset.getManufactureYear();
        r.purchaseDate = asset.getPurchaseDate();
        r.purchaseCost = asset.getPurchaseCost();
        r.warrantyExpiryDate = asset.getWarrantyExpiryDate();
        r.locationId = asset.getLocationId();
        r.locationName = asset.getLocation() != null ? asset.getLocation().getName() : null;
        r.status = asset.getStatus() != null ? asset.getStatus().name() : null;
        r.criticality = asset.getCriticality() != null ? asset.getCriticality().name() : null;
        r.expectedLifeYears = asset.getExpectedLifeYears();
        r.lastMaintenanceDate = asset.getLastMaintenanceDate();
        r.nextMaintenanceDate = asset.getNextMaintenanceDate();
        r.maintenanceIntervalDays = asset.getMaintenanceIntervalDays();
        r.qrCode = asset.getQrCode();
        r.manualUrl = asset.getManualUrl();
        r.createdAt = asset.getCreatedAt();
        r.createdBy = asset.getCreatedBy();
        r.updatedAt = asset.getUpdatedAt();
        return r;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getAssetCode() { return assetCode; }
    public void setAssetCode(String assetCode) { this.assetCode = assetCode; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public String getSubcategory() { return subcategory; }
    public void setSubcategory(String subcategory) { this.subcategory = subcategory; }

    public String getBrand() { return brand; }
    public void setBrand(String brand) { this.brand = brand; }

    public String getModel() { return model; }
    public void setModel(String model) { this.model = model; }

    public String getSerialNumber() { return serialNumber; }
    public void setSerialNumber(String serialNumber) { this.serialNumber = serialNumber; }

    public Integer getManufactureYear() { return manufactureYear; }
    public void setManufactureYear(Integer manufactureYear) { this.manufactureYear = manufactureYear; }

    public LocalDate getPurchaseDate() { return purchaseDate; }
    public void setPurchaseDate(LocalDate purchaseDate) { this.purchaseDate = purchaseDate; }

    public BigDecimal getPurchaseCost() { return purchaseCost; }
    public void setPurchaseCost(BigDecimal purchaseCost) { this.purchaseCost = purchaseCost; }

    public LocalDate getWarrantyExpiryDate() { return warrantyExpiryDate; }
    public void setWarrantyExpiryDate(LocalDate warrantyExpiryDate) { this.warrantyExpiryDate = warrantyExpiryDate; }

    public Long getLocationId() { return locationId; }
    public void setLocationId(Long locationId) { this.locationId = locationId; }

    public String getLocationName() { return locationName; }
    public void setLocationName(String locationName) { this.locationName = locationName; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getCriticality() { return criticality; }
    public void setCriticality(String criticality) { this.criticality = criticality; }

    public Integer getExpectedLifeYears() { return expectedLifeYears; }
    public void setExpectedLifeYears(Integer expectedLifeYears) { this.expectedLifeYears = expectedLifeYears; }

    public LocalDate getLastMaintenanceDate() { return lastMaintenanceDate; }
    public void setLastMaintenanceDate(LocalDate lastMaintenanceDate) { this.lastMaintenanceDate = lastMaintenanceDate; }

    public LocalDate getNextMaintenanceDate() { return nextMaintenanceDate; }
    public void setNextMaintenanceDate(LocalDate nextMaintenanceDate) { this.nextMaintenanceDate = nextMaintenanceDate; }

    public Integer getMaintenanceIntervalDays() { return maintenanceIntervalDays; }
    public void setMaintenanceIntervalDays(Integer maintenanceIntervalDays) { this.maintenanceIntervalDays = maintenanceIntervalDays; }

    public String getQrCode() { return qrCode; }
    public void setQrCode(String qrCode) { this.qrCode = qrCode; }

    public String getManualUrl() { return manualUrl; }
    public void setManualUrl(String manualUrl) { this.manualUrl = manualUrl; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public String getCreatedBy() { return createdBy; }
    public void setCreatedBy(String createdBy) { this.createdBy = createdBy; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}
