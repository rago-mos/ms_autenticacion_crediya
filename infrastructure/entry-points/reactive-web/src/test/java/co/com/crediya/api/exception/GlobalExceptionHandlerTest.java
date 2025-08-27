package co.com.crediya.api.exception;

import co.com.crediya.usecase.createuser.exception.BusinessException;
import co.com.crediya.usecase.createuser.exception.InvalidRequestException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GlobalExceptionHandlerTest {

    private GlobalExceptionHandler handler;

    @Mock
    private ServerWebExchange exchange;

    @Mock
    private ServerHttpResponse response;

    @Mock
    private DataBufferFactory bufferFactory;

    @Mock
    private DataBuffer dataBuffer;

    @BeforeEach
    void setUp() {
        handler = new GlobalExceptionHandler();

        when(exchange.getResponse()).thenReturn(response);
        when(response.bufferFactory()).thenReturn(bufferFactory);
        when(response.writeWith(any())).thenReturn(Mono.empty());
        when(bufferFactory.wrap(any(byte[].class))).thenReturn(dataBuffer);
        when(response.getHeaders()).thenReturn(new HttpHeaders());
    }

    @Test
    void shouldHandleBusinessException() {
        BusinessException ex = new BusinessException("Business rule violated");

        Mono<Void> result = handler.handle(exchange, ex);

        // Verifica que se haya configurado el código de estado
        verify(response).setStatusCode(HttpStatus.CONFLICT);

        // Verifica que se haya escrito el buffer
        verify(response).writeWith(any());

    }

    @Test
    void shouldHandleInvalidRequestException() {
        InvalidRequestException ex = new InvalidRequestException("Invalid input");

        Mono<Void> result = handler.handle(exchange, ex);

        verify(response).setStatusCode(HttpStatus.BAD_REQUEST);
        verify(response).writeWith(any());

    }

    @Test
    void shouldHandleIllegalArgumentException() {
        IllegalArgumentException ex = new IllegalArgumentException("Bad argument");

        Mono<Void> result = handler.handle(exchange, ex);

        verify(response).setStatusCode(HttpStatus.BAD_REQUEST);
        verify(response).writeWith(any());

    }

    @Test
    void shouldHandleUnexpectedException() {
        RuntimeException ex = new RuntimeException("Unexpected failure");

        Mono<Void> result = handler.handle(exchange, ex);

        verify(response).setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR);
        verify(response).writeWith(any());

    }

    @MockitoSettings(strictness = Strictness.LENIENT)
    @Test
    void shouldReturnOrderValue() {
        assertEquals(-1, handler.getOrder());
    }


}