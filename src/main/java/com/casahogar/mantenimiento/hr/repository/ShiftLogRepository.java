package com.casahogar.mantenimiento.hr.repository;

import com.casahogar.mantenimiento.hr.entity.ShiftLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ShiftLogRepository extends JpaRepository<ShiftLog, Long> {

    List<ShiftLog> findByShiftIdAndLogDateOrderByCreatedAtAsc(Long shiftId, LocalDate logDate);

    List<ShiftLog> findByLogDateBetweenOrderByCreatedAtAsc(LocalDate start, LocalDate end);
}