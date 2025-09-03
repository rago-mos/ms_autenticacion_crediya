package co.com.crediya.security.config;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.http.server.reactive.MockServerHttpRequest;
import org.springframework.mock.http.server.reactive.MockServerHttpResponse;
import org.springframework.mock.web.server.MockServerWebExchange;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.spy;

class CustomAccessDeniedHandlerTest {

    private CustomAccessDeniedHandler handler;

    @BeforeEach
    void setUp() {
        handler = new CustomAccessDeniedHandler();
    }

    @Test
    void shouldRespondWithForbiddenAndJsonBody() {
        ServerWebExchange exchange = MockServerWebExchange.from(MockServerHttpRequest.get("/secure"));
        AccessDeniedException exception = new AccessDeniedException("Access denied");

        Mono<Void> result = handler.handle(exchange, exception);

        StepVerifier.create(result)
                .verifyComplete();

        MockServerHttpResponse response = (MockServerHttpResponse) exchange.getResponse();

        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        assertEquals(MediaType.APPLICATION_JSON, response.getHeaders().getContentType());

        String body = response.getBodyAsString().block();
        assertNotNull(body);
        assertTrue(body.contains("\"status\":403"));
        assertTrue(body.contains("\"error\":\"AccessDeniedException\""));
        assertTrue(body.contains("\"message\":\"Access denied. You do not have the necessary permissions for this resource\""));
    }

    @Test
    void shouldReturnMonoErrorWhenJsonProcessingFails() throws JsonProcessingException, NoSuchFieldException, IllegalAccessException {
        // Spy del handler para interceptar el ObjectMapper
        CustomAccessDeniedHandler handlerSpy = spy(new CustomAccessDeniedHandler());

        // Simula que writeValueAsBytes lanza JsonProcessingException
        ObjectMapper mapperSpy = spy(new ObjectMapper());
        doThrow(new JsonProcessingException("Serialization failed") {}).when(mapperSpy)
                .writeValueAsBytes(any());

        // Inyecta el mapper fallido usando reflexión
        Field field = CustomAccessDeniedHandler.class.getDeclaredField("objectMapper");
        field.setAccessible(true);
        field.set(handlerSpy, mapperSpy);

        ServerWebExchange exchange = MockServerWebExchange.from(MockServerHttpRequest.get("/secure"));
        AccessDeniedException exception = new AccessDeniedException("Access denied");

        Mono<Void> result = handlerSpy.handle(exchange, exception);

        StepVerifier.create(result)
                .expectErrorMatches(e -> e instanceof IllegalStateException &&
                        e.getCause() instanceof JsonProcessingException &&
                        e.getMessage().contains("Serialization failed"))
                .verify();
    }

}