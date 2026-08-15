package com.casahogar.mantenimiento.hr.repository;

import com.casahogar.mantenimiento.common.repository.BaseRepository;
import com.casahogar.mantenimiento.hr.entity.Staff;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StaffRepository extends BaseRepository<Staff, Long> {

    Optional<Staff> findByEmployeeCode(String employeeCode);

    Optional<Staff> findByDocumentNumber(String documentNumber);

    List<Staff> findByStatus(Staff.StaffStatus status);

    List<Staff> findByDepartment(String department);

    boolean existsByEmployeeCode(String employeeCode);

    boolean existsByDocumentNumber(String documentNumber);

    @Query("SELECT s FROM Staff s WHERE (s.employeeCode LIKE %:search% OR s.firstName LIKE %:search% OR s.lastName LIKE %:search% OR s.documentNumber LIKE %:search%) AND s.deleted = false")
    List<Staff> search(@Param("search") String search);

    @Query("SELECT s FROM Staff s WHERE (s.employeeCode LIKE %:search% OR s.firstName LIKE %:search% OR s.lastName LIKE %:search% OR s.documentNumber LIKE %:search%) AND s.deleted = false")
    Page<Staff> searchPaged(@Param("search") String search, Pageable pageable);

    @Query("SELECT s FROM Staff s WHERE s.deleted = false")
    Page<Staff> findAllActivePaged(Pageable pageable);

    @Query("SELECT s FROM Staff s WHERE s.department = :department AND s.deleted = false")
    Page<Staff> findByDepartmentPaged(@Param("department") String department, Pageable pageable);
}
