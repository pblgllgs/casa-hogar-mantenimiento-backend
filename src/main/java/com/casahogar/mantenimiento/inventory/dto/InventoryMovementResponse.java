package com.casahogar.mantenimiento.inventory.dto;

import com.casahogar.mantenimiento.inventory.entity.InventoryMovement;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class InventoryMovementResponse {

    private Long id;
    private Long inventoryItemId;
    private String inventoryItemName;
    private String movementType;
    private BigDecimal quantity;
    private BigDecimal unitCost;
    private BigDecimal totalCost;
    private String referenceType;
    private Long referenceId;
    private String notes;
    private Long performedById;
    private String performedByName;
    private LocalDateTime movementDate;
    private LocalDateTime createdAt;

    public static InventoryMovementResponse of(InventoryMovement movement) {
        InventoryMovementResponse r = new InventoryMovementResponse();
        r.id = movement.getId();
        r.inventoryItemId = movement.getInventoryItemId();
        r.inventoryItemName = movement.getInventoryItem() != null ? movement.getInventoryItem().getName() : null;
        r.movementType = movement.getMovementType() != null ? movement.getMovementType().name() : null;
        r.quantity = movement.getQuantity();
        r.unitCost = movement.getUnitCost();
        r.totalCost = movement.getTotalCost();
        r.referenceType = movement.getReferenceType();
        r.referenceId = movement.getReferenceId();
        r.notes = movement.getNotes();
        r.performedById = movement.getPerformedById();
        r.performedByName = movement.getPerformedByName();
        r.movementDate = movement.getMovementDate();
        r.createdAt = movement.getCreatedAt();
        return r;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getInventoryItemId() { return inventoryItemId; }
    public void setInventoryItemId(Long inventoryItemId) { this.inventoryItemId = inventoryItemId; }

    public String getInventoryItemName() { return inventoryItemName; }
    public void setInventoryItemName(String inventoryItemName) { this.inventoryItemName = inventoryItemName; }

    public String getMovementType() { return movementType; }
    public void setMovementType(String movementType) { this.movementType = movementType; }

    public BigDecimal getQuantity() { return quantity; }
    public void setQuantity(BigDecimal quantity) { this.quantity = quantity; }

    public BigDecimal getUnitCost() { return unitCost; }
    public void setUnitCost(BigDecimal unitCost) { this.unitCost = unitCost; }

    public BigDecimal getTotalCost() { return totalCost; }
    public void setTotalCost(BigDecimal totalCost) { this.totalCost = totalCost; }

    public String getReferenceType() { return referenceType; }
    public void setReferenceType(String referenceType) { this.referenceType = referenceType; }

    public Long getReferenceId() { return referenceId; }
    public void setReferenceId(Long referenceId) { this.referenceId = referenceId; }

    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }

    public Long getPerformedById() { return performedById; }
    public void setPerformedById(Long performedById) { this.performedById = performedById; }

    public String getPerformedByName() { return performedByName; }
    public void setPerformedByName(String performedByName) { this.performedByName = performedByName; }

    public LocalDateTime getMovementDate() { return movementDate; }
    public void setMovementDate(LocalDateTime movementDate) { this.movementDate = movementDate; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
