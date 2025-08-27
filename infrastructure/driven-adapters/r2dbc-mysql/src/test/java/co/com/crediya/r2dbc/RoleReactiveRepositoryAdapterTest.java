package co.com.crediya.r2dbc;

import co.com.crediya.model.role.Role;
import co.com.crediya.r2dbc.entities.RoleEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.reactivecommons.utils.ObjectMapper;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RoleReactiveRepositoryAdapterTest {

    @Mock
    private RoleReactiveRepository repository;

    @Mock
    private ObjectMapper mapper;

    private RoleReactiveRepositoryAdapter adapter;

    private final RoleEntity roleEntity = RoleEntity.builder()
            .idRol(1)
            .name("ADMIN")
            .description("Administrator role")
            .build();

    private final Role role = Role.builder()
            .idRol(1)
            .name("ADMIN")
            .description("Administrator role")
            .build();

    @BeforeEach
    void setUp() {
        adapter = new RoleReactiveRepositoryAdapter(repository, mapper);
    }

    @Test
    void shouldFindRoleByNameSuccessfully() {
        when(repository.findByName("ADMIN")).thenReturn(Mono.just(roleEntity));
        when(mapper.map(roleEntity, Role.class)).thenReturn(role);

        StepVerifier.create(adapter.findByName("ADMIN"))
                .expectNext(role)
                .verifyComplete();

        verify(repository).findByName("ADMIN");
        verify(mapper).map(roleEntity, Role.class);
    }

    @Test
    void shouldFailWhenRoleNotFound() {
        when(repository.findByName("ADMIN")).thenReturn(Mono.empty());

        StepVerifier.create(adapter.findByName("ADMIN"))
                .expectErrorMatches(e -> e instanceof Exception &&
                        e.getMessage().contains("Role not found"))
                .verify();

        verify(repository).findByName("ADMIN");
        verifyNoInteractions(mapper);
    }
}