package co.com.crediya.api.mapper;

import co.com.crediya.api.dto.CreateUserRequest;
import co.com.crediya.api.dto.CreateUserResponse;
import co.com.crediya.model.role.Role;
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
                "123456789",
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
                "123456789",
                LocalDate.of(1990, 1, 1),
                "Palmira",
                "3001234567",
                null,
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
                "123456789",
                LocalDate.of(1990, 1, 1),
                "Palmira",
                "3001234567",
                "   ",
                new BigDecimal("5000000")
        );

        User user = mapper.toModel(dto);

        assertNull(user.getRole());
    }

    @Test
    void shouldMapUserToCreateUserResponseCorrectly() {
        User user = User.builder()
                .firstName("Rubén")
                .lastName("Gómez")
                .email("ruben@example.com")
                .identityDocument("123456789")
                .birthDate(LocalDate.of(1990, 1, 1))
                .address("Palmira")
                .phoneNumber("3001234567")
                .role(Role.builder().name("ADMIN").description("Admin role").build())
                .baseSalary(new BigDecimal("5000000"))
                .build();

        CreateUserResponse response = mapper.toResponse(user);

        assertEquals(user.getFirstName(), response.firstName());
        assertEquals(user.getLastName(), response.lastName());
        assertEquals(user.getEmail(), response.email());
        assertEquals(user.getIdentityDocument(), response.identityDocument());
        assertEquals(user.getBirthDate(), response.birthDate());
        assertEquals(user.getAddress(), response.address());
        assertEquals(user.getPhoneNumber(), response.phoneNumber());
        assertEquals(user.getRole().getName(), response.rol());
        assertEquals(user.getBaseSalary(), response.baseSalary());
    }

    @Test
    void shouldReturnNullRolInResponseWhenUserRoleIsNull() {
        User user = User.builder()
                .firstName("Rubén")
                .lastName("Gómez")
                .email("ruben@example.com")
                .identityDocument("123456789")
                .birthDate(LocalDate.of(1990, 1, 1))
                .address("Palmira")
                .phoneNumber("3001234567")
                .role(null)
                .baseSalary(new BigDecimal("5000000"))
                .build();

        CreateUserResponse response = mapper.toResponse(user);

        assertNull(response.rol());
    }
}
