package com.casahogar.mantenimiento.auth;

import com.casahogar.mantenimiento.auth.entity.Role;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("integration")
class RolesMigrationTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    void rolesTable_seededWithAllSevenRoles() {
        List<String> codes = jdbcTemplate.queryForList(
                "SELECT code FROM roles ORDER BY code", String.class);

        assertThat(codes).containsExactlyInAnyOrder(
                "ADMIN", "SUPERVISOR", "MAINTENANCE",
                "INVENTORY", "RESIDENTS", "HR", "VIEWER");
        assertThat(codes.size()).isEqualTo(Role.values().length);
    }

    @Test
    void rolesTable_codeIsPrimaryKey() {
        Long count = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM information_schema.KEY_COLUMN_USAGE "
                        + "WHERE TABLE_SCHEMA = DATABASE() AND TABLE_NAME = 'roles' AND COLUMN_NAME = 'code'",
                Long.class);
        assertThat(count).isPositive();
    }

    @Test
    void rolesTable_hasDisplayNameAndDescription() {
        String name = jdbcTemplate.queryForObject(
                "SELECT name FROM roles WHERE code = 'ADMIN'", String.class);
        String description = jdbcTemplate.queryForObject(
                "SELECT description FROM roles WHERE code = 'ADMIN'", String.class);

        assertThat(name).isEqualTo("Administrador");
        assertThat(description).isEqualTo("Acceso total al sistema");
    }

    @Test
    void userRoles_hasForeignKeyToRolesTable() {
        Long fkCount = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM information_schema.KEY_COLUMN_USAGE "
                        + "WHERE TABLE_SCHEMA = DATABASE() AND TABLE_NAME = 'user_roles' "
                        + "AND REFERENCED_TABLE_NAME = 'roles' AND REFERENCED_COLUMN_NAME = 'code'",
                Long.class);

        assertThat(fkCount).isEqualTo(1L);
    }

    @Test
    void enumRoleCodes_matchSeededRolesTable() {
        Set<String> enumCodes = new java.util.HashSet<>();
        for (Role role : Role.values()) {
            enumCodes.add(role.name());
        }
        Set<String> dbCodes = new java.util.HashSet<>(jdbcTemplate.queryForList(
                "SELECT code FROM roles", String.class));

        assertThat(enumCodes).isEqualTo(dbCodes);
    }
}
