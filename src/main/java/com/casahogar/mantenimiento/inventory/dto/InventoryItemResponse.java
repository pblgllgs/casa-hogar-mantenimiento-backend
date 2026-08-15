package com.casahogar.mantenimiento.inventory.dto;

import com.casahogar.mantenimiento.inventory.entity.InventoryItem;
import com.casahogar.mantenimiento.inventory.entity.InventoryItemCategory;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class InventoryItemResponse {

    private Long id;
    private String code;
    private String name;
    private String description;
    private InventoryItemCategory category;
    private String unitOfMeasure;
    private BigDecimal currentStock;
    private BigDecimal minimumStock;
    private BigDecimal maximumStock;
    private BigDecimal reorderPoint;
    private BigDecimal unitCost;
    private Long locationId;
    private String locationName;
    private String supplierName;
    private String supplierContact;
    private String supplierSku;
    private LocalDate lastPurchaseDate;
    private BigDecimal lastPurchaseCost;
    private Boolean isActive;
    private LocalDateTime createdAt;
    private String createdBy;
    private LocalDateTime updatedAt;

    public static InventoryItemResponse of(InventoryItem item) {
        InventoryItemResponse r = new InventoryItemResponse();
        r.id = item.getId();
        r.code = item.getCode();
        r.name = item.getName();
        r.description = item.getDescription();
        r.category = item.getCategory();
        r.unitOfMeasure = item.getUnitOfMeasure();
        r.currentStock = item.getCurrentStock();
        r.minimumStock = item.getMinimumStock();
        r.maximumStock = item.getMaximumStock();
        r.reorderPoint = item.getReorderPoint();
        r.unitCost = item.getUnitCost();
        r.locationId = item.getLocationId();
        r.locationName = item.getLocation() != null ? item.getLocation().getName() : null;
        r.supplierName = item.getSupplierName();
        r.supplierContact = item.getSupplierContact();
        r.supplierSku = item.getSupplierSku();
        r.lastPurchaseDate = item.getLastPurchaseDate();
        r.lastPurchaseCost = item.getLastPurchaseCost();
        r.isActive = item.getIsActive();
        r.createdAt = item.getCreatedAt();
        r.createdBy = item.getCreatedBy();
        r.updatedAt = item.getUpdatedAt();
        return r;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public InventoryItemCategory getCategory() { return category; }
    public void setCategory(InventoryItemCategory category) { this.category = category; }

    public String getUnitOfMeasure() { return unitOfMeasure; }
    public void setUnitOfMeasure(String unitOfMeasure) { this.unitOfMeasure = unitOfMeasure; }

    public BigDecimal getCurrentStock() { return currentStock; }
    public void setCurrentStock(BigDecimal currentStock) { this.currentStock = currentStock; }

    public BigDecimal getMinimumStock() { return minimumStock; }
    public void setMinimumStock(BigDecimal minimumStock) { this.minimumStock = minimumStock; }

    public BigDecimal getMaximumStock() { return maximumStock; }
    public void setMaximumStock(BigDecimal maximumStock) { this.maximumStock = maximumStock; }

    public BigDecimal getReorderPoint() { return reorderPoint; }
    public void setReorderPoint(BigDecimal reorderPoint) { this.reorderPoint = reorderPoint; }

    public BigDecimal getUnitCost() { return unitCost; }
    public void setUnitCost(BigDecimal unitCost) { this.unitCost = unitCost; }

    public Long getLocationId() { return locationId; }
    public void setLocationId(Long locationId) { this.locationId = locationId; }

    public String getLocationName() { return locationName; }
    public void setLocationName(String locationName) { this.locationName = locationName; }

    public String getSupplierName() { return supplierName; }
    public void setSupplierName(String supplierName) { this.supplierName = supplierName; }

    public String getSupplierContact() { return supplierContact; }
    public void setSupplierContact(String supplierContact) { this.supplierContact = supplierContact; }

    public String getSupplierSku() { return supplierSku; }
    public void setSupplierSku(String supplierSku) { this.supplierSku = supplierSku; }

    public LocalDate getLastPurchaseDate() { return lastPurchaseDate; }
    public void setLastPurchaseDate(LocalDate lastPurchaseDate) { this.lastPurchaseDate = lastPurchaseDate; }

    public BigDecimal getLastPurchaseCost() { return lastPurchaseCost; }
    public void setLastPurchaseCost(BigDecimal lastPurchaseCost) { this.lastPurchaseCost = lastPurchaseCost; }

    public Boolean getIsActive() { return isActive; }
    public void setIsActive(Boolean isActive) { this.isActive = isActive; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public String getCreatedBy() { return createdBy; }
    public void setCreatedBy(String createdBy) { this.createdBy = createdBy; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}
