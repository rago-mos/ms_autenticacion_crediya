package co.com.crediya.api.validator;

import static org.junit.jupiter.api.Assertions.*;

import co.com.crediya.api.dto.CreateUserRequest;
import jakarta.validation.Validation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import reactor.test.StepVerifier;
import java.math.BigDecimal;
import java.time.LocalDate;
import jakarta.validation.Validator;

class RequestValidatorTest {

    private Validator validator;

    @BeforeEach
    void setUp() {
        validator = Validation.buildDefaultValidatorFactory().getValidator();
    }

    @Test
    void shouldPassValidationSuccessfully() {
        CreateUserRequest request = new CreateUserRequest(
                "Rubén",
                "Gómez",
                "ruben@example.com",
                123456789L,
                LocalDate.of(1990, 1, 1),
                "Palmira",
                "3001234567",
                "ADMIN",
                new BigDecimal("5000000")
        );

        StepVerifier.create(RequestValidator.validate(request, validator))
                .expectNext(request)
                .verifyComplete();
    }

    @Test
    void shouldFailValidationDueToInvalidEmail() {
        CreateUserRequest request = new CreateUserRequest(
                "Rubén",
                "Gómez",
                "correo-invalido",
                123456789L,
                LocalDate.of(1990, 1, 1),
                "Palmira",
                "3001234567",
                "ADMIN",
                new BigDecimal("5000000")
        );

        StepVerifier.create(RequestValidator.validate(request, validator))
                .expectErrorMatches(e -> e instanceof IllegalArgumentException &&
                        e.getMessage().contains("email: The email format is not valid"))
                .verify();
    }

    @Test
    void shouldFailValidationDueToMissingFields() {
        CreateUserRequest request = new CreateUserRequest(
                "Rubén",
                "Gómez",
                null,
                123456789L,
                LocalDate.of(1990, 1, 1),
                "Palmira",
                "300ABC",
                null,
                null
        );

        StepVerifier.create(RequestValidator.validate(request, validator))
                .expectErrorMatches(e -> e instanceof IllegalArgumentException &&
                        e.getMessage().contains("email: The field is mandatory") &&
                        e.getMessage().contains("phoneNumber: The field must contain only numerical digits") &&
                        e.getMessage().contains("rol: The field is mandatory") &&
                        e.getMessage().contains("baseSalary: The field is mandatory"))
                .verify();
    }

    @Test
    void shouldCoverPrivateConstructor() throws Exception {
        var constructor = RequestValidator.class.getDeclaredConstructor();
        constructor.setAccessible(true);
        constructor.newInstance();
    }
}