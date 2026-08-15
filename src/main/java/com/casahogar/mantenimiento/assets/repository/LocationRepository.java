package com.casahogar.mantenimiento.assets.repository;

import com.casahogar.mantenimiento.assets.entity.Location;
import com.casahogar.mantenimiento.common.repository.BaseRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LocationRepository extends BaseRepository<Location, Long> {

    Optional<Location> findByCode(String code);

    List<Location> findByType(Location.LocationType type);

    List<Location> findByParentId(Long parentId);

    @Query("SELECT l FROM Location l WHERE l.deleted = false AND l.isActive = true")
    List<Location> findAllActiveLocations();

    @Query("SELECT l FROM Location l WHERE (l.code LIKE %:search% OR l.name LIKE %:search% OR l.description LIKE %:search%) AND l.deleted = false")
    List<Location> search(@Param("search") String search);

    @Query("SELECT l FROM Location l WHERE (l.code LIKE %:search% OR l.name LIKE %:search% OR l.description LIKE %:search%) AND l.deleted = false")
    Page<Location> searchPaged(@Param("search") String search, Pageable pageable);

    @Query("SELECT l FROM Location l WHERE l.deleted = false")
    Page<Location> findAllActivePaged(Pageable pageable);

    boolean existsByCode(String code);
}
