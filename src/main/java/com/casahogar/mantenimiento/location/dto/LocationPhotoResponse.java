package com.casahogar.mantenimiento.location.dto;

import com.casahogar.mantenimiento.location.entity.LocationPhoto;

import java.time.LocalDateTime;

public class LocationPhotoResponse {

    private Long id;
    private String title;
    private String category;
    private String photoUrl;
    private Integer displayOrder;
    private Boolean isActive;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static LocationPhotoResponse of(LocationPhoto entity) {
        LocationPhotoResponse r = new LocationPhotoResponse();
        r.id = entity.getId();
        r.title = entity.getTitle();
        r.category = entity.getCategory();
        r.photoUrl = entity.getPhotoUrl();
        r.displayOrder = entity.getDisplayOrder();
        r.isActive = entity.getIsActive();
        r.createdAt = entity.getCreatedAt();
        r.updatedAt = entity.getUpdatedAt();
        return r;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public String getPhotoUrl() { return photoUrl; }
    public void setPhotoUrl(String photoUrl) { this.photoUrl = photoUrl; }

    public Integer getDisplayOrder() { return displayOrder; }
    public void setDisplayOrder(Integer displayOrder) { this.displayOrder = displayOrder; }

    public Boolean getIsActive() { return isActive; }
    public void setIsActive(Boolean isActive) { this.isActive = isActive; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}
