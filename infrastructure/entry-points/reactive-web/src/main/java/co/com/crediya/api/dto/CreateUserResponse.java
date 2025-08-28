package co.com.crediya.api.dto;

import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalDate;

@Builder
public record CreateUserResponse (

        String firstName,
        String lastName,
        String email,
        Long identityDocument,
        LocalDate birthDate,
        String address,
        String phoneNumber,
        String rol,
        BigDecimal baseSalary
) {
}
