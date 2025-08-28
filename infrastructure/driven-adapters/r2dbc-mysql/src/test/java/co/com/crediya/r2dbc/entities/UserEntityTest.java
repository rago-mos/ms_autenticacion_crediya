package co.com.crediya.r2dbc.entities;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class UserEntityTest {

    @Test
    void shouldBuildUserEntityCorrectly() {
        UserEntity entity = UserEntity.builder()
                .id(1L)
                .firstName("Rubén")
                .lastName("Gómez")
                .email("ruben@example.com")
                .identityDocument("123456789")
                .birthDate(LocalDate.of(1990, 1, 1))
                .address("Palmira")
                .phoneNumber("3001234567")
                .role(1)
                .baseSalary(new BigDecimal("5000000"))
                .build();

        assertEquals("Rubén", entity.getFirstName());
        assertEquals("Gómez", entity.getLastName());
        assertEquals("ruben@example.com", entity.getEmail());
        assertEquals("123456789", entity.getIdentityDocument());
        assertEquals(LocalDate.of(1990, 1, 1), entity.getBirthDate());
        assertEquals("Palmira", entity.getAddress());
        assertEquals("3001234567", entity.getPhoneNumber());
        assertEquals(1, entity.getRole());
        assertEquals(new BigDecimal("5000000"), entity.getBaseSalary());
    }

    @Test
    void shouldUseSettersAndGetters() {
        UserEntity entity = new UserEntity();
        entity.setFirstName("Rubén");
        entity.setLastName("Gómez");

        assertEquals("Rubén", entity.getFirstName());
        assertEquals("Gómez", entity.getLastName());
    }

    @Test
    void shouldCopyUserEntityWithToBuilder() {
        UserEntity original = UserEntity.builder()
                .email("ruben@example.com")
                .build();

        UserEntity copy = original.builder()
                .email("nuevo@example.com")
                .build();

        assertEquals("nuevo@example.com", copy.getEmail());
    }

    @Test
    void shouldCreateUserEntityWithAllArgsConstructor() {
        UserEntity entity = new UserEntity(1L, "Rubén", "Gómez", "ruben@example.com", "123",
                LocalDate.of(1990, 1, 1), "Palmira", "3001234567", 1, new BigDecimal("5000000"));

        assertNotNull(entity);
        assertEquals("Rubén", entity.getFirstName());
    }

    @Test
    void shouldCreateUserEntityWithNoArgsConstructor() {
        UserEntity entity = new UserEntity();
        assertNotNull(entity);
    }
}