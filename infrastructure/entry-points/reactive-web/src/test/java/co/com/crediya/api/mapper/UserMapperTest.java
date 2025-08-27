package co.com.crediya.api.mapper;

import co.com.crediya.api.dto.CreateUserRequest;
import co.com.crediya.model.user.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class UserMapperTest {

    private UserMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = new UserMapper();
    }

    @Test
    void shouldMapCreateUserRequestToUserCorrectly() {
        CreateUserRequest dto = new CreateUserRequest(
                "Rubén",
                "Gómez",
                "ruben@example.com",
                123456789L,
                LocalDate.of(1990, 1, 1),
                "Palmira",
                "3001234567",
                "ADMIN",
                new BigDecimal("5000000")
        );

        User user = mapper.toModel(dto);

        assertEquals(dto.firstName(), user.getFirstName());
        assertEquals(dto.lastName(), user.getLastName());
        assertEquals(dto.email(), user.getEmail());
        assertEquals(dto.identityDocument(), user.getIdentityDocument());
        assertEquals(dto.birthDate(), user.getBirthDate());
        assertEquals(dto.address(), user.getAddress());
        assertEquals(dto.phoneNumber(), user.getPhoneNumber());
        assertEquals(dto.baseSalary(), user.getBaseSalary());

        assertNotNull(user.getRole());
        assertEquals("ADMIN", user.getRole().getName());
        assertEquals("", user.getRole().getDescription());
    }

    @Test
    void shouldReturnNullRoleWhenRoleNameIsNull() {
        CreateUserRequest dto = new CreateUserRequest(
                "Rubén",
                "Gómez",
                "ruben@example.com",
                123456789L,
                LocalDate.of(1990, 1, 1),
                "Palmira",
                "3001234567",
                null, // 👈 rol nulo
                new BigDecimal("5000000")
        );

        User user = mapper.toModel(dto);

        assertNull(user.getRole());
    }

    @Test
    void shouldReturnNullRoleWhenRoleNameIsBlank() {
        CreateUserRequest dto = new CreateUserRequest(
                "Rubén",
                "Gómez",
                "ruben@example.com",
                123456789L,
                LocalDate.of(1990, 1, 1),
                "Palmira",
                "3001234567",
                "   ", // 👈 rol en blanco
                new BigDecimal("5000000")
        );

        User user = mapper.toModel(dto);

        assertNull(user.getRole());
    }
}
