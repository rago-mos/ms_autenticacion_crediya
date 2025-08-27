package co.com.crediya.model.user;

import co.com.crediya.model.role.Role;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class UserTest {

    @Test
    void shouldCreateUserWithBuilder() {
        Role role = new Role(1, "ADMIN", "ROLE_ADMIN");

        User user = User.builder()
                .firstName("Rubén")
                .lastName("Gómez")
                .email("ruben@example.com")
                .identityDocument(123456789L)
                .birthDate(LocalDate.of(1990, 1, 1))
                .address("Palmira")
                .phoneNumber("3001234567")
                .role(role)
                .baseSalary(new BigDecimal("5000000"))
                .build();

        assertEquals("Rubén", user.getFirstName());
        assertEquals("Gómez", user.getLastName());
        assertEquals("ruben@example.com", user.getEmail());
        assertEquals(123456789L, user.getIdentityDocument());
        assertEquals(LocalDate.of(1990, 1, 1), user.getBirthDate());
        assertEquals("Palmira", user.getAddress());
        assertEquals("3001234567", user.getPhoneNumber());
        assertEquals(role, user.getRole());
        assertEquals(new BigDecimal("5000000"), user.getBaseSalary());
    }

    @Test
    void shouldUseSettersAndGetters() {
        User user = new User();
        user.setFirstName("Rubén");
        user.setLastName("Gómez");

        assertEquals("Rubén", user.getFirstName());
        assertEquals("Gómez", user.getLastName());
    }

    @Test
    void shouldCopyUserWithToBuilder() {
        User original = User.builder()
                .firstName("Rubén")
                .lastName("Gómez")
                .email("ruben@example.com")
                .build();

        User copy = original.toBuilder()
                .email("nuevo@example.com")
                .build();

        assertEquals("Rubén", copy.getFirstName());
        assertEquals("Gómez", copy.getLastName());
        assertEquals("nuevo@example.com", copy.getEmail());
    }

    @Test
    void shouldCreateUserWithAllArgsConstructor() {
        Role role = new Role(1, "ADMIN", "ROLE_ADMIN");
        User user = new User("Rubén", "Gómez", "ruben@example.com", 123L,
                LocalDate.of(1990, 1, 1), "Palmira", "3001234567", role, new BigDecimal("5000000"));

        assertNotNull(user);
        assertEquals("Rubén", user.getFirstName());
    }

    @Test
    void shouldCreateUserWithNoArgsConstructor() {
        User user = new User();
        assertNotNull(user);
    }

}