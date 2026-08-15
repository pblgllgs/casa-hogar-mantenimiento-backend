package com.casahogar.mantenimiento.residents.service;

import com.casahogar.mantenimiento.residents.dto.ResidentDailyHistoryRequest;
import com.casahogar.mantenimiento.residents.dto.ResidentDailyHistoryResponse;
import com.casahogar.mantenimiento.residents.entity.ResidentDailyHistory;
import com.casahogar.mantenimiento.residents.repository.ResidentDailyHistoryRepository;
import com.casahogar.mantenimiento.residents.repository.ResidentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ResidentDailyHistoryService {

    private final ResidentDailyHistoryRepository repository;
    private final ResidentRepository residentRepository;

    public ResidentDailyHistoryService(ResidentDailyHistoryRepository repository, ResidentRepository residentRepository) {
        this.repository = repository;
        this.residentRepository = residentRepository;
    }

    public List<ResidentDailyHistoryResponse> getByResidentAndDate(Long residentId, LocalDate date) {
        return repository.findByResidentIdAndLogDateOrderByPeriodAsc(residentId, date)
                .stream()
                .map(ResidentDailyHistoryResponse::of)
                .collect(Collectors.toList());
    }

    @Transactional
    public ResidentDailyHistoryResponse upsert(Long residentId, ResidentDailyHistoryRequest request) {
        if (!residentRepository.existsById(residentId)) {
            throw new IllegalArgumentException("Residente no encontrado");
        }
        if (request.getLogDate() == null) {
            throw new IllegalArgumentException("La fecha es requerida");
        }
        ResidentDailyHistory.Period period;
        try {
            period = ResidentDailyHistory.Period.valueOf(request.getPeriod());
        } catch (Exception e) {
            throw new IllegalArgumentException("Período inválido");
        }
        if (request.getComment() == null || request.getComment().isBlank()) {
            throw new IllegalArgumentException("El comentario es requerido");
        }

        ResidentDailyHistory history = repository
                .findByResidentIdAndLogDateAndPeriod(residentId, request.getLogDate(), period)
                .orElseGet(() -> {
                    ResidentDailyHistory h = new ResidentDailyHistory();
                    h.setResidentId(residentId);
                    h.setLogDate(request.getLogDate());
                    h.setPeriod(period);
                    return h;
                });

        history.setComment(request.getComment());
        history.setUpdatedAt(LocalDateTime.now());
        repository.save(history);
        return ResidentDailyHistoryResponse.of(history);
    }

    @Transactional
    public void delete(Long id) {
        repository.deleteById(id);
    }
}
