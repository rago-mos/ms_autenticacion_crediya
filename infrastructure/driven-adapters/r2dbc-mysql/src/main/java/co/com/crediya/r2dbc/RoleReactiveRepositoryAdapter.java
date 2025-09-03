package co.com.crediya.r2dbc;

import co.com.crediya.model.exception.NotFoundException;
import co.com.crediya.model.role.Role;
import co.com.crediya.model.role.gateways.RoleRepository;
import co.com.crediya.r2dbc.entities.RoleEntity;
import co.com.crediya.r2dbc.helper.ReactiveAdapterOperations;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

import static co.com.crediya.model.utils.Constant.ERROR_ROLE;

@Repository
public class RoleReactiveRepositoryAdapter extends ReactiveAdapterOperations<
        Role,
        RoleEntity,
        Integer,
        RoleReactiveRepository
> implements RoleRepository {
    public RoleReactiveRepositoryAdapter(RoleReactiveRepository repository, ObjectMapper mapper) {
        super(repository, mapper, d -> mapper.map(d, Role.class/* change for domain model */));
    }

    @Override
    public Mono<Role> findByName(String name) {
        return repository.findByName(name)
                .switchIfEmpty(Mono.error(new NotFoundException(ERROR_ROLE)))
                .map(entity -> mapper.map(entity, Role.class));
    }

    @Override
    public Mono<Role> findById(Integer id) {
        return repository.findById(id)
                .switchIfEmpty(Mono.error(new NotFoundException(ERROR_ROLE)))
                .map(entity -> mapper.map(entity, Role.class));
    }
}
