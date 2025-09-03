package co.com.crediya.r2dbc;

import co.com.crediya.model.exception.BadCredentialsException;
import co.com.crediya.model.login.LoginDTO;
import co.com.crediya.model.role.Role;
import co.com.crediya.model.user.User;
import co.com.crediya.r2dbc.entities.UserEntity;
import co.com.crediya.r2dbc.mapper.UserEntityMapper;
import co.com.crediya.security.jwt.provider.JwtProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.reactive.TransactionalOperator;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import java.math.BigDecimal;
import java.time.LocalDate;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserReactiveRepositoryAdapterTest {

    @Mock
    private UserReactiveRepository repository;

    @Mock
    private ObjectMapper objectMapper;

    @Mock
    private TransactionalOperator transactionalOperator;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    JwtProvider  jwtProvider;

    private UserEntityMapper userEntityMapper;

    private UserReactiveRepositoryAdapter adapter;

    private final Role role = new Role(1, "ADMIN", "Administrator");
    private final User user = User.builder()
            .firstName("Rubén")
            .lastName("Gómez")
            .email("ruben@example.com")
            .identityDocument("123456789")
            .birthDate(LocalDate.of(1990, 1, 1))
            .address("Palmira")
            .phoneNumber("3001234567")
            .role(role)
            .baseSalary(new BigDecimal("5000000"))
            .build();

    private final UserEntity userEntity = UserEntity.builder()
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

    @BeforeEach
    void setUp() {
        userEntityMapper = new UserEntityMapper(passwordEncoder);
        adapter = new UserReactiveRepositoryAdapter(repository, objectMapper, userEntityMapper, transactionalOperator,
                passwordEncoder, jwtProvider);
    }

    @Test
    void shouldReturnTrueWhenEmailExists() {
        when(repository.existsByEmail("ruben@example.com")).thenReturn(Mono.just(true));

        StepVerifier.create(adapter.existsByEmail("ruben@example.com"))
                .expectNext(true)
                .verifyComplete();
    }

    @Test
    void shouldReturnFalseWhenEmailDoesNotExist() {
        when(repository.existsByEmail("ruben@example.com")).thenReturn(Mono.just(false));

        StepVerifier.create(adapter.existsByEmail("ruben@example.com"))
                .expectNext(false)
                .verifyComplete();
    }

    @Test
    void shouldReturnTrueWhenDocumentExists() {
        String document = "123456789";
        when(repository.existsByIdentityDocument(document)).thenReturn(Mono.just(true));

        StepVerifier.create(adapter.existsByIdentityDocument(document))
                .expectNext(true)
                .verifyComplete();
    }

    @Test
    void shouldReturnFalseWhenDocumentDoesNotExist() {
        String document = "987654321";
        when(repository.existsByIdentityDocument(document)).thenReturn(Mono.just(false));

        StepVerifier.create(adapter.existsByIdentityDocument(document))
                .expectNext(false)
                .verifyComplete();
    }

    @Test
    void shouldReturnTokenWhenLoginIsSuccessful() {
        LoginDTO loginDTO = LoginDTO.builder()
                .identityDocument("123456789")
                .password("securePass")
                .build();

        when(repository.findByIdentityDocument("123456789")).thenReturn(Mono.just(userEntity));
        when(passwordEncoder.matches("securePass", userEntity.getPassword())).thenReturn(true);
        when(jwtProvider.generateToken(userEntity)).thenReturn("mocked-jwt-token");

        StepVerifier.create(adapter.login(loginDTO))
                .expectNextMatches(token -> token.token().equals("mocked-jwt-token"))
                .verifyComplete();
    }

    @Test
    void shouldThrowBadCredentialsExceptionWhenLoginFails() {
        LoginDTO loginDTO = LoginDTO.builder()
                .identityDocument("123456789")
                .password("wrongPass")
                .build();

        when(repository.findByIdentityDocument("123456789")).thenReturn(Mono.just(userEntity));
        when(passwordEncoder.matches("wrongPass", userEntity.getPassword())).thenReturn(false);

        StepVerifier.create(adapter.login(loginDTO))
                .expectErrorMatches(e -> e instanceof BadCredentialsException &&
                        e.getMessage().equals("bad credentials"))
                .verify();
    }
}
