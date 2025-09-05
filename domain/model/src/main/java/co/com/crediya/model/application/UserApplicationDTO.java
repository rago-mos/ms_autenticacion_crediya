package co.com.crediya.model.application;

import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record UserApplicationDTO(

        String firstName,
        String lastName,
        String email,
        String identityDocument,
        BigDecimal baseSalary
) {
}
