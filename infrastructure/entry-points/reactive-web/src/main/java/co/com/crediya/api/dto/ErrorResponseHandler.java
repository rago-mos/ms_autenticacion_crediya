package co.com.crediya.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Schema(description = "Structured error response")
@Builder
public class ErrorResponseHandler {

    private LocalDateTime timestamp;
    private int status;
    private String error;
    private String message;
}
