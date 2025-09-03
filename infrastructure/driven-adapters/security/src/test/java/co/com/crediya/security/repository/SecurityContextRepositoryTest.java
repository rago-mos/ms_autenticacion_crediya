package co.com.crediya.security.repository;

import co.com.crediya.security.jwt.manager.JwtAuthenticationManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.mock.http.server.reactive.MockServerHttpRequest;
import org.springframework.mock.web.server.MockServerWebExchange;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class SecurityContextRepositoryTest {

    private JwtAuthenticationManager jwtAuthenticationManager;
    private SecurityContextRepository repository;

    @BeforeEach
    void setUp() {
        jwtAuthenticationManager = mock(JwtAuthenticationManager.class);
        repository = new SecurityContextRepository(jwtAuthenticationManager);
    }

    @Test
    void shouldReturnEmptyOnSave() {
        ServerWebExchange exchange = MockServerWebExchange.from(MockServerHttpRequest.get("/"));
        SecurityContext context = new SecurityContextImpl();

        StepVerifier.create(repository.save(exchange, context))
                .verifyComplete();
    }

    @Test
    void shouldLoadSecurityContextFromValidBearerToken() {
        String token = "mocked-jwt-token";

        Authentication authentication = new UsernamePasswordAuthenticationToken("user", null);
        when(jwtAuthenticationManager.authenticate(any())).thenReturn(Mono.just(authentication));

        ServerWebExchange exchange = MockServerWebExchange.from(
                MockServerHttpRequest.get("/")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
        );

        StepVerifier.create(repository.load(exchange))
                .assertNext(context -> {
                    assertNotNull(context);
                    assertEquals("user", context.getAuthentication().getPrincipal());
                })
                .verifyComplete();

        ArgumentCaptor<Authentication> captor = ArgumentCaptor.forClass(Authentication.class);
        verify(jwtAuthenticationManager).authenticate(captor.capture());

        Authentication captured = captor.getValue();
        assertEquals(token, captured.getCredentials());
    }

    @Test
    void shouldReturnEmptyWhenNoAuthorizationHeader() {
        ServerWebExchange exchange = MockServerWebExchange.from(MockServerHttpRequest.get("/"));

        StepVerifier.create(repository.load(exchange))
                .verifyComplete();
    }

    @Test
    void shouldReturnEmptyWhenAuthorizationHeaderIsNotBearer() {
        ServerWebExchange exchange = MockServerWebExchange.from(
                MockServerHttpRequest.get("/")
                        .header(HttpHeaders.AUTHORIZATION, "Basic abc123")
        );

        StepVerifier.create(repository.load(exchange))
                .verifyComplete();
    }
}