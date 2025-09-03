package co.com.crediya.api.dto;

import co.com.crediya.api.validator.RequestValidator;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import reactor.test.StepVerifier;

class LoginRequestTest {

    private Validator validator;

    @BeforeEach
    void setUp() {
        validator = Validation.buildDefaultValidatorFactory().getValidator();
    }

    @Test
    void shouldPassValidationSuccessfully() {
        LoginRequest request = new LoginRequest("123456789", "securePass");

        StepVerifier.create(RequestValidator.validate(request, validator))
                .expectNext(request)
                .verifyComplete();
    }

    @Test
    void shouldFailValidationDueToInvalidIdentityDocument() {
        LoginRequest request = new LoginRequest("ABC123XYZ", "securePass");

        StepVerifier.create(RequestValidator.validate(request, validator))
                .expectErrorMatches(e -> e instanceof IllegalArgumentException &&
                        e.getMessage().contains("identityDocument: The field identityDocument must contain only numeric digits and must not exceed 20 characters"))
                .verify();
    }

    @Test
    void shouldFailValidationDueToEmptyPassword() {
        LoginRequest request = new LoginRequest("123456789", "");

        StepVerifier.create(RequestValidator.validate(request, validator))
                .expectErrorMatches(e -> e instanceof IllegalArgumentException &&
                        e.getMessage().contains("password: The field password is mandatory"))
                .verify();
    }

    @Test
    void shouldFailValidationDueToPasswordTooShort() {
        LoginRequest request = new LoginRequest("123456789", "123");

        StepVerifier.create(RequestValidator.validate(request, validator))
                .expectErrorMatches(e -> e instanceof IllegalArgumentException &&
                        e.getMessage().contains("password: The field password must be between 6 and 12 characters"))
                .verify();
    }

    @Test
    void shouldFailValidationDueToPasswordTooLong() {
        LoginRequest request = new LoginRequest("123456789", "1234567890123");

        StepVerifier.create(RequestValidator.validate(request, validator))
                .expectErrorMatches(e -> e instanceof IllegalArgumentException &&
                        e.getMessage().contains("password: The field password must be between 6 and 12 characters"))
                .verify();
    }
}