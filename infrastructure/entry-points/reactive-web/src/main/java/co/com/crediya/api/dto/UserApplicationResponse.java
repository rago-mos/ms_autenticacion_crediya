package co.com.crediya.api.dto;

import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record UserApplicationResponse(

        String firstName,
        String lastName,
        String email,
        String identityDocument,
        BigDecimal baseSalary
) {
}
