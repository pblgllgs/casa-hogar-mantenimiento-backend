package com.casahogar.mantenimiento.hr.repository;

import com.casahogar.mantenimiento.hr.entity.Shift;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ShiftRepository extends JpaRepository<Shift, Long> {

    @Query("SELECT s FROM Shift s WHERE s.isActive = true")
    List<Shift> findAllActive();

    @Query("SELECT s FROM Shift s WHERE s.isActive = true")
    Page<Shift> findAllActivePaged(Pageable pageable);

    @Query("SELECT s FROM Shift s WHERE (s.name LIKE %:search% OR s.daysOfWeek LIKE %:search%) AND s.isActive = true")
    Page<Shift> searchPaged(@Param("search") String search, Pageable pageable);

    List<Shift> findByIsActive(Boolean isActive);
}
