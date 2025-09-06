package co.com.crediya.usecase.createuser;

import co.com.crediya.model.application.UserApplicationView;
import co.com.crediya.model.role.Role;
import co.com.crediya.model.role.gateways.RoleRepository;
import co.com.crediya.model.user.User;
import co.com.crediya.model.user.gateways.UserRepository;
import co.com.crediya.model.exception.BusinessException;
import co.com.crediya.model.exception.InvalidRequestException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CreateUserUseCaseTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleRepository roleRepository;

    private CreateUserUseCase useCase;

    private final Role validRole = new Role(3, "ADMIN", "ROLE_ADMIN");
    private final User user = User.builder()
            .firstName("Rubén")
            .lastName("Gómez")
            .email("ruben@example.com")
            .identityDocument("123456789")
            .password("securePass")
            .birthDate(LocalDate.of(1990, 1, 1))
            .address("Palmira")
            .phoneNumber("3001234567")
            .role(validRole)
            .baseSalary(new BigDecimal("5000000"))
            .build();

    @BeforeEach
    void setUp() {
        useCase = new CreateUserUseCase(userRepository, roleRepository);
    }

    @Test
    void shouldCreateUserSuccessfully() {
        when(userRepository.existsByEmail(user.getEmail())).thenReturn(Mono.just(false));
        when(userRepository.existsByIdentityDocument(user.getIdentityDocument())).thenReturn(Mono.just(false));
        when(userRepository.saveUser(user)).thenReturn(Mono.just(user));
        when(roleRepository.findById(validRole.getIdRol())).thenReturn(Mono.just(validRole));

        StepVerifier.create(useCase.execute(user))
                .assertNext(created -> {
                    assertEquals("Rubén", created.getFirstName());
                    assertEquals("ADMIN", created.getRole().getName());
                })
                .verifyComplete();
    }

    @Test
    void shouldThrowExceptionWhenEmailAlreadyExists() {
        when(userRepository.existsByEmail(user.getEmail())).thenReturn(Mono.just(true));

        StepVerifier.create(useCase.execute(user))
                .expectErrorMatches(e -> e instanceof BusinessException &&
                        e.getMessage().contains("A user with this email already exists"))
                .verify();
    }

    @Test
    void shouldThrowExceptionWhenDocumentAlreadyExists() {
        when(userRepository.existsByEmail(user.getEmail())).thenReturn(Mono.just(false));
        when(userRepository.existsByIdentityDocument(user.getIdentityDocument())).thenReturn(Mono.just(true));

        StepVerifier.create(useCase.execute(user))
                .expectErrorMatches(e -> e instanceof BusinessException &&
                        e.getMessage().contains("A user with this document already exists"))
                .verify();
    }

    @Test
    void shouldReturnTrueWhenDocumentExists() {
        when(userRepository.existsByIdentityDocument("123456789")).thenReturn(Mono.just(true));

        StepVerifier.create(useCase.existsUserByDocument("123456789"))
                .expectNext(true)
                .verifyComplete();
    }

    @Test
    void shouldReturnFalseWhenDocumentDoesNotExist() {
        when(userRepository.existsByIdentityDocument("987654321")).thenReturn(Mono.just(false));

        StepVerifier.create(useCase.existsUserByDocument("987654321"))
                .expectNext(false)
                .verifyComplete();
    }

    @Test
    void shouldThrowNullPointerWhenRoleIsNull() {
        User userWithoutRole = user.toBuilder().role(null).build();

        assertThrows(NullPointerException.class, () -> useCase.execute(userWithoutRole).block());
    }

    @Test
    void shouldThrowNullPointerWhenRoleNameIsNull() {
        User userWithEmptyRoleName = user.toBuilder()
                .role(new Role(3, null, "ROLE_ADMIN"))
                .build();

        assertThrows(NullPointerException.class, () -> useCase.execute(userWithEmptyRoleName).block());
    }

    @Test
    void shouldReturnTrueWhenUserExistsByDocument() {

        String document = "328472388273823";
        when(userRepository.existsByIdentityDocument(document)).thenReturn(Mono.just(true));

        Mono<Boolean> result = useCase.existsUserByDocument(document);
        StepVerifier.create(result)
                .expectNext(true)
                .verifyComplete();

        verify(userRepository).existsByIdentityDocument(document);
    }

    @Test
    void shouldReturnFalseWhenUserDoesNotExistByDocument() {

        String document = "000000000000000";
        when(userRepository.existsByIdentityDocument(document)).thenReturn(Mono.just(false));

        Mono<Boolean> result = useCase.existsUserByDocument(document);
        StepVerifier.create(result)
                .expectNext(false)
                .verifyComplete();

        verify(userRepository).existsByIdentityDocument(document);
    }

    @Test
    void shouldReturnUserApplicationViewsFromRepository() {
        // Arrange
        List<String> documents = List.of("123456789", "987654321");

        UserApplicationView view1 = UserApplicationView.builder()
                .firstName("Rubén")
                .lastName("Tester")
                .email("ruben@example.com")
                .identityDocument("123456789")
                .baseSalary(new BigDecimal("3000000"))
                .build();

        UserApplicationView view2 = view1.toBuilder()
                .identityDocument("987654321")
                .email("ana@example.com")
                .firstName("Ana")
                .build();

        when(userRepository.findUsersByIdentityDocument(documents))
                .thenReturn(Flux.just(view1, view2));

        // Act & Assert
        StepVerifier.create(useCase.findUsersByIdentityDocument(documents))
                .expectNext(view1)
                .expectNext(view2)
                .verifyComplete();

        verify(userRepository).findUsersByIdentityDocument(documents);
    }
}