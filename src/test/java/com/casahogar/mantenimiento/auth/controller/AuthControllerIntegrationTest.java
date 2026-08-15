package com.casahogar.mantenimiento.auth.controller;

import com.casahogar.mantenimiento.auth.dto.AuthResponse;
import com.casahogar.mantenimiento.auth.dto.LoginRequest;
import com.casahogar.mantenimiento.auth.dto.RefreshTokenRequest;
import com.casahogar.mantenimiento.auth.dto.RegisterRequest;
import com.casahogar.mantenimiento.common.dto.ApiResponse;
import com.casahogar.mantenimiento.auth.repository.UserRepository;
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

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("integration")
class AuthControllerIntegrationTest {

    private static final ParameterizedTypeReference<ApiResponse<AuthResponse>> AUTH_RESPONSE_TYPE =
            new ParameterizedTypeReference<ApiResponse<AuthResponse>>() {};

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void cleanDatabase() {
        jdbcTemplate.execute("SET FOREIGN_KEY_CHECKS = 0");
        jdbcTemplate.execute("DELETE FROM user_roles");
        jdbcTemplate.execute("DELETE FROM users");
        jdbcTemplate.execute("SET FOREIGN_KEY_CHECKS = 1");
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

    private HttpHeaders jsonHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return headers;
    }

    private AuthResponse register(String username) {
        ResponseEntity<ApiResponse<AuthResponse>> response = restTemplate.exchange(
                "/auth/register", HttpMethod.POST,
                new HttpEntity<>(buildRegisterRequest(username), jsonHeaders()),
                AUTH_RESPONSE_TYPE);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().isSuccess()).isTrue();
        return response.getBody().getData();
    }

    @Test
    void register_createsUserAndReturnsTokens() {
        AuthResponse auth = register("nuevo_usuario");

        assertThat(auth).isNotNull();
        assertThat(auth.getAccessToken()).isNotBlank();
        assertThat(auth.getRefreshToken()).isNotBlank();
        assertThat(auth.getTokenType()).isEqualTo("Bearer");
        assertThat(auth.getExpiresIn()).isPositive();
        assertThat(auth.getUser()).isNotNull();
        assertThat(auth.getUser().getUsername()).isEqualTo("nuevo_usuario");
        assertThat(auth.getUser().getEmail()).isEqualTo("nuevo_usuario@test.cl");
        assertThat(auth.getUser().getRoles()).contains("VIEWER");
        assertThat(userRepository.findByUsername("nuevo_usuario")).isPresent();
    }

    @Test
    void register_duplicateUsername_rejects() {
        register("dup");

        ResponseEntity<ApiResponse<AuthResponse>> second = restTemplate.exchange(
                "/auth/register", HttpMethod.POST,
                new HttpEntity<>(buildRegisterRequest("dup"), jsonHeaders()),
                AUTH_RESPONSE_TYPE);

        assertThat(second.getStatusCode().is4xxClientError() || second.getStatusCode().is5xxServerError())
                .as("duplicate registration should be rejected")
                .isTrue();
        assertThat(userRepository.findAll()).hasSize(1);
    }

    @Test
    void register_validationFails_shortPassword() {
        RegisterRequest request = buildRegisterRequest("corto_pw");
        request.setPassword("123");

        ResponseEntity<ApiResponse<AuthResponse>> response = restTemplate.exchange(
                "/auth/register", HttpMethod.POST,
                new HttpEntity<>(request, jsonHeaders()),
                AUTH_RESPONSE_TYPE);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(userRepository.findByUsername("corto_pw")).isEmpty();
    }

    @Test
    void login_validCredentials_returnsTokens() {
        register("login_ok");

        LoginRequest login = new LoginRequest();
        login.setUsername("login_ok");
        login.setPassword("secret123");

        ResponseEntity<ApiResponse<AuthResponse>> response = restTemplate.exchange(
                "/auth/login", HttpMethod.POST,
                new HttpEntity<>(login, jsonHeaders()),
                AUTH_RESPONSE_TYPE);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().isSuccess()).isTrue();
        AuthResponse auth = response.getBody().getData();
        assertThat(auth).isNotNull();
        assertThat(auth.getAccessToken()).isNotBlank();
        assertThat(auth.getUser().getUsername()).isEqualTo("login_ok");
    }

    @Test
    void login_wrongPassword_returns401() {
        register("login_bad");

        LoginRequest login = new LoginRequest();
        login.setUsername("login_bad");
        login.setPassword("wrong-password");

        ResponseEntity<ApiResponse<AuthResponse>> response = restTemplate.exchange(
                "/auth/login", HttpMethod.POST,
                new HttpEntity<>(login, jsonHeaders()),
                AUTH_RESPONSE_TYPE);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
    }

    @Test
    void login_unknownUser_returns401() {
        LoginRequest login = new LoginRequest();
        login.setUsername("ghost_user");
        login.setPassword("secret123");

        ResponseEntity<ApiResponse<AuthResponse>> response = restTemplate.exchange(
                "/auth/login", HttpMethod.POST,
                new HttpEntity<>(login, jsonHeaders()),
                AUTH_RESPONSE_TYPE);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
    }

    @Test
    void refresh_validToken_returnsNewTokens() {
        AuthResponse registered = register("refresh_ok");

        RefreshTokenRequest refresh = new RefreshTokenRequest();
        refresh.setRefreshToken(registered.getRefreshToken());

        ResponseEntity<ApiResponse<AuthResponse>> response = restTemplate.exchange(
                "/auth/refresh", HttpMethod.POST,
                new HttpEntity<>(refresh, jsonHeaders()),
                AUTH_RESPONSE_TYPE);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().isSuccess()).isTrue();
        assertThat(response.getBody().getData()).isNotNull();
        assertThat(response.getBody().getData().getAccessToken()).isNotBlank();
    }

    @Test
    void refresh_invalidToken_rejects() {
        RefreshTokenRequest refresh = new RefreshTokenRequest();
        refresh.setRefreshToken("this-is-not-a-valid-jwt");

        ResponseEntity<ApiResponse<AuthResponse>> response = restTemplate.exchange(
                "/auth/refresh", HttpMethod.POST,
                new HttpEntity<>(refresh, jsonHeaders()),
                AUTH_RESPONSE_TYPE);

        assertThat(response.getStatusCode().is4xxClientError()).isTrue();
    }

    @Test
    void logout_returns200() {
        AuthResponse registered = register("logout_ok");

        HttpHeaders headers = jsonHeaders();
        headers.setBearerAuth(registered.getAccessToken());

        ResponseEntity<ApiResponse<AuthResponse>> response = restTemplate.exchange(
                "/auth/logout", HttpMethod.POST,
                new HttpEntity<>(null, headers),
                AUTH_RESPONSE_TYPE);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().isSuccess()).isTrue();
    }
}
