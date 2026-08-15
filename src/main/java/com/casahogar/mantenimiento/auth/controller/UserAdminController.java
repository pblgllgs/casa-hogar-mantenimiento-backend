package com.casahogar.mantenimiento.auth.controller;

import com.casahogar.mantenimiento.auth.dto.AuthResponse;
import com.casahogar.mantenimiento.auth.dto.UpdateUserRolesRequest;
import com.casahogar.mantenimiento.auth.service.AuthService;
import com.casahogar.mantenimiento.common.dto.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/auth/users")
@PreAuthorize("hasRole('ADMIN')")
@Tag(name = "Users (Admin)", description = "Gestión de usuarios (solo administradores)")
public class UserAdminController {

    private final AuthService authService;

    public UserAdminController(AuthService authService) {
        this.authService = authService;
    }

    @Operation(summary = "Listar todos los usuarios", description = "Devuelve todos los usuarios del sistema con sus roles")
    @GetMapping
    public ResponseEntity<ApiResponse<List<AuthResponse.UserInfo>>> listUsers() {
        return ResponseEntity.ok(ApiResponse.ok(authService.getAllUsers(), "Usuarios"));
    }

    @Operation(summary = "Actualizar roles de un usuario", description = "Reemplaza el conjunto completo de roles de un usuario")
    @PutMapping("/{id}/roles")
    public ResponseEntity<ApiResponse<AuthResponse.UserInfo>> updateRoles(
            @PathVariable Long id,
            @Valid @RequestBody UpdateUserRolesRequest request) {
        Set<String> roles = Set.copyOf(request.getRoles());
        AuthResponse.UserInfo updated = authService.updateUserRoles(id, roles);
        return ResponseEntity.ok(ApiResponse.ok(updated, "Roles actualizados"));
    }
}
