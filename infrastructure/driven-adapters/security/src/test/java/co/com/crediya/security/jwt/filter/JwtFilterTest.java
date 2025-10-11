package co.com.crediya.security.jwt.filter;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.stubbing.Answer;
import org.springframework.http.HttpHeaders;
import org.springframework.mock.http.server.reactive.MockServerHttpRequest;
import org.springframework.mock.web.server.MockServerWebExchange;
import org.springframework.security.web.server.util.matcher.ServerWebExchangeMatcher;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class JwtFilterTest {

    @Mock
    private ServerWebExchangeMatcher matcher;

    @Mock
    private WebFilterChain chain;

    private JwtFilter jwtFilter;

    @BeforeEach
    void setUp() {
        jwtFilter = new JwtFilter(matcher);
    }

    @Test
    void shouldPassThroughPublicPathWithoutToken() {
        ServerWebExchange exchange = MockServerWebExchange.from(
                MockServerHttpRequest.get("/actuator/health").build()
        );

        Answer<Mono<ServerWebExchangeMatcher.MatchResult>> answer = invocation ->
                ServerWebExchangeMatcher.MatchResult.match();

        when(matcher.matches(any(ServerWebExchange.class))).thenAnswer(answer);
        when(chain.filter(exchange)).thenReturn(Mono.empty());

        StepVerifier.create(jwtFilter.filter(exchange, chain))
                .expectComplete()
                .verify();

        assertNull(exchange.getAttributes().get("token"));
    }

    @Test
    void shouldPassThroughProtectedPathWithoutToken() {
        ServerWebExchange exchange = MockServerWebExchange.from(MockServerHttpRequest.get("/api/v1/protected").build());

        Answer<Mono<ServerWebExchangeMatcher.MatchResult>> answer = invocation ->
                ServerWebExchangeMatcher.MatchResult.match();

        when(matcher.matches(any(ServerWebExchange.class))).thenAnswer(answer);
        when(chain.filter(exchange)).thenReturn(Mono.empty());

        StepVerifier.create(jwtFilter.filter(exchange, chain))
                .verifyComplete();

        Assertions.assertNull(exchange.getAttributes().get("token"));
    }

    @Test
    void shouldExtractTokenFromProtectedPath() {
        String jwt = "Bearer abc.def.ghi";
        ServerWebExchange exchange = MockServerWebExchange.from(
                MockServerHttpRequest.get("/api/v1/protected")
                        .header(HttpHeaders.AUTHORIZATION, jwt)
                        .build()
        );

        Answer<Mono<ServerWebExchangeMatcher.MatchResult>> answer = invocation ->
                ServerWebExchangeMatcher.MatchResult.notMatch();

        when(matcher.matches(any(ServerWebExchange.class))).thenAnswer(answer);
        when(chain.filter(exchange)).thenReturn(Mono.empty());

        StepVerifier.create(jwtFilter.filter(exchange, chain))
                .expectComplete()
                .verify();

        Assertions.assertEquals("abc.def.ghi", exchange.getAttributes().get("token"));
    }

}