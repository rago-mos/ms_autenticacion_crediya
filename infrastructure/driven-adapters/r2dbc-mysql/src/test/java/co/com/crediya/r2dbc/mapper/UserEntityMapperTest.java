package co.com.crediya.r2dbc.mapper;

import co.com.crediya.model.application.UserApplicationView;
import co.com.crediya.model.role.Role;
import co.com.crediya.model.user.User;
import co.com.crediya.r2dbc.entities.UserApplicationViewEntity;
import co.com.crediya.r2dbc.entities.UserEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class UserEntityMapperTest {

    private PasswordEncoder passwordEncoder;
    private UserEntityMapper mapper;

    @BeforeEach
    void setUp() {
        passwordEncoder = mock(PasswordEncoder.class);
        mapper = new UserEntityMapper(passwordEncoder);
    }

    @Test
    void shouldMapUserToUserEntityCorrectly() {
        User user = User.builder()
                .firstName("Rubén")
                .lastName("Gómez")
                .email("ruben@example.com")
                .identityDocument("123456789")
                .password("securePass")
                .birthDate(LocalDate.of(1990, 1, 1))
                .address("Palmira")
                .phoneNumber("3001234567")
                .role(new Role(1, "ADMIN", "Administrator"))
                .baseSalary(new BigDecimal("5000000"))
                .build();

        when(passwordEncoder.encode("securePass")).thenReturn("encodedPass");

        UserEntity entity = mapper.toEntity(user);

        assertEquals("Rubén", entity.getFirstName());
        assertEquals("Gómez", entity.getLastName());
        assertEquals("ruben@example.com", entity.getEmail());
        assertEquals("123456789", entity.getIdentityDocument());
        assertEquals("encodedPass", entity.getPassword());
        assertEquals(LocalDate.of(1990, 1, 1), entity.getBirthDate());
        assertEquals("Palmira", entity.getAddress());
        assertEquals("3001234567", entity.getPhoneNumber());
        assertEquals(1, entity.getRole());
        assertEquals(new BigDecimal("5000000"), entity.getBaseSalary());
    }

    @Test
    void shouldMapUserEntityToUserDomainCorrectly() {
        UserEntity entity = UserEntity.builder()
                .firstName("Rubén")
                .lastName("Gómez")
                .email("ruben@example.com")
                .identityDocument("123456789")
                .password("encodedPass")
                .birthDate(LocalDate.of(1990, 1, 1))
                .address("Palmira")
                .phoneNumber("3001234567")
                .role(1)
                .baseSalary(new BigDecimal("5000000"))
                .build();

        User user = mapper.toDomain(entity);

        assertEquals("Rubén", user.getFirstName());
        assertEquals("Gómez", user.getLastName());
        assertEquals("ruben@example.com", user.getEmail());
        assertEquals("123456789", user.getIdentityDocument());
        assertEquals("encodedPass", user.getPassword());
        assertEquals(LocalDate.of(1990, 1, 1), user.getBirthDate());
        assertEquals("Palmira", user.getAddress());
        assertEquals("3001234567", user.getPhoneNumber());
        assertEquals(1, user.getRole().getIdRol());
        assertEquals(new BigDecimal("5000000"), user.getBaseSalary());
    }

    @Test
    void shouldReturnNullRoleWhenEntityHasNoRole() {
        UserEntity entity = UserEntity.builder()
                .firstName("Rubén")
                .lastName("Gómez")
                .email("ruben@example.com")
                .identityDocument("123456789")
                .password("encodedPass")
                .birthDate(LocalDate.of(1990, 1, 1))
                .address("Palmira")
                .phoneNumber("3001234567")
                .role(null)
                .baseSalary(new BigDecimal("5000000"))
                .build();

        User user = mapper.toDomain(entity);

        assertNull(user.getRole());
    }

    @Test
    void shouldMapEntityToViewCorrectly() {

        UserApplicationViewEntity entity = UserApplicationViewEntity.builder()
                .firstName("Rubén")
                .lastName("Tester")
                .email("ruben@example.com")
                .identityDocument("123456789")
                .baseSalary(new BigDecimal("3000000"))
                .build();

        UserApplicationView view = mapper.toView(entity);

        assertNotNull(view);
        assertEquals("Rubén", view.getFirstName());
        assertEquals("Tester", view.getLastName());
        assertEquals("ruben@example.com", view.getEmail());
        assertEquals("123456789", view.getIdentityDocument());
        assertEquals(new BigDecimal("3000000"), view.getBaseSalary());
    }
}