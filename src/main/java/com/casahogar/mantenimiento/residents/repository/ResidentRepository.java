package com.casahogar.mantenimiento.residents.repository;

import com.casahogar.mantenimiento.common.repository.BaseRepository;
import com.casahogar.mantenimiento.residents.entity.Resident;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ResidentRepository extends BaseRepository<Resident, Long> {

    Optional<Resident> findByCode(String code);

    Optional<Resident> findByDocumentNumber(String documentNumber);

    List<Resident> findByStatus(Resident.ResidentStatus status);

    List<Resident> findByRoomId(Long roomId);

    boolean existsByCode(String code);

    boolean existsByDocumentNumber(String documentNumber);

    @Query("SELECT r FROM Resident r WHERE (r.code LIKE %:search% OR r.firstName LIKE %:search% OR r.lastName LIKE %:search% OR r.documentNumber LIKE %:search%) AND r.deleted = false")
    List<Resident> search(@Param("search") String search);

    @Query("SELECT r FROM Resident r WHERE (r.code LIKE %:search% OR r.firstName LIKE %:search% OR r.lastName LIKE %:search% OR r.documentNumber LIKE %:search%) AND r.deleted = false")
    Page<Resident> searchPaged(@Param("search") String search, Pageable pageable);

    @Query("SELECT r FROM Resident r WHERE r.deleted = false")
    Page<Resident> findAllActivePaged(Pageable pageable);

    @Query("SELECT r FROM Resident r WHERE r.status = :status AND r.deleted = false")
    Page<Resident> findByStatusPaged(@Param("status") Resident.ResidentStatus status, Pageable pageable);

    @Query("SELECT r FROM Resident r WHERE r.deleted = false")
    List<Resident> findAllActiveResidents();
}
