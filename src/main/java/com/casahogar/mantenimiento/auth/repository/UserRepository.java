package com.casahogar.mantenimiento.auth.repository;

import com.casahogar.mantenimiento.auth.entity.User;
import com.casahogar.mantenimiento.common.repository.BaseRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends BaseRepository<User, Long> {
    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);
    Optional<User> findByDocumentNumber(String documentNumber);
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
    boolean existsByDocumentNumber(String documentNumber);

    @Query("SELECT u FROM User u WHERE u.isActive = true AND :role MEMBER OF u.roles")
    List<User> findActiveUsersByRole(@Param("role") String role);
}