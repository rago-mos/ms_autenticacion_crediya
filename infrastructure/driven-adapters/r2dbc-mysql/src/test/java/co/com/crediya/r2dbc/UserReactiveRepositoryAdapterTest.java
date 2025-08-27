package co.com.crediya.r2dbc;

import co.com.crediya.model.role.Role;
import co.com.crediya.model.user.User;
import co.com.crediya.r2dbc.entities.UserEntity;
import co.com.crediya.r2dbc.mapper.UserEntityMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.transaction.reactive.TransactionalOperator;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import java.math.BigDecimal;
import java.time.LocalDate;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserReactiveRepositoryAdapterTest {

    @Mock
    private UserReactiveRepository repository;

    @Mock
    private ObjectMapper objectMapper;

    @Mock
    private TransactionalOperator transactionalOperator;

    private UserEntityMapper userEntityMapper;

    private UserReactiveRepositoryAdapter adapter;

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

    private final UserEntity userEntity = UserEntity.builder()
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

    @BeforeEach
    void setUp() {
        userEntityMapper = new UserEntityMapper();
        adapter = new UserReactiveRepositoryAdapter(repository, objectMapper, userEntityMapper, transactionalOperator);
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
}
