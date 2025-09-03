package co.com.crediya.security.config;

import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import static org.assertj.core.api.Assertions.assertThat;

class AccesDeniedResponseTest {

    @Test
    void shouldCreateObjectUsingBuilder() {
        LocalDateTime now = LocalDateTime.now();
        AccesDeniedResponse response = AccesDeniedResponse.builder()
                .timestamp(now)
                .status(403)
                .error("Forbidden")
                .message("Access is denied")
                .build();

        assertThat(response.getTimestamp()).isEqualTo(now);
        assertThat(response.getStatus()).isEqualTo(403);
        assertThat(response.getError()).isEqualTo("Forbidden");
        assertThat(response.getMessage()).isEqualTo("Access is denied");
    }

    @Test
    void shouldSetAndGetFields() {
        AccesDeniedResponse response = new AccesDeniedResponse();
        LocalDateTime now = LocalDateTime.now();

        response.setTimestamp(now);
        response.setStatus(401);
        response.setError("Unauthorized");
        response.setMessage("Token expired");

        assertThat(response.getTimestamp()).isEqualTo(now);
        assertThat(response.getStatus()).isEqualTo(401);
        assertThat(response.getError()).isEqualTo("Unauthorized");
        assertThat(response.getMessage()).isEqualTo("Token expired");
    }

    @Test
    void shouldUseAllArgsConstructor() {
        LocalDateTime now = LocalDateTime.now();
        AccesDeniedResponse response = new AccesDeniedResponse(now, 403, "Forbidden", "Access denied");

        assertThat(response.getTimestamp()).isEqualTo(now);
        assertThat(response.getStatus()).isEqualTo(403);
        assertThat(response.getError()).isEqualTo("Forbidden");
        assertThat(response.getMessage()).isEqualTo("Access denied");
    }
}

