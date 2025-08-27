package co.com.crediya.usecase.createuser.validator;

import co.com.crediya.usecase.createuser.exception.BusinessException;
import co.com.crediya.usecase.createuser.exception.InvalidRequestException;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;

public class UserValidator {

    private static final BigDecimal MIN_SALARY = BigDecimal.ZERO;
    private static final BigDecimal MAX_SALARY = new BigDecimal("15000000");

    private UserValidator() {
        // Evita instanciación
    }

    public static Mono<Void> validateFirtsName(String name) {
        if (name == null || name.isBlank()) {
            return Mono.error(new InvalidRequestException("firtsName is null or blank"));
        }
        return Mono.empty();
    }

    public static Mono<Void> validateLastName(String lastName) {
        if (lastName == null || lastName.isBlank()) {
            return Mono.error(new InvalidRequestException("Last name is null or blank"));
        }
        return Mono.empty();
    }

    public static Mono<Void> validateSalary(BigDecimal salary) {
        if (salary == null ||
                salary.compareTo(MIN_SALARY) < 0 ||
                salary.compareTo(MAX_SALARY) > 0) {
            return Mono.error(new BusinessException(
                    String.format("The salary is not valid; it must be between %s and %s",
                            MIN_SALARY, MAX_SALARY)));
        }
        return Mono.empty();
    }
}
