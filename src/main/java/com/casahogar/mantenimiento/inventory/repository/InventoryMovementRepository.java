package com.casahogar.mantenimiento.inventory.repository;

import com.casahogar.mantenimiento.inventory.entity.InventoryMovement;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InventoryMovementRepository extends JpaRepository<InventoryMovement, Long> {

    @Query("SELECT m FROM InventoryMovement m WHERE m.inventoryItemId = :itemId AND m.deleted = false ORDER BY m.movementDate DESC")
    List<InventoryMovement> findByInventoryItemId(@Param("itemId") Long itemId);

    @Query("SELECT m FROM InventoryMovement m WHERE m.inventoryItemId = :itemId AND m.deleted = false ORDER BY m.movementDate DESC")
    Page<InventoryMovement> findByInventoryItemId(@Param("itemId") Long itemId, Pageable pageable);

    @Query("SELECT m FROM InventoryMovement m WHERE m.movementType = :type AND m.deleted = false ORDER BY m.movementDate DESC")
    List<InventoryMovement> findByMovementType(@Param("type") String type);

    @Query("SELECT m FROM InventoryMovement m WHERE m.deleted = false ORDER BY m.movementDate DESC")
    Page<InventoryMovement> findAllByDeletedFalse(Pageable pageable);
}
