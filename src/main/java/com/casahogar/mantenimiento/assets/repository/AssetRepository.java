package com.casahogar.mantenimiento.assets.repository;

import com.casahogar.mantenimiento.assets.entity.Asset;
import com.casahogar.mantenimiento.common.repository.BaseRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface AssetRepository extends BaseRepository<Asset, Long> {

    Optional<Asset> findByAssetCode(String assetCode);

    List<Asset> findByCategory(Asset.AssetStatus status);

    List<Asset> findByStatus(Asset.AssetStatus status);

    List<Asset> findByLocationId(Long locationId);

    @Query("SELECT a FROM Asset a WHERE a.nextMaintenanceDate <= :date AND a.deleted = false AND a.status != 'DECOMMISSIONED' AND a.status != 'RETIRED'")
    List<Asset> findDueForMaintenance(@Param("date") LocalDate date);

    Page<Asset> findAllByDeletedFalse(Pageable pageable);

    @Query("SELECT a FROM Asset a WHERE (a.assetCode LIKE %:search% OR a.name LIKE %:search% OR a.brand LIKE %:search% OR a.model LIKE %:search%) AND a.deleted = false")
    Page<Asset> search(@Param("search") String search, Pageable pageable);

    boolean existsByAssetCode(String assetCode);

    long countByStatusAndDeletedFalse(Asset.AssetStatus status);
}
