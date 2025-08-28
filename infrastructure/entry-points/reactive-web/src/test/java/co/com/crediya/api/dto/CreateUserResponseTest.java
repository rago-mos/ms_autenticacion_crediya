package co.com.crediya.api.dto;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class CreateUserResponseTest {

    @Test
    void shouldBuildCreateUserResponseCorrectly() {
        CreateUserResponse response = CreateUserResponse.builder()
                .firstName("Rubén")
                .lastName("Gómez")
                .email("ruben@example.com")
                .identityDocument("123456789")
                .birthDate(LocalDate.of(1990, 1, 1))
                .address("Palmira")
                .phoneNumber("3001234567")
                .rol("ADMIN")
                .baseSalary(new BigDecimal("5000000"))
                .build();

        assertEquals("Rubén", response.firstName());
        assertEquals("Gómez", response.lastName());
        assertEquals("ruben@example.com", response.email());
        assertEquals("123456789", response.identityDocument());
        assertEquals(LocalDate.of(1990, 1, 1), response.birthDate());
        assertEquals("Palmira", response.address());
        assertEquals("3001234567", response.phoneNumber());
        assertEquals("ADMIN", response.rol());
        assertEquals(new BigDecimal("5000000"), response.baseSalary());
    }

    @Test
    void shouldSupportEqualityAndHashCode() {
        CreateUserResponse r1 = CreateUserResponse.builder()
                .firstName("Rubén")
                .lastName("Gómez")
                .email("ruben@example.com")
                .identityDocument("123456789")
                .birthDate(LocalDate.of(1990, 1, 1))
                .address("Palmira")
                .phoneNumber("3001234567")
                .rol("ADMIN")
                .baseSalary(new BigDecimal("5000000"))
                .build();

        CreateUserResponse r2 = CreateUserResponse.builder()
                .firstName("Rubén")
                .lastName("Gómez")
                .email("ruben@example.com")
                .identityDocument("123456789")
                .birthDate(LocalDate.of(1990, 1, 1))
                .address("Palmira")
                .phoneNumber("3001234567")
                .rol("ADMIN")
                .baseSalary(new BigDecimal("5000000"))
                .build();

        assertEquals(r1, r2);
        assertEquals(r1.hashCode(), r2.hashCode());
    }
}
