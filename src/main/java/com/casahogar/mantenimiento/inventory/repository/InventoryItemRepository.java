package com.casahogar.mantenimiento.inventory.repository;

import com.casahogar.mantenimiento.common.repository.BaseRepository;
import com.casahogar.mantenimiento.inventory.entity.InventoryItem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface InventoryItemRepository extends BaseRepository<InventoryItem, Long> {

    Optional<InventoryItem> findByCode(String code);

    List<InventoryItem> findByCategory(com.casahogar.mantenimiento.inventory.entity.InventoryItemCategory category);

    @Query("SELECT i FROM InventoryItem i WHERE i.currentStock <= i.reorderPoint AND i.deleted = false AND i.isActive = true")
    List<InventoryItem> findBelowReorderPoint();

    @Query("SELECT i FROM InventoryItem i WHERE i.currentStock <= i.minimumStock AND i.deleted = false AND i.isActive = true")
    List<InventoryItem> findBelowMinimumStock();

    boolean existsByCode(String code);

    @Query("SELECT i FROM InventoryItem i WHERE (i.code LIKE %:search% OR i.name LIKE %:search% OR i.description LIKE %:search%) AND i.deleted = false")
    List<InventoryItem> search(@Param("search") String search);

    @Query("SELECT i FROM InventoryItem i WHERE (i.code LIKE %:search% OR i.name LIKE %:search% OR i.description LIKE %:search%) AND i.deleted = false")
    Page<InventoryItem> searchPaged(@Param("search") String search, Pageable pageable);

    @Query("SELECT i FROM InventoryItem i WHERE i.deleted = false")
    Page<InventoryItem> findAllActivePaged(Pageable pageable);
}
