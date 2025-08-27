package co.com.crediya.r2dbc;

import co.com.crediya.r2dbc.entities.RoleEntity;
import org.springframework.data.repository.query.ReactiveQueryByExampleExecutor;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;


public interface RoleReactiveRepository extends ReactiveCrudRepository<RoleEntity, Integer>, ReactiveQueryByExampleExecutor<RoleEntity> {

    Mono<RoleEntity> findByName(String name);
}
