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
    void shouldMapCreateUserRequestToUserModelCorrectly() {
        CreateUserRequest request = new CreateUserRequest(
                "Pepe",
                "Perez",
                "pepe@gmail.com",
                "262762",
                "328472388273823",
                LocalDate.of(1990, 1, 1),
                "Cr 5 N° 798",
                "36127328237",
                1,
                new BigDecimal("87234783216")
        );

        User user = mapper.toModel(request);

        assertEquals("Pepe", user.getFirstName());
        assertEquals("Perez", user.getLastName());
        assertEquals("pepe@gmail.com", user.getEmail());
        assertEquals("328472388273823", user.getIdentityDocument());
        assertEquals("262762", user.getPassword());
        assertEquals(LocalDate.of(1990, 1, 1), user.getBirthDate());
        assertEquals("Cr 5 N° 798", user.getAddress());
        assertEquals("36127328237", user.getPhoneNumber());
        assertEquals(new BigDecimal("87234783216"), user.getBaseSalary());

        assertNotNull(user.getRole());
        assertEquals(1, user.getRole().getIdRol());
        assertEquals("", user.getRole().getName());
        assertEquals("", user.getRole().getDescription());
    }

    @Test
    void shouldMapUserModelToCreateUserResponseCorrectly() {
        Role role = Role.builder()
                .idRol(1)
                .name("CLIENTE")
                .description("Cliente estándar")
                .build();

        User user = User.builder()
                .firstName("Pepe")
                .lastName("Perez")
                .email("pepe@gmail.com")
                .identityDocument("328472388273823")
                .password("262762")
                .birthDate(LocalDate.of(1990, 1, 1))
                .address("Cr 5 N° 798")
                .phoneNumber("36127328237")
                .role(role)
                .baseSalary(new BigDecimal("87234783216"))
                .build();

        CreateUserResponse response = mapper.toResponse(user);

        assertEquals("Pepe", response.firstName());
        assertEquals("Perez", response.lastName());
        assertEquals("pepe@gmail.com", response.email());
        assertEquals("328472388273823", response.identityDocument());
        assertEquals(LocalDate.of(1990, 1, 1), response.birthDate());
        assertEquals("Cr 5 N° 798", response.address());
        assertEquals("36127328237", response.phoneNumber());
        assertEquals("CLIENTE", response.rol());
        assertEquals(new BigDecimal("87234783216"), response.baseSalary());
    }
}
