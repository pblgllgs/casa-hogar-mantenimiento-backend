package com.casahogar.mantenimiento.auth.controller;

import com.casahogar.mantenimiento.auth.dto.AuthResponse;
import com.casahogar.mantenimiento.auth.dto.LoginRequest;
import com.casahogar.mantenimiento.auth.dto.RegisterRequest;
import com.casahogar.mantenimiento.auth.dto.UpdateUserRolesRequest;
import com.casahogar.mantenimiento.auth.repository.UserRepository;
import com.casahogar.mantenimiento.common.dto.ApiResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("integration")
class UserAdminControllerIntegrationTest {

    private static final ParameterizedTypeReference<ApiResponse<List<AuthResponse.UserInfo>>> USERS_LIST_TYPE =
            new ParameterizedTypeReference<ApiResponse<List<AuthResponse.UserInfo>>>() {};

    private static final ParameterizedTypeReference<ApiResponse<AuthResponse.UserInfo>> USER_TYPE =
            new ParameterizedTypeReference<ApiResponse<AuthResponse.UserInfo>>() {};

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private String adminToken;

    @BeforeEach
    void cleanDatabase() {
        jdbcTemplate.execute("SET FOREIGN_KEY_CHECKS = 0");
        jdbcTemplate.execute("DELETE FROM user_roles");
        jdbcTemplate.execute("DELETE FROM users");
        jdbcTemplate.execute("SET FOREIGN_KEY_CHECKS = 1");

        adminToken = registerAndLogin("admin_test", Set.of("ADMIN"), "ADMIN");
    }

    private HttpHeaders jsonHeaders(String token) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        if (token != null) headers.setBearerAuth(token);
        return headers;
    }

    private RegisterRequest buildRegisterRequest(String username) {
        RegisterRequest request = new RegisterRequest();
        request.setUsername(username);
        request.setEmail(username + "@test.cl");
        request.setPassword("secret123");
        request.setFirstName("Test");
        request.setLastName("User");
        return request;
    }

    private String register(String username, Set<String> roles, String defaultRole) {
        ResponseEntity<ApiResponse<AuthResponse>> response = restTemplate.exchange(
                "/auth/register", HttpMethod.POST,
                new HttpEntity<>(buildRegisterRequest(username), jsonHeaders(null)),
                new ParameterizedTypeReference<ApiResponse<AuthResponse>>() {});
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        if (roles != null && !roles.isEmpty()) {
            userRepository.findByUsername(username).ifPresent(u -> {
                u.setRoles(roles.stream()
                        .map(r -> com.casahogar.mantenimiento.auth.entity.Role.valueOf(r))
                        .collect(java.util.stream.Collectors.toCollection(java.util.HashSet::new)));
                userRepository.save(u);
            });
        }
        return response.getBody().getData().getAccessToken();
    }

    private String registerAndLogin(String username, Set<String> roles, String defaultRole) {
        String token = register(username, roles, defaultRole);
        LoginRequest login = new LoginRequest();
        login.setUsername(username);
        login.setPassword("secret123");
        ResponseEntity<ApiResponse<AuthResponse>> loginResponse = restTemplate.exchange(
                "/auth/login", HttpMethod.POST,
                new HttpEntity<>(login, jsonHeaders(null)),
                new ParameterizedTypeReference<ApiResponse<AuthResponse>>() {});
        assertThat(loginResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        return loginResponse.getBody().getData().getAccessToken();
    }

    @Test
    void listUsers_asAdmin_returnsAllUsers() {
        register("user_a", null, "VIEWER");
        register("user_b", null, "VIEWER");

        ResponseEntity<ApiResponse<List<AuthResponse.UserInfo>>> response = restTemplate.exchange(
                "/auth/users", HttpMethod.GET,
                new HttpEntity<>(jsonHeaders(adminToken)), USERS_LIST_TYPE);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        List<AuthResponse.UserInfo> users = response.getBody().getData();
        assertThat(users).extracting(AuthResponse.UserInfo::getUsername)
                .contains("admin_test", "user_a", "user_b");
    }

    @Test
    void listUsers_withoutToken_returns403() {
        ResponseEntity<ApiResponse> response = restTemplate.getForEntity("/auth/users", ApiResponse.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
    }

    @Test
    void listUsers_asNonAdmin_returns403() {
        String viewerToken = registerAndLogin("viewer_user", null, "VIEWER");

        ResponseEntity<ApiResponse> response = restTemplate.exchange(
                "/auth/users", HttpMethod.GET,
                new HttpEntity<>(jsonHeaders(viewerToken)), ApiResponse.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
    }

    @Test
    void updateRoles_asAdmin_replacesRoles() {
        String userId = registerAndGetId("target_user", null);

        UpdateUserRolesRequest request = new UpdateUserRolesRequest();
        request.setRoles(List.of("SUPERVISOR", "MAINTENANCE"));

        ResponseEntity<ApiResponse<AuthResponse.UserInfo>> response = restTemplate.exchange(
                "/auth/users/" + userId + "/roles", HttpMethod.PUT,
                new HttpEntity<>(request, jsonHeaders(adminToken)), USER_TYPE);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().getData().getRoles())
                .containsExactlyInAnyOrder("SUPERVISOR", "MAINTENANCE");
    }

    @Test
    void updateRoles_asNonAdmin_returns403() {
        String viewerToken = registerAndLogin("viewer_user", null, "VIEWER");
        String userId = registerAndGetId("target_user", null);

        UpdateUserRolesRequest request = new UpdateUserRolesRequest();
        request.setRoles(List.of("ADMIN"));

        ResponseEntity<ApiResponse> response = restTemplate.exchange(
                "/auth/users/" + userId + "/roles", HttpMethod.PUT,
                new HttpEntity<>(request, jsonHeaders(viewerToken)), ApiResponse.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
    }

    @Test
    void updateRoles_unknownUser_returnsBadRequest() {
        UpdateUserRolesRequest request = new UpdateUserRolesRequest();
        request.setRoles(List.of("ADMIN"));

        ResponseEntity<ApiResponse> response = restTemplate.exchange(
                "/auth/users/99999/roles", HttpMethod.PUT,
                new HttpEntity<>(request, jsonHeaders(adminToken)), ApiResponse.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    void updateRoles_invalidRoleName_returnsBadRequest() {
        String userId = registerAndGetId("target_user", null);

        UpdateUserRolesRequest request = new UpdateUserRolesRequest();
        request.setRoles(List.of("BOGUS_ROLE"));

        ResponseEntity<ApiResponse> response = restTemplate.exchange(
                "/auth/users/" + userId + "/roles", HttpMethod.PUT,
                new HttpEntity<>(request, jsonHeaders(adminToken)), ApiResponse.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    void updateRoles_emptyRoles_returnsBadRequest() {
        String userId = registerAndGetId("target_user", null);

        UpdateUserRolesRequest request = new UpdateUserRolesRequest();
        request.setRoles(List.of());

        ResponseEntity<ApiResponse> response = restTemplate.exchange(
                "/auth/users/" + userId + "/roles", HttpMethod.PUT,
                new HttpEntity<>(request, jsonHeaders(adminToken)), ApiResponse.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    private String registerAndGetId(String username, Set<String> roles) {
        register(username, roles, "VIEWER");
        return String.valueOf(userRepository.findByUsername(username).orElseThrow().getId());
    }
}
