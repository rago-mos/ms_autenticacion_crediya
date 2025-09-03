package co.com.crediya.security.config;


import lombok.*;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class AccesDeniedResponse {
    private LocalDateTime timestamp;
    private int status;
    private String error;
    private String message;
}
