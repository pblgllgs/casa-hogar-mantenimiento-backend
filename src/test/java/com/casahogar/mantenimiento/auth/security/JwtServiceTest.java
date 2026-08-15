package com.casahogar.mantenimiento.auth.security;

import com.casahogar.mantenimiento.auth.entity.Role;
import com.casahogar.mantenimiento.auth.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.Base64;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class JwtServiceTest {

    private JwtService jwtService;

    @BeforeEach
    void setUp() throws Exception {
        jwtService = new JwtService();
        setField(jwtService, "secret", Base64.getEncoder().encodeToString(new byte[64]));
        setField(jwtService, "jwtExpiration", 60000L);
        setField(jwtService, "refreshExpiration", 120000L);
        jwtService.init();
    }

    private void setField(Object target, String name, Object value) throws Exception {
        Field field = JwtService.class.getDeclaredField(name);
        field.setAccessible(true);
        field.set(target, value);
    }

    private User user(String username) {
        User user = new User();
        user.setUsername(username);
        user.setPassword("dummy");
        user.setRoles(Set.of(Role.ADMIN));
        return user;
    }

    @Test
    void generateToken_createsValidAccessToken() {
        String token = jwtService.generateToken(user("admin"), Set.of("ADMIN"));

        assertTrue(jwtService.isAccessToken(token));
        assertFalse(jwtService.isRefreshToken(token));
        assertEquals("admin", jwtService.extractUsername(token));
        assertEquals(Set.of("ADMIN"), jwtService.extractRoles(token));
        assertTrue(jwtService.isTokenValid(token, user("admin")));
    }

    @Test
    void generateRefreshToken_createsValidRefreshToken() {
        String token = jwtService.generateRefreshToken("admin");

        assertTrue(jwtService.isRefreshToken(token));
        assertFalse(jwtService.isAccessToken(token));
        assertEquals("admin", jwtService.extractUsername(token));
    }

    @Test
    void tokenSignedWithDifferentSecret_isRejected() throws Exception {
        String token = jwtService.generateToken(user("admin"), Set.of("ADMIN"));

        JwtService other = new JwtService();
        setField(other, "secret", Base64.getEncoder().encodeToString(new byte[65]));
        setField(other, "jwtExpiration", 60000L);
        setField(other, "refreshExpiration", 120000L);
        other.init();

        assertFalse(other.isTokenValid(token, user("admin")));
    }

    @Test
    void expiredToken_isInvalid() throws Exception {
        setField(jwtService, "jwtExpiration", -1000L);
        String token = jwtService.generateToken(user("admin"), Set.of("ADMIN"));

        assertFalse(jwtService.isTokenValid(token, user("admin")));
    }

    @Test
    void refreshToken_usedAsAccess_isRejected() {
        String refresh = jwtService.generateRefreshToken("admin");

        assertFalse(jwtService.isAccessToken(refresh));
        assertTrue(jwtService.isRefreshTokenValid(refresh, user("admin")));
    }
}
