package com.casahogar.mantenimiento.maintenance.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.time.LocalDate;

public class WorkOrderRequest {
    @NotBlank
    @Size(max = 200)
    private String title;

    @Size(max = 5000)
    private String description;

    @NotNull
    private WorkOrderType type;

    @NotNull
    private Priority priority;

    private Long locationId;
    private String locationName;

    private Long assetId;
    private String assetName;

    private Long requestedById;

    private Long assignedToId;
    private String assignedToName;

    private Long supervisorId;
    private String supervisorName;

    private BigDecimal estimatedHours;

    private LocalDate scheduledStartDate;
    private LocalDate scheduledEndDate;

    private BigDecimal costMaterials;
    private BigDecimal costLabor;

    private String completionNotes;

    private Boolean requiresExternalVendor = false;
    private String vendorName;
    private String vendorContact;

    public enum WorkOrderType {
        PREVENTIVE, CORRECTIVE, PREDICTIVE, EMERGENCY, INSPECTION, CALIBRATION
    }

    public enum Priority {
        LOW, MEDIUM, HIGH, URGENT, CRITICAL
    }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public WorkOrderType getType() { return type; }
    public void setType(WorkOrderType type) { this.type = type; }

    public Priority getPriority() { return priority; }
    public void setPriority(Priority priority) { this.priority = priority; }

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

    public LocalDate getScheduledStartDate() { return scheduledStartDate; }
    public void setScheduledStartDate(LocalDate scheduledStartDate) { this.scheduledStartDate = scheduledStartDate; }

    public LocalDate getScheduledEndDate() { return scheduledEndDate; }
    public void setScheduledEndDate(LocalDate scheduledEndDate) { this.scheduledEndDate = scheduledEndDate; }

    public BigDecimal getCostMaterials() { return costMaterials; }
    public void setCostMaterials(BigDecimal costMaterials) { this.costMaterials = costMaterials; }

    public BigDecimal getCostLabor() { return costLabor; }
    public void setCostLabor(BigDecimal costLabor) { this.costLabor = costLabor; }

    public String getCompletionNotes() { return completionNotes; }
    public void setCompletionNotes(String completionNotes) { this.completionNotes = completionNotes; }

    public Boolean getRequiresExternalVendor() { return requiresExternalVendor; }
    public void setRequiresExternalVendor(Boolean requiresExternalVendor) { this.requiresExternalVendor = requiresExternalVendor; }

    public String getVendorName() { return vendorName; }
    public void setVendorName(String vendorName) { this.vendorName = vendorName; }

    public String getVendorContact() { return vendorContact; }
    public void setVendorContact(String vendorContact) { this.vendorContact = vendorContact; }
}
