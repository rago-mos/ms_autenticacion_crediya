package co.com.crediya.api.dto;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class CreateUserRequestTest {

    private Validator validator;

    @BeforeEach
    void setUp() {
        validator = Validation.buildDefaultValidatorFactory().getValidator();
    }

    @Test
    void shouldPassValidationWithValidData() {
        CreateUserRequest request = new CreateUserRequest(
                "Rubén",
                "Gómez",
                "ruben@example.com",
                "123456789",
                LocalDate.of(1990, 1, 1),
                "Palmira",
                "3001234567",
                "ADMIN",
                new BigDecimal("5000000")
        );

        Set<ConstraintViolation<CreateUserRequest>> violations = validator.validate(request);
        assertTrue(violations.isEmpty());
    }

    @Test
    void shouldFailValidationWithInvalidEmail() {
        CreateUserRequest request = new CreateUserRequest(
                "Rubén",
                "Gómez",
                "correo-invalido",
                "123456789",
                LocalDate.of(1990, 1, 1),
                "Palmira",
                "3001234567",
                "ADMIN",
                new BigDecimal("5000000")
        );

        Set<ConstraintViolation<CreateUserRequest>> violations = validator.validate(request);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("email")));
    }

    @Test
    void shouldFailValidationWithNullFields() {
        CreateUserRequest request = new CreateUserRequest(
                "Rubén",
                "Gómez",
                null,
                "123456789",
                LocalDate.of(1990, 1, 1),
                "Palmira",
                "3001234567",
                null,
                null
        );

        Set<ConstraintViolation<CreateUserRequest>> violations = validator.validate(request);
        assertEquals(3, violations.size());
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("email")));
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("rol")));
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("baseSalary")));
    }

    @Test
    void shouldFailValidationWithInvalidPhoneNumber() {
        CreateUserRequest request = new CreateUserRequest(
                "Rubén",
                "Gómez",
                "ruben@example.com",
                "123456789",
                LocalDate.of(1990, 1, 1),
                "Palmira",
                "ABC123",
                "ADMIN",
                new BigDecimal("5000000")
        );

        Set<ConstraintViolation<CreateUserRequest>> violations = validator.validate(request);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("phoneNumber")));
    }
}
