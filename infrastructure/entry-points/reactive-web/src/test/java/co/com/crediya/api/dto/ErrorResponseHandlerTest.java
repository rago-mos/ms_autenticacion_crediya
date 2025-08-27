package co.com.crediya.api.dto;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class ErrorResponseHandlerTest {

    @Test
    void shouldBuildErrorResponseCorrectly() {
        LocalDateTime now = LocalDateTime.now();

        ErrorResponseHandler response = ErrorResponseHandler.builder()
                .timestamp(now)
                .status(404)
                .error("Not Found")
                .message("User not found")
                .build();

        assertEquals(now, response.getTimestamp());
        assertEquals(404, response.getStatus());
        assertEquals("Not Found", response.getError());
        assertEquals("User not found", response.getMessage());
    }

    @Test
    void shouldUseSettersAndGetters() {
        ErrorResponseHandler response = new ErrorResponseHandler();
        response.setStatus(500);
        response.setMessage("Internal Server Error");

        assertEquals(500, response.getStatus());
        assertEquals("Internal Server Error", response.getMessage());
    }

    @Test
    void shouldCopyWithToBuilder() {
        ErrorResponseHandler original = ErrorResponseHandler.builder()
                .status(403)
                .message("Forbidden")
                .build();

        ErrorResponseHandler copy = original.builder()
                .status(403)
                .message("Access denied")
                .build();

        assertEquals(403, copy.getStatus());
        assertEquals("Access denied", copy.getMessage());
    }

    @Test
    void shouldCreateWithAllArgsConstructor() {
        LocalDateTime now = LocalDateTime.now();
        ErrorResponseHandler response = new ErrorResponseHandler(now, 400, "Bad Request", "Invalid input");

        assertEquals(now, response.getTimestamp());
        assertEquals(400, response.getStatus());
        assertEquals("Bad Request", response.getError());
        assertEquals("Invalid input", response.getMessage());
    }

    @Test
    void shouldCreateWithNoArgsConstructor() {
        ErrorResponseHandler response = new ErrorResponseHandler();
        assertNotNull(response);
    }
}
