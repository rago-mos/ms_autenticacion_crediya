package co.com.crediya.usecase.createuser;

import co.com.crediya.model.role.Role;
import co.com.crediya.model.role.gateways.RoleRepository;
import co.com.crediya.model.user.User;
import co.com.crediya.model.user.gateways.UserRepository;
import co.com.crediya.usecase.createuser.exception.BusinessException;
import co.com.crediya.usecase.createuser.exception.InvalidRequestException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CreateUserUseCaseTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleRepository roleRepository;

    private CreateUserUseCase useCase;

    private final Role validRole = new Role(3, "ADMIN", "ROLE_ADMIN");
    private final User validUser = User.builder()
            .firstName("Rubén")
            .lastName("Gómez")
            .email("ruben@example.com")
            .baseSalary(new BigDecimal("5000000"))
            .role(validRole)
            .build();

    @BeforeEach
    void setUp() {
        useCase = new CreateUserUseCase(userRepository, roleRepository);
    }

    @Test
    void shouldCreateUserSuccessfully() {
        // Arrange
        when(roleRepository.findByName("ADMIN")).thenReturn(Mono.just(validRole));
        when(userRepository.existsByEmail(validUser.getEmail())).thenReturn(Mono.just(false));
        when(userRepository.saveUser(validUser)).thenReturn(Mono.just(validUser));

        // Act & Assert
        StepVerifier.create(useCase.execute(validUser))
                .expectNext(validUser)
                .verifyComplete();
    }

    @Test
    void shouldFailWhenFirstNameIsBlank() {
        User invalidUser = validUser.toBuilder().firstName(" ").build();

        when(roleRepository.findByName("ADMIN")).thenReturn(Mono.just(validRole));

        StepVerifier.create(useCase.execute(invalidUser))
                .expectErrorMatches(e -> e instanceof InvalidRequestException &&
                        e.getMessage().contains("firtsName is null or blank"))
                .verify();
    }

    @Test
    void shouldFailWhenLastNameIsNull() {
        User invalidUser = validUser.toBuilder().lastName(null).build();

        when(roleRepository.findByName("ADMIN")).thenReturn(Mono.just(validRole));

        StepVerifier.create(useCase.execute(invalidUser))
                .expectErrorMatches(e -> e instanceof InvalidRequestException &&
                        e.getMessage().contains("Last name is null or blank"))
                .verify();
    }

    @Test
    void shouldFailWhenSalaryIsInvalid() {
        User invalidUser = validUser.toBuilder().baseSalary(new BigDecimal("-100")).build();

        when(roleRepository.findByName("ADMIN")).thenReturn(Mono.just(validRole));

        StepVerifier.create(useCase.execute(invalidUser))
                .expectErrorMatches(e -> e instanceof BusinessException &&
                        e.getMessage().contains("The salary is not valid"))
                .verify();
    }

    @Test
    void shouldFailWhenEmailAlreadyExists() {
        when(roleRepository.findByName("ADMIN")).thenReturn(Mono.just(validRole));
        when(userRepository.existsByEmail(validUser.getEmail())).thenReturn(Mono.just(true));

        StepVerifier.create(useCase.execute(validUser))
                .expectErrorMatches(e -> e instanceof BusinessException &&
                        e.getMessage().contains("already exists"))
                .verify();
    }

    @Test
    void shouldFailWhenRoleNotFound() {
        when(roleRepository.findByName("ADMIN")).thenReturn(Mono.error(new Exception("Role not found")));

        StepVerifier.create(useCase.execute(validUser))
                .expectErrorMatches(e -> e.getMessage().contains("Role not found"))
                .verify();
    }

    @Test
    void shouldAssignRoleWithLowercaseName() {
        User userWithLowercaseRole = validUser.toBuilder()
                .role(new Role(3, "admin", "ROLE_ADMIN"))
                .build();

        when(roleRepository.findByName("ADMIN")).thenReturn(Mono.just(validRole));
        when(userRepository.existsByEmail(userWithLowercaseRole.getEmail())).thenReturn(Mono.just(false));
        when(userRepository.saveUser(userWithLowercaseRole)).thenReturn(Mono.just(userWithLowercaseRole));

        StepVerifier.create(useCase.execute(userWithLowercaseRole))
                .expectNext(userWithLowercaseRole)
                .verifyComplete();
    }

    @Test
    void shouldThrowNullPointerWhenRoleIsNull() {
        User userWithoutRole = validUser.toBuilder().role(null).build();

        assertThrows(NullPointerException.class, () -> useCase.execute(userWithoutRole).block());
    }

    @Test
    void shouldFailWhenEmailIsNull() {
        User userWithoutEmail = validUser.toBuilder().email(null).build();

        when(roleRepository.findByName("ADMIN")).thenReturn(Mono.just(validRole));

        StepVerifier.create(useCase.execute(userWithoutEmail))
                .expectErrorMatches(e -> e instanceof NullPointerException)
                .verify();
    }

    @Test
    void shouldThrowNullPointerWhenRoleNameIsNull() {
        User userWithEmptyRoleName = validUser.toBuilder()
                .role(new Role(3, null, "ROLE_ADMIN"))
                .build();

        assertThrows(NullPointerException.class, () -> useCase.execute(userWithEmptyRoleName).block());
    }

    @Test
    void shouldPropagateUnexpectedErrorOnSave() {
        when(roleRepository.findByName("ADMIN")).thenReturn(Mono.just(validRole));
        when(userRepository.existsByEmail(validUser.getEmail())).thenReturn(Mono.just(false));
        when(userRepository.saveUser(validUser)).thenReturn(Mono.error(new RuntimeException("DB error")));

        StepVerifier.create(useCase.execute(validUser))
                .expectErrorMatches(e -> e instanceof RuntimeException &&
                        e.getMessage().contains("DB error"))
                .verify();
    }
}