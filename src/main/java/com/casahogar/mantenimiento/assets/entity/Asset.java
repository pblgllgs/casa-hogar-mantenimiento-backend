package com.casahogar.mantenimiento.assets.entity;

import com.casahogar.mantenimiento.common.entity.BaseEntity;
import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "assets")
public class Asset extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "asset_code", unique = true, nullable = false, length = 30)
    private String assetCode;

    @Column(name = "name", nullable = false, length = 200)
    private String name;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "category", nullable = false, length = 30)
    private AssetCategory category;

    @Enumerated(EnumType.STRING)
    @Column(name = "subcategory", length = 30)
    private AssetSubcategory subcategory;

    @Column(name = "brand", length = 100)
    private String brand;

    @Column(name = "model", length = 100)
    private String model;

    @Column(name = "serial_number", length = 100)
    private String serialNumber;

    @Column(name = "manufacture_year")
    private Integer manufactureYear;

    @Column(name = "purchase_date")
    private LocalDate purchaseDate;

    @Column(name = "purchase_cost", precision = 12, scale = 2)
    private java.math.BigDecimal purchaseCost;

    @Column(name = "warranty_expiry_date")
    private LocalDate warrantyExpiryDate;

    @Column(name = "location_id")
    private Long locationId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "location_id", insertable = false, updatable = false)
    private Location location;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    private AssetStatus status = AssetStatus.OPERATIONAL;

    @Enumerated(EnumType.STRING)
    @Column(name = "criticality", length = 20)
    private Criticality criticality = Criticality.MEDIUM;

    @Column(name = "expected_life_years")
    private Integer expectedLifeYears;

    @Column(name = "last_maintenance_date")
    private LocalDate lastMaintenanceDate;

    @Column(name = "next_maintenance_date")
    private LocalDate nextMaintenanceDate;

    @Column(name = "maintenance_interval_days")
    private Integer maintenanceIntervalDays;

    @Column(name = "qr_code", length = 500)
    private String qrCode;

    @Column(name = "manual_url", length = 500)
    private String manualUrl;

    @Column(name = "specifications", columnDefinition = "JSON")
    private String specifications;

    public enum AssetCategory {
        ELECTRICAL, PLUMBING, HVAC, STRUCTURAL, FURNITURE, APPLIANCES, SAFETY,
        MEDICAL, KITCHEN, LAUNDRY, IT_NETWORK, SECURITY, VEHICLES, TOOLS, OTHER,
        FIRE_SAFETY, VEHICLE
    }

    public enum AssetSubcategory {
        GENERATOR, PANEL, WIRING, LIGHTING, OUTLETS, PUMP, PIPE, VALVE, TANK,
        WATER_HEATER, AC_UNIT, HEATER, VENTILATION, DUCTWORK, ROOF, WALL, FLOOR,
        DOOR, WINDOW, BED, MATTRESS, WARDROBE, DESK, CHAIR, TABLE, REFRIGERATOR,
        STOVE, OVEN, MICROWAVE, DISHWASHER, WASHER, DRYER, FIRE_EXTINGUISHER,
        ALARM_SYSTEM, SPRINKLER, EXIT_SIGN, BED_MEDICAL, WHEELCHAIR, MONITOR,
        COMPUTER, PRINTER, SERVER, ROUTER, CAMERA, ACCESS_CONTROL, FIREWALL,
        VAN, TRUCK, CAR, HAND_TOOL, POWER_TOOL, MEASURING_TOOL, SAFETY_GEAR,
        WATER_PUMP, CCTV, PICKUP, EXTINGUISHER, UPS, MINI_SPLIT
    }

    public enum AssetStatus {
        OPERATIONAL, UNDER_MAINTENANCE, REPAIR_NEEDED, OUT_OF_SERVICE,
        DECOMMISSIONED, PENDING_INSPECTION, RETIRED, NEEDS_MAINTENANCE
    }

    public enum Criticality {
        LOW, MEDIUM, HIGH, CRITICAL
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getAssetCode() { return assetCode; }
    public void setAssetCode(String assetCode) { this.assetCode = assetCode; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public AssetCategory getCategory() { return category; }
    public void setCategory(AssetCategory category) { this.category = category; }

    public AssetSubcategory getSubcategory() { return subcategory; }
    public void setSubcategory(AssetSubcategory subcategory) { this.subcategory = subcategory; }

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

    public java.math.BigDecimal getPurchaseCost() { return purchaseCost; }
    public void setPurchaseCost(java.math.BigDecimal purchaseCost) { this.purchaseCost = purchaseCost; }

    public LocalDate getWarrantyExpiryDate() { return warrantyExpiryDate; }
    public void setWarrantyExpiryDate(LocalDate warrantyExpiryDate) { this.warrantyExpiryDate = warrantyExpiryDate; }

    public Long getLocationId() { return locationId; }
    public void setLocationId(Long locationId) { this.locationId = locationId; }

    public Location getLocation() { return location; }
    public void setLocation(Location location) { this.location = location; }

    public AssetStatus getStatus() { return status; }
    public void setStatus(AssetStatus status) { this.status = status; }

    public Criticality getCriticality() { return criticality; }
    public void setCriticality(Criticality criticality) { this.criticality = criticality; }

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

    public String getSpecifications() { return specifications; }
    public void setSpecifications(String specifications) { this.specifications = specifications; }
}
