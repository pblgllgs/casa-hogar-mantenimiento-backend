package com.casahogar.mantenimiento.auth.service;

import com.casahogar.mantenimiento.auth.dto.AuthResponse;
import com.casahogar.mantenimiento.auth.dto.LoginRequest;
import com.casahogar.mantenimiento.auth.dto.RegisterRequest;
import com.casahogar.mantenimiento.auth.entity.Role;
import com.casahogar.mantenimiento.auth.entity.User;
import com.casahogar.mantenimiento.auth.repository.UserRepository;
import com.casahogar.mantenimiento.auth.security.JwtService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.EnumSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder,
                       JwtService jwtService,
                       AuthenticationManager authenticationManager) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
    }

    @Transactional
    public AuthResponse register(RegisterRequest request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new IllegalArgumentException("El nombre de usuario ya existe");
        }
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("El email ya está registrado");
        }
        if (request.getDocumentNumber() != null && userRepository.existsByDocumentNumber(request.getDocumentNumber())) {
            throw new IllegalArgumentException("El número de documento ya existe");
        }

        User user = new User();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setPhone(request.getPhone());
        user.setDocumentNumber(request.getDocumentNumber());
        user.setRoles(Set.of(Role.VIEWER));
        user.setIsActive(true);

        userRepository.save(user);

        return generateAuthResponse(user);
    }

    public AuthResponse login(LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
        );

        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new IllegalArgumentException("Credenciales inválidas"));

        if (!user.getIsActive()) {
            throw new IllegalArgumentException("Usuario desactivado");
        }

        user.setLastLoginAt(java.time.LocalDateTime.now());
        userRepository.save(user);

        return generateAuthResponse(user);
    }

    public AuthResponse refreshToken(String refreshToken) {
        String username;
        try {
            username = jwtService.extractUsername(refreshToken);
        } catch (io.jsonwebtoken.JwtException | IllegalArgumentException ex) {
            throw new IllegalArgumentException("Token de refresco inválido o expirado");
        }
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("Token inválido"));

        if (!user.isEnabled() || !jwtService.isRefreshTokenValid(refreshToken, user)) {
            throw new IllegalArgumentException("Token de refresco inválido o expirado");
        }

        return generateAuthResponse(user);
    }

    @Transactional(readOnly = true)
    public List<AuthResponse.UserInfo> getAllUsers() {
        return userRepository.findAll().stream()
                .map(this::toUserInfo)
                .collect(Collectors.toList());
    }

    @Transactional
    public AuthResponse.UserInfo updateUserRoles(Long userId, Set<String> requestedRoles) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));

        Set<Role> validRoles = EnumSet.allOf(Role.class);
        Set<Role> newRoles = EnumSet.noneOf(Role.class);
        for (String roleName : requestedRoles) {
            try {
                newRoles.add(Role.valueOf(roleName));
            } catch (IllegalArgumentException ex) {
                throw new IllegalArgumentException("Role inválido: " + roleName);
            }
        }
        if (newRoles.isEmpty()) {
            throw new IllegalArgumentException("El usuario debe tener al menos un role");
        }
        user.setRoles(newRoles);
        userRepository.save(user);
        return toUserInfo(user);
    }

    private AuthResponse.UserInfo toUserInfo(User user) {
        Set<String> roles = user.getRoles().stream()
                .map(Enum::name)
                .collect(Collectors.toSet());
        AuthResponse.UserInfo userInfo = new AuthResponse.UserInfo();
        userInfo.setId(user.getId());
        userInfo.setUsername(user.getUsername());
        userInfo.setEmail(user.getEmail());
        userInfo.setFirstName(user.getFirstName());
        userInfo.setLastName(user.getLastName());
        userInfo.setFullName(user.getFullName());
        userInfo.setRoles(roles);
        return userInfo;
    }

    private AuthResponse generateAuthResponse(User user) {
        Set<String> roles = user.getRoles().stream()
                .map(Enum::name)
                .collect(Collectors.toSet());
        String accessToken = jwtService.generateToken(user, roles);
        String refreshToken = jwtService.generateRefreshToken(user.getUsername());

        AuthResponse response = new AuthResponse();
        response.setAccessToken(accessToken);
        response.setRefreshToken(refreshToken);
        response.setExpiresIn(jwtService.getExpirationTime());

        AuthResponse.UserInfo userInfo = new AuthResponse.UserInfo();
        userInfo.setId(user.getId());
        userInfo.setUsername(user.getUsername());
        userInfo.setEmail(user.getEmail());
        userInfo.setFirstName(user.getFirstName());
        userInfo.setLastName(user.getLastName());
        userInfo.setFullName(user.getFullName());
        userInfo.setRoles(roles);

        response.setUser(userInfo);
        return response;
    }
}
