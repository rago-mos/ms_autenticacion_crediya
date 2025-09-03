package co.com.crediya.api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record LoginRequest(

        @Pattern(regexp = "^\\d{1,20}$", message = "The field identityDocument must contain only numeric digits and must not exceed 20 characters")
        String identityDocument,

        @NotBlank(message = "The field password is mandatory")
        @Size(min = 6, max = 12, message = "The field password must be between 6 and 12 characters")
        String password

) {
}
