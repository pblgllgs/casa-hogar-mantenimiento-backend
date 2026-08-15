package com.casahogar.mantenimiento.assets.dto;

import com.casahogar.mantenimiento.assets.entity.Location;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class LocationResponse {

    private Long id;
    private String code;
    private String name;
    private String description;
    private String type;
    private Long parentId;
    private String parentName;
    private String floor;
    private String wing;
    private String roomNumber;
    private Integer capacity;
    private BigDecimal areaSqm;
    private Boolean isActive;
    private LocalDateTime createdAt;
    private String createdBy;
    private LocalDateTime updatedAt;
    private BigDecimal mapX;
    private BigDecimal mapY;
    private String mapImage;

    public static LocationResponse of(Location location) {
        LocationResponse r = new LocationResponse();
        r.id = location.getId();
        r.code = location.getCode();
        r.name = location.getName();
        r.description = location.getDescription();
        r.type = location.getType() != null ? location.getType().name() : null;
        r.parentId = location.getParentId();
        r.parentName = location.getParent() != null ? location.getParent().getName() : null;
        r.floor = location.getFloor();
        r.wing = location.getWing();
        r.roomNumber = location.getRoomNumber();
        r.capacity = location.getCapacity();
        r.areaSqm = location.getAreaSqm();
        r.isActive = location.getIsActive();
        r.createdAt = location.getCreatedAt();
        r.createdBy = location.getCreatedBy();
        r.updatedAt = location.getUpdatedAt();
        r.mapX = location.getMapX();
        r.mapY = location.getMapY();
        r.mapImage = location.getMapImage();
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

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public Long getParentId() { return parentId; }
    public void setParentId(Long parentId) { this.parentId = parentId; }

    public String getParentName() { return parentName; }
    public void setParentName(String parentName) { this.parentName = parentName; }

    public String getFloor() { return floor; }
    public void setFloor(String floor) { this.floor = floor; }

    public String getWing() { return wing; }
    public void setWing(String wing) { this.wing = wing; }

    public String getRoomNumber() { return roomNumber; }
    public void setRoomNumber(String roomNumber) { this.roomNumber = roomNumber; }

    public Integer getCapacity() { return capacity; }
    public void setCapacity(Integer capacity) { this.capacity = capacity; }

    public BigDecimal getAreaSqm() { return areaSqm; }
    public void setAreaSqm(BigDecimal areaSqm) { this.areaSqm = areaSqm; }

    public Boolean getIsActive() { return isActive; }
    public void setIsActive(Boolean isActive) { this.isActive = isActive; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public String getCreatedBy() { return createdBy; }
    public void setCreatedBy(String createdBy) { this.createdBy = createdBy; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

    public BigDecimal getMapX() { return mapX; }
    public void setMapX(BigDecimal mapX) { this.mapX = mapX; }

    public BigDecimal getMapY() { return mapY; }
    public void setMapY(BigDecimal mapY) { this.mapY = mapY; }

    public String getMapImage() { return mapImage; }
    public void setMapImage(String mapImage) { this.mapImage = mapImage; }
}
