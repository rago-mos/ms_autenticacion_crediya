package co.com.crediya.r2dbc.entities;

import static org.junit.jupiter.api.Assertions.*;

import co.com.crediya.r2dbc.enums.RoleEnum;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import java.util.Collection;

class UserEntityTest {

    @Test
    void shouldBuildUserEntityCorrectly() {
        UserEntity user = UserEntity.builder()
                .id(1L)
                .firstName("Rubén")
                .lastName("Gómez")
                .email("ruben@example.com")
                .identityDocument("123456789")
                .password("securePass")
                .birthDate(LocalDate.of(1990, 1, 1))
                .address("Palmira")
                .phoneNumber("3001234567")
                .role(1)
                .baseSalary(new BigDecimal("5000000"))
                .build();

        assertEquals("Rubén", user.getFirstName());
        assertEquals("Gómez", user.getLastName());
        assertEquals("ruben@example.com", user.getEmail());
        assertEquals("123456789", user.getUsername());
        assertEquals("securePass", user.getPassword());
        assertEquals(1, user.getRole());
        assertEquals(new BigDecimal("5000000"), user.getBaseSalary());
    }

    @Test
    void shouldReturnAuthoritiesBasedOnRoleEnum() {
        UserEntity user = UserEntity.builder()
                .identityDocument("123456789")
                .password("securePass")
                .role(RoleEnum.ADMIN.ordinal() + 1) // ID 1
                .build();

        Collection<? extends GrantedAuthority> authorities = user.getAuthorities();

        assertEquals(1, authorities.size());
        assertTrue(authorities.contains(new SimpleGrantedAuthority("ADMIN")));
    }

    @Test
    void shouldReturnEmptyAuthoritiesWhenRoleIsInvalid() {
        UserEntity user = UserEntity.builder()
                .identityDocument("123456789")
                .password("securePass")
                .role(99) // ID no válido
                .build();

        Collection<? extends GrantedAuthority> authorities = user.getAuthorities();

        assertTrue(authorities.isEmpty());
    }

    @Test
    void shouldReturnEmptyAuthoritiesWhenRoleIsNull() {
        UserEntity user = UserEntity.builder()
                .identityDocument("123456789")
                .password("securePass")
                .role(null)
                .build();

        Collection<? extends GrantedAuthority> authorities = user.getAuthorities();

        assertTrue(authorities.isEmpty());
    }
}