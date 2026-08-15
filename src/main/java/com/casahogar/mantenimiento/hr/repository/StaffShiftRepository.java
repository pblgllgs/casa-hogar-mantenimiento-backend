package com.casahogar.mantenimiento.hr.repository;

import com.casahogar.mantenimiento.hr.entity.StaffShift;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StaffShiftRepository extends JpaRepository<StaffShift, Long> {

    @Query("SELECT ss FROM StaffShift ss WHERE ss.staffId = :staffId AND ss.isActive = true")
    List<StaffShift> findActiveByStaffId(@Param("staffId") Long staffId);

    @Query("SELECT ss FROM StaffShift ss WHERE ss.shiftId = :shiftId AND ss.isActive = true")
    List<StaffShift> findActiveByShiftId(@Param("shiftId") Long shiftId);

    @Query("SELECT ss FROM StaffShift ss WHERE ss.staffId = :staffId AND ss.shiftId = :shiftId AND ss.isActive = true")
    List<StaffShift> findByStaffIdAndShiftId(@Param("staffId") Long staffId, @Param("shiftId") Long shiftId);

    @Query("SELECT ss FROM StaffShift ss WHERE ss.staffId = :staffId AND ss.shiftId = :shiftId AND ss.startDate = :startDate")
    List<StaffShift> findByStaffIdAndShiftIdAndStartDate(@Param("staffId") Long staffId, @Param("shiftId") Long shiftId, @Param("startDate") java.time.LocalDate startDate);

    List<StaffShift> findAllByIsActiveTrue();
}
