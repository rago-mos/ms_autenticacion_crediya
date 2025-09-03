package co.com.crediya.security.jwt.manager;

import co.com.crediya.security.jwt.provider.JwtProvider;
import io.jsonwebtoken.Claims;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatusCode;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import reactor.test.StepVerifier;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class JwtAuthenticationManagerTest {

    private JwtProvider jwtProvider;
    private JwtAuthenticationManager authenticationManager;

    @BeforeEach
    void setUp() {
        jwtProvider = mock(JwtProvider.class);
        authenticationManager = new JwtAuthenticationManager(jwtProvider);
    }

    @Test
    void shouldAuthenticateSuccessfullyWithValidToken() {
        String token = "valid-token";

        Claims claims = mock(Claims.class);
        when(claims.getSubject()).thenReturn("admin");
        when(claims.get("roles")).thenReturn(
                List.of(Map.of("authority", "ROLE_ADMIN"), Map.of("authority", "ROLE_USER"))
        );

        when(jwtProvider.getClaims(token)).thenReturn(claims);

        Authentication input = new UsernamePasswordAuthenticationToken(token, token);

        StepVerifier.create(authenticationManager.authenticate(input))
                .assertNext(auth -> {
                    assertEquals("admin", auth.getPrincipal());
                    assertTrue(auth.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN")));
                    assertTrue(auth.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_USER")));
                })
                .verifyComplete();
    }

    @Test
    void shouldFailAuthenticationWithInvalidToken() {
        String token = "invalid-token";

        when(jwtProvider.getClaims(token)).thenThrow(new IllegalArgumentException("Invalid token"));

        Authentication input = new UsernamePasswordAuthenticationToken(token, token);

        StepVerifier.create(authenticationManager.authenticate(input))
                .expectErrorMatches(e ->
                        e instanceof org.springframework.web.server.ResponseStatusException &&
                                ((org.springframework.web.server.ResponseStatusException) e).getStatusCode().equals(HttpStatusCode.valueOf(403))
                )
                .verify();
    }
}