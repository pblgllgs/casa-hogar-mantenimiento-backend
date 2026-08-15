package com.casahogar.mantenimiento.maintenance.entity;

import com.casahogar.mantenimiento.common.entity.BaseEntity;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "work_orders")
public class WorkOrder extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "order_number", unique = true, nullable = false, length = 20)
    private String orderNumber;

    @Column(name = "title", nullable = false, length = 200)
    private String title;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false, length = 20)
    private WorkOrderType type;

    @Enumerated(EnumType.STRING)
    @Column(name = "priority", nullable = false, length = 20)
    private Priority priority;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    private WorkOrderStatus status;

    @Column(name = "location_id")
    private Long locationId;

    @Column(name = "location_name", length = 200)
    private String locationName;

    @Column(name = "asset_id")
    private Long assetId;

    @Column(name = "asset_name", length = 200)
    private String assetName;

    @Column(name = "requested_by_id", nullable = false)
    private Long requestedById;

    @Column(name = "requested_by_name", length = 200)
    private String requestedByName;

    @Column(name = "assigned_to_id")
    private Long assignedToId;

    @Column(name = "assigned_to_name", length = 200)
    private String assignedToName;

    @Column(name = "supervisor_id")
    private Long supervisorId;

    @Column(name = "supervisor_name", length = 200)
    private String supervisorName;

    @Column(name = "estimated_hours", precision = 5, scale = 2)
    private BigDecimal estimatedHours;

    @Column(name = "actual_hours", precision = 5, scale = 2)
    private BigDecimal actualHours;

    @Column(name = "scheduled_start_date")
    private LocalDate scheduledStartDate;

    @Column(name = "scheduled_end_date")
    private LocalDate scheduledEndDate;

    @Column(name = "actual_start_date")
    private LocalDateTime actualStartDate;

    @Column(name = "actual_end_date")
    private LocalDateTime actualEndDate;

    @Column(name = "cost_materials", precision = 12, scale = 2)
    private BigDecimal costMaterials;

    @Column(name = "cost_labor", precision = 12, scale = 2)
    private BigDecimal costLabor;

    @Column(name = "cost_total", precision = 12, scale = 2)
    private BigDecimal costTotal;

    @Column(name = "completion_notes", columnDefinition = "TEXT")
    private String completionNotes;

    @Column(name = "requires_external_vendor")
    private Boolean requiresExternalVendor = false;

    @Column(name = "vendor_name", length = 200)
    private String vendorName;

    @Column(name = "vendor_contact", length = 200)
    private String vendorContact;

    public enum WorkOrderType {
        PREVENTIVE, CORRECTIVE, PREDICTIVE, EMERGENCY, INSPECTION, CALIBRATION
    }

    public enum Priority {
        LOW, MEDIUM, HIGH, URGENT, CRITICAL
    }

    public enum WorkOrderStatus {
        PENDING, ASSIGNED, IN_PROGRESS, ON_HOLD, PENDING_REVIEW, COMPLETED, CANCELLED, REOPENED, SCHEDULED
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getOrderNumber() { return orderNumber; }
    public void setOrderNumber(String orderNumber) { this.orderNumber = orderNumber; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public WorkOrderType getType() { return type; }
    public void setType(WorkOrderType type) { this.type = type; }

    public Priority getPriority() { return priority; }
    public void setPriority(Priority priority) { this.priority = priority; }

    public WorkOrderStatus getStatus() { return status; }
    public void setStatus(WorkOrderStatus status) { this.status = status; }

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

    public java.time.LocalDate getScheduledStartDate() { return scheduledStartDate; }
    public void setScheduledStartDate(java.time.LocalDate scheduledStartDate) { this.scheduledStartDate = scheduledStartDate; }

    public java.time.LocalDate getScheduledEndDate() { return scheduledEndDate; }
    public void setScheduledEndDate(java.time.LocalDate scheduledEndDate) { this.scheduledEndDate = scheduledEndDate; }

    public java.time.LocalDateTime getActualStartDate() { return actualStartDate; }
    public void setActualStartDate(java.time.LocalDateTime actualStartDate) { this.actualStartDate = actualStartDate; }

    public java.time.LocalDateTime getActualEndDate() { return actualEndDate; }
    public void setActualEndDate(java.time.LocalDateTime actualEndDate) { this.actualEndDate = actualEndDate; }

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

}