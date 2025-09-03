package co.com.crediya.api.dto;

import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.time.LocalDate;

public record CreateUserRequest(

        String firstName,

        String lastName,

        @NotNull(message = "The field email is mandatory")
        @Email(message = "The email format is not valid")
        String email,

        @NotBlank(message = "The field password is mandatory")
        @Size(min = 6, max = 12, message = "The field password must be between 6 and 12 characters")
        String password,

        @Pattern(regexp = "^\\d{1,20}$", message = "The field identityDocument must contain only numeric digits and must not exceed 20 characters")
        String identityDocument,

        LocalDate birthDate,

        String address,

        @Pattern(regexp = "^\\d+$", message = "The field phoneNumber must contain only numerical digits")
        String phoneNumber,

        @NotNull(message = "The field rol is mandatory")
        Integer idRol,

        @NotNull(message = "The field baseSalary is mandatory")
        BigDecimal baseSalary
) {
}
