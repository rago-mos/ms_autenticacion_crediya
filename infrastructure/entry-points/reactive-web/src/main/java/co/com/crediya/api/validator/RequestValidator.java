package co.com.crediya.api.validator;

import co.com.crediya.api.dto.CreateUserRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import java.util.Set;
import java.util.stream.Collectors;

import reactor.core.publisher.Mono;

public final class RequestValidator {

    private static final Logger log = LoggerFactory.getLogger(RequestValidator.class);

    private RequestValidator() {
        // Evita instanciación
    }

    public static Mono<CreateUserRequest> validate(CreateUserRequest  request, Validator validator) {
        Set<ConstraintViolation<CreateUserRequest>> violations = validator.validate(request);

        if (violations.isEmpty()) {
            return Mono.just(request);
        }

        String message = violations.stream()
                .map(v -> v.getPropertyPath() + ": " + v.getMessage())
                .collect(Collectors.joining(", "));

        log.warn("Validation errors: {}", message);

        return Mono.error(new IllegalArgumentException(message));
    }
}
