package co.com.crediya.usecase.createuser.validator;

import co.com.crediya.usecase.createuser.exception.BusinessException;
import co.com.crediya.usecase.createuser.exception.InvalidRequestException;
import org.junit.jupiter.api.Test;
import reactor.test.StepVerifier;

import java.lang.reflect.Constructor;
import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class UserValidatorTest {

    @Test
    void shouldCoverPrivateConstructor() throws Exception {
        Constructor<UserValidator> constructor = UserValidator.class.getDeclaredConstructor();
        constructor.setAccessible(true);
        UserValidator instance = constructor.newInstance();
        assertNotNull(instance);
    }

    @Test
    void shouldPassFirstNameValidation() {
        StepVerifier.create(UserValidator.validateFirtsName("Rubén"))
                .verifyComplete();
    }

    @Test
    void shouldFailFirstNameValidationWhenNull() {
        StepVerifier.create(UserValidator.validateFirtsName(null))
                .expectErrorMatches(e -> e instanceof InvalidRequestException &&
                        e.getMessage().equals("firtsName is null or blank"))
                .verify();
    }

    @Test
    void shouldFailFirstNameValidationWhenBlank() {
        StepVerifier.create(UserValidator.validateFirtsName("   "))
                .expectErrorMatches(e -> e instanceof InvalidRequestException &&
                        e.getMessage().equals("firtsName is null or blank"))
                .verify();
    }

    @Test
    void shouldPassLastNameValidation() {
        StepVerifier.create(UserValidator.validateLastName("Gómez"))
                .verifyComplete();
    }

    @Test
    void shouldFailLastNameValidationWhenNull() {
        StepVerifier.create(UserValidator.validateLastName(null))
                .expectErrorMatches(e -> e instanceof InvalidRequestException &&
                        e.getMessage().equals("Last name is null or blank"))
                .verify();
    }

    @Test
    void shouldFailLastNameValidationWhenBlank() {
        StepVerifier.create(UserValidator.validateLastName(" "))
                .expectErrorMatches(e -> e instanceof InvalidRequestException &&
                        e.getMessage().equals("Last name is null or blank"))
                .verify();
    }

    @Test
    void shouldPassSalaryValidationWithinRange() {
        StepVerifier.create(UserValidator.validateSalary(new BigDecimal("1000000")))
                .verifyComplete();
    }

    @Test
    void shouldFailSalaryValidationWhenNull() {
        StepVerifier.create(UserValidator.validateSalary(null))
                .expectErrorMatches(e -> e instanceof BusinessException &&
                        e.getMessage().contains("The salary is not valid"))
                .verify();
    }

    @Test
    void shouldFailSalaryValidationWhenTooLow() {
        StepVerifier.create(UserValidator.validateSalary(new BigDecimal("-1")))
                .expectErrorMatches(e -> e instanceof BusinessException &&
                        e.getMessage().contains("The salary is not valid"))
                .verify();
    }

    @Test
    void shouldFailSalaryValidationWhenTooHigh() {
        StepVerifier.create(UserValidator.validateSalary(new BigDecimal("20000000")))
                .expectErrorMatches(e -> e instanceof BusinessException &&
                        e.getMessage().contains("The salary is not valid"))
                .verify();
    }

}