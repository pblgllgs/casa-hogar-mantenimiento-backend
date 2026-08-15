package com.casahogar.mantenimiento.location.repository;

import com.casahogar.mantenimiento.location.entity.LocationPhoto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LocationPhotoRepository extends JpaRepository<LocationPhoto, Long> {

    List<LocationPhoto> findByDeletedFalseOrderByDisplayOrderAsc();

    @Query("SELECT p FROM LocationPhoto p WHERE p.deleted = false AND p.isActive = true ORDER BY p.displayOrder ASC")
    List<LocationPhoto> findAllActive();

    @Query("SELECT p FROM LocationPhoto p WHERE p.deleted = false ORDER BY p.displayOrder ASC")
    List<LocationPhoto> findAllNotDeleted();
}
