package co.com.crediya.security.jwt.filter;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.mock.http.server.reactive.MockServerHttpRequest;
import org.springframework.mock.web.server.MockServerWebExchange;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class JwtFilterTest {

    private JwtFilter jwtFilter;
    private WebFilterChain chain;

    @BeforeEach
    void setUp() {
        jwtFilter = new JwtFilter();
        chain = mock(WebFilterChain.class);
        when(chain.filter(any())).thenReturn(Mono.empty());
    }

    @Test
    void shouldAllowRequestToApiPathWithoutToken() {
        ServerWebExchange exchange = MockServerWebExchange.from(
                MockServerHttpRequest.get("/api/v1/usuarios")
        );

        StepVerifier.create(jwtFilter.filter(exchange, chain))
                .verifyComplete();

        verify(chain).filter(exchange);
    }

    @Test
    void shouldAllowRequestWithValidBearerToken() {
        String token = "mocked-token";
        ServerWebExchange exchange = MockServerWebExchange.from(
                MockServerHttpRequest.get("/secure")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
        );

        StepVerifier.create(jwtFilter.filter(exchange, chain))
                .verifyComplete();

        verify(chain).filter(exchange);
        assertEquals(token, exchange.getAttribute("token"));
    }

    @Test
    void shouldRejectRequestWithoutAuthorizationHeader() {
        ServerWebExchange exchange = MockServerWebExchange.from(
                MockServerHttpRequest.get("/secure")
        );

        StepVerifier.create(jwtFilter.filter(exchange, chain))
                .expectErrorMatches(e -> e.getMessage().equals("no token was found"))
                .verify();
    }

    @Test
    void shouldRejectRequestWithInvalidAuthorizationFormat() {
        ServerWebExchange exchange = MockServerWebExchange.from(
                MockServerHttpRequest.get("/secure")
                        .header(HttpHeaders.AUTHORIZATION, "Basic abc123")
        );

        StepVerifier.create(jwtFilter.filter(exchange, chain))
                .expectErrorMatches(e -> e.getMessage().equals("invalid route"))
                .verify();
    }
}