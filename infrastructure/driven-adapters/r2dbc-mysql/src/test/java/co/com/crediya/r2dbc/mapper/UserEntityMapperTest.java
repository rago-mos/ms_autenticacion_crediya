package co.com.crediya.r2dbc.mapper;

import co.com.crediya.model.role.Role;
import co.com.crediya.model.user.User;
import co.com.crediya.r2dbc.entities.UserEntity;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class UserEntityMapperTest {

    private final UserEntityMapper mapper = new UserEntityMapper();

    private final Role role = new Role(1, "ADMIN", "Administrator");

    private final User user = User.builder()
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

    private final UserEntity entity = UserEntity.builder()
            .firstName("Rubén")
            .lastName("Gómez")
            .email("ruben@example.com")
            .identityDocument(123456789L)
            .birthDate(LocalDate.of(1990, 1, 1))
            .address("Palmira")
            .phoneNumber("3001234567")
            .role(1)
            .baseSalary(new BigDecimal("5000000"))
            .build();

    @Test
    void shouldMapUserToEntityCorrectly() {
        UserEntity mapped = mapper.toEntity(user);

        assertEquals(user.getFirstName(), mapped.getFirstName());
        assertEquals(user.getLastName(), mapped.getLastName());
        assertEquals(user.getEmail(), mapped.getEmail());
        assertEquals(user.getIdentityDocument(), mapped.getIdentityDocument());
        assertEquals(user.getBirthDate(), mapped.getBirthDate());
        assertEquals(user.getAddress(), mapped.getAddress());
        assertEquals(user.getPhoneNumber(), mapped.getPhoneNumber());
        assertEquals(user.getRole().getIdRol(), mapped.getRole());
        assertEquals(user.getBaseSalary(), mapped.getBaseSalary());
    }

    @Test
    void shouldMapEntityToUserCorrectly() {
        User mapped = mapper.toDomain(entity);

        assertEquals(entity.getFirstName(), mapped.getFirstName());
        assertEquals(entity.getLastName(), mapped.getLastName());
        assertEquals(entity.getEmail(), mapped.getEmail());
        assertEquals(entity.getIdentityDocument(), mapped.getIdentityDocument());
        assertEquals(entity.getBirthDate(), mapped.getBirthDate());
        assertEquals(entity.getAddress(), mapped.getAddress());
        assertEquals(entity.getPhoneNumber(), mapped.getPhoneNumber());
        assertEquals(entity.getRole(), mapped.getRole().getIdRol());
        assertEquals(entity.getBaseSalary(), mapped.getBaseSalary());
    }

    @Test
    void shouldHandleNullRoleInUserToEntity() {
        User userWithoutRole = user.toBuilder().role(null).build();

        UserEntity mapped = mapper.toEntity(userWithoutRole);

        assertNull(mapped.getRole());
    }

    @Test
    void shouldHandleNullRoleInEntityToUser() {
        UserEntity entityWithoutRole = entity.builder().role(null).build();

        User mapped = mapper.toDomain(entityWithoutRole);

        assertNull(mapped.getRole());
    }
}