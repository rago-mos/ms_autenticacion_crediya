package co.com.crediya.api.dto;

import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.time.LocalDate;

public record CreateUserRequest(

        String firstName,

        String lastName,

        @NotNull(message = "The field is mandatory")
        @Email(message = "The email format is not valid")
        String email,

        Long identityDocument,

        LocalDate birthDate,

        String address,

        @Pattern(regexp = "^\\d+$", message = "The field must contain only numerical digits")
        String phoneNumber,

        @NotNull(message = "The field is mandatory")
        String rol,

        @NotNull(message = "The field is mandatory")
        BigDecimal baseSalary
) {
}
