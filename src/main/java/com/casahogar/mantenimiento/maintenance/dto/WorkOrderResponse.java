package com.casahogar.mantenimiento.maintenance.dto;

import com.casahogar.mantenimiento.maintenance.entity.WorkOrder;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class WorkOrderResponse {
    private Long id;
    private String orderNumber;
    private String title;
    private String description;
    private String type;
    private String priority;
    private String status;
    private Long locationId;
    private String locationName;
    private Long assetId;
    private String assetName;
    private Long requestedById;
    private String requestedByName;
    private Long assignedToId;
    private String assignedToName;
    private Long supervisorId;
    private String supervisorName;
    private BigDecimal estimatedHours;
    private BigDecimal actualHours;
    private LocalDate scheduledStartDate;
    private LocalDate scheduledEndDate;
    private LocalDateTime actualStartDate;
    private LocalDateTime actualEndDate;
    private BigDecimal costMaterials;
    private BigDecimal costLabor;
    private BigDecimal costTotal;
    private String completionNotes;
    private Boolean requiresExternalVendor;
    private String vendorName;
    private String vendorContact;
    private LocalDateTime createdAt;
    private String createdBy;
    private LocalDateTime updatedAt;

    public static WorkOrderResponse of(WorkOrder wo) {
        WorkOrderResponse r = new WorkOrderResponse();
        r.id = wo.getId();
        r.orderNumber = wo.getOrderNumber();
        r.title = wo.getTitle();
        r.description = wo.getDescription();
        r.type = wo.getType() != null ? wo.getType().name() : null;
        r.priority = wo.getPriority() != null ? wo.getPriority().name() : null;
        r.status = wo.getStatus() != null ? wo.getStatus().name() : null;
        r.locationId = wo.getLocationId();
        r.locationName = wo.getLocationName();
        r.assetId = wo.getAssetId();
        r.assetName = wo.getAssetName();
        r.requestedById = wo.getRequestedById();
        r.requestedByName = wo.getRequestedByName();
        r.assignedToId = wo.getAssignedToId();
        r.assignedToName = wo.getAssignedToName();
        r.supervisorId = wo.getSupervisorId();
        r.supervisorName = wo.getSupervisorName();
        r.estimatedHours = wo.getEstimatedHours();
        r.actualHours = wo.getActualHours();
        r.scheduledStartDate = wo.getScheduledStartDate();
        r.scheduledEndDate = wo.getScheduledEndDate();
        r.actualStartDate = wo.getActualStartDate();
        r.actualEndDate = wo.getActualEndDate();
        r.costMaterials = wo.getCostMaterials();
        r.costLabor = wo.getCostLabor();
        r.costTotal = wo.getCostTotal();
        r.completionNotes = wo.getCompletionNotes();
        r.requiresExternalVendor = wo.getRequiresExternalVendor();
        r.vendorName = wo.getVendorName();
        r.vendorContact = wo.getVendorContact();
        r.createdAt = wo.getCreatedAt();
        r.createdBy = wo.getCreatedBy();
        r.updatedAt = wo.getUpdatedAt();
        return r;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getOrderNumber() { return orderNumber; }
    public void setOrderNumber(String orderNumber) { this.orderNumber = orderNumber; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public String getPriority() { return priority; }
    public void setPriority(String priority) { this.priority = priority; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public Long getLocationId() { return locationId; }
    public void setLocationId(Long locationId) { this.locationId = locationId; }

    public String getLocationName() { return locationName; }
    public void setLocationName(String locationName) { this.locationName = locationName; }

    public Long getAssetId() { return assetId; }
    public void setAssetId(Long assetId) { this.assetId = assetId; }

    public String getAssetName() { return assetName; }
    public void setAssetName(String assetName) { this.assetName = assetName; }

    public Long getRequestedById() { return requestedById; }
    public void setRequestedById(Long requestedById) { this.requestedById = requestedById; }

    public String getRequestedByName() { return requestedByName; }
    public void setRequestedByName(String requestedByName) { this.requestedByName = requestedByName; }

    public Long getAssignedToId() { return assignedToId; }
    public void setAssignedToId(Long assignedToId) { this.assignedToId = assignedToId; }

    public String getAssignedToName() { return assignedToName; }
    public void setAssignedToName(String assignedToName) { this.assignedToName = assignedToName; }

    public Long getSupervisorId() { return supervisorId; }
    public void setSupervisorId(Long supervisorId) { this.supervisorId = supervisorId; }

    public String getSupervisorName() { return supervisorName; }
    public void setSupervisorName(String supervisorName) { this.supervisorName = supervisorName; }

    public BigDecimal getEstimatedHours() { return estimatedHours; }
    public void setEstimatedHours(BigDecimal estimatedHours) { this.estimatedHours = estimatedHours; }

    public BigDecimal getActualHours() { return actualHours; }
    public void setActualHours(BigDecimal actualHours) { this.actualHours = actualHours; }

    public LocalDate getScheduledStartDate() { return scheduledStartDate; }
    public void setScheduledStartDate(LocalDate scheduledStartDate) { this.scheduledStartDate = scheduledStartDate; }

    public LocalDate getScheduledEndDate() { return scheduledEndDate; }
    public void setScheduledEndDate(LocalDate scheduledEndDate) { this.scheduledEndDate = scheduledEndDate; }

    public LocalDateTime getActualStartDate() { return actualStartDate; }
    public void setActualStartDate(LocalDateTime actualStartDate) { this.actualStartDate = actualStartDate; }

    public LocalDateTime getActualEndDate() { return actualEndDate; }
    public void setActualEndDate(LocalDateTime actualEndDate) { this.actualEndDate = actualEndDate; }

    public BigDecimal getCostMaterials() { return costMaterials; }
    public void setCostMaterials(BigDecimal costMaterials) { this.costMaterials = costMaterials; }

    public BigDecimal getCostLabor() { return costLabor; }
    public void setCostLabor(BigDecimal costLabor) { this.costLabor = costLabor; }

    public BigDecimal getCostTotal() { return costTotal; }
    public void setCostTotal(BigDecimal costTotal) { this.costTotal = costTotal; }

    public String getCompletionNotes() { return completionNotes; }
    public void setCompletionNotes(String completionNotes) { this.completionNotes = completionNotes; }

    public Boolean getRequiresExternalVendor() { return requiresExternalVendor; }
    public void setRequiresExternalVendor(Boolean requiresExternalVendor) { this.requiresExternalVendor = requiresExternalVendor; }

    public String getVendorName() { return vendorName; }
    public void setVendorName(String vendorName) { this.vendorName = vendorName; }

    public String getVendorContact() { return vendorContact; }
    public void setVendorContact(String vendorContact) { this.vendorContact = vendorContact; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public String getCreatedBy() { return createdBy; }
    public void setCreatedBy(String createdBy) { this.createdBy = createdBy; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}
