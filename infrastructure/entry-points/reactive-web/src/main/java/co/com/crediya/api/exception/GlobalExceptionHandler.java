package co.com.crediya.api.exception;

import co.com.crediya.api.dto.ErrorResponseHandler;
import co.com.crediya.usecase.createuser.exception.BusinessException;
import co.com.crediya.usecase.createuser.exception.InvalidRequestException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.boot.web.reactive.error.ErrorWebExceptionHandler;
import org.springframework.core.Ordered;
import org.springframework.core.codec.DecodingException;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebExchangeBindException;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.ServerWebInputException;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;

import static co.com.crediya.api.Handler.log;

@Component
public class GlobalExceptionHandler implements ErrorWebExceptionHandler, Ordered {

    private static final String INVALID_REQUEST_FORMAT = "Invalid request format: ";
    private static final String BINDING_ERROR = "Binding error: ";
    private static final String CONFLICT = "Conflict: ";
    private static final String UNEXPECTED_ERROR = "Unexpected error";
    private final ObjectMapper objectMapper;

    public GlobalExceptionHandler() {
        this.objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }

    @Override
    public Mono<Void> handle(ServerWebExchange exchange, Throwable ex) {
        log.error("Handled exception: {}", ex.getClass().getSimpleName(), ex);
        HttpStatus status = resolveStatus(ex);
        String message = resolveMessage(ex);

        ErrorResponseHandler errorBody = ErrorResponseHandler.builder()
                .timestamp(LocalDateTime.now())
                .status(status.value())
                .error(ex.getClass().getSimpleName())
                .message(message)
                .build();

        byte[] bytes;
        try {
            bytes = objectMapper.writeValueAsBytes(errorBody);
        } catch (JsonProcessingException e) {
            bytes = ("{\"error\":\"Serialization error\"}").getBytes(StandardCharsets.UTF_8);
            e.printStackTrace();
        }

        exchange.getResponse().setStatusCode(status);
        exchange.getResponse().getHeaders().setContentType(MediaType.APPLICATION_JSON);
        DataBuffer buffer = exchange.getResponse().bufferFactory().wrap(bytes);

        return exchange.getResponse().writeWith(Mono.just(buffer));
    }

    private HttpStatus resolveStatus(Throwable ex) {
        if (ex instanceof WebExchangeBindException) return HttpStatus.BAD_REQUEST;
        if (ex instanceof ServerWebInputException) return HttpStatus.BAD_REQUEST;
        if (ex instanceof ResponseStatusException e) return (HttpStatus) e.getStatusCode();
        if (ex instanceof IllegalArgumentException) return HttpStatus.BAD_REQUEST;
        if (ex instanceof InvalidFormatException) return HttpStatus.BAD_REQUEST;
        if (ex instanceof DecodingException) return HttpStatus.BAD_REQUEST;
        if (ex instanceof BusinessException) return HttpStatus.CONFLICT;
        if (ex instanceof InvalidRequestException) return HttpStatus.BAD_REQUEST;
        return HttpStatus.INTERNAL_SERVER_ERROR;
    }

    private String resolveMessage(Throwable ex) {
        if (ex instanceof WebExchangeBindException e) return BINDING_ERROR + e.getMessage();
        if (ex instanceof ServerWebInputException e) return INVALID_REQUEST_FORMAT + e.getMessage();
        if (ex instanceof ResponseStatusException e) return e.getReason();
        if (ex instanceof InvalidFormatException e) return INVALID_REQUEST_FORMAT + e.getOriginalMessage();
        if (ex instanceof DecodingException e) return INVALID_REQUEST_FORMAT + e.getMessage();
        if (ex instanceof BusinessException e) return CONFLICT + e.getMessage();
        if (ex instanceof InvalidRequestException e) return INVALID_REQUEST_FORMAT + e.getMessage();
        return ex.getMessage() != null ? ex.getMessage() : UNEXPECTED_ERROR;
    }

    @Override
    public int getOrder() {
        return -1;
    }
}
