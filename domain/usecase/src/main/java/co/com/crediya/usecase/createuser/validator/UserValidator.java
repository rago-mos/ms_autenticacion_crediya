package co.com.crediya.usecase.createuser.validator;

import co.com.crediya.model.exception.BusinessException;
import co.com.crediya.model.exception.InvalidRequestException;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;

import static co.com.crediya.model.utils.Constant.*;

public class UserValidator {

    private static final BigDecimal MIN_SALARY = BigDecimal.ZERO;
    private static final BigDecimal MAX_SALARY = new BigDecimal("15000000");

    private UserValidator() {
        // Evita instanciación
    }

    public static Mono<Void> validateFirtsName(String name) {
        if (name == null || name.isBlank()) {
            return Mono.error(new InvalidRequestException(ERROR_REQUEST_FIRTSNAME));
        }
        return Mono.empty();
    }

    public static Mono<Void> validateLastName(String lastName) {
        if (lastName == null || lastName.isBlank()) {
            return Mono.error(new InvalidRequestException(ERROR_REQUEST_LASTNAME));
        }
        return Mono.empty();
    }

    public static Mono<Void> validateSalary(BigDecimal salary) {
        if (salary == null ||
                salary.compareTo(MIN_SALARY) < 0 ||
                salary.compareTo(MAX_SALARY) > 0) {
            return Mono.error(new BusinessException(
                    String.format(ERROR_BUSINESS_SALARY, MIN_SALARY, MAX_SALARY)));
        }
        return Mono.empty();
    }
}
