package com.casahogar.mantenimiento.residents.repository;

import com.casahogar.mantenimiento.residents.entity.ResidentDailyHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface ResidentDailyHistoryRepository extends JpaRepository<ResidentDailyHistory, Long> {

    Optional<ResidentDailyHistory> findByResidentIdAndLogDateAndPeriod(Long residentId, LocalDate logDate, ResidentDailyHistory.Period period);

    List<ResidentDailyHistory> findByResidentIdAndLogDateOrderByPeriodAsc(Long residentId, LocalDate logDate);
}
