package co.com.crediya.r2dbc;

import co.com.crediya.model.application.UserApplicationView;
import co.com.crediya.r2dbc.entities.UserEntity;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.query.ReactiveQueryByExampleExecutor;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;


public interface UserReactiveRepository extends ReactiveCrudRepository<UserEntity, Long>, ReactiveQueryByExampleExecutor<UserEntity> {

    Mono<UserEntity> findByIdentityDocument(String document);
    Mono<Boolean> existsByEmail(String email);
    Mono<Boolean> existsByIdentityDocument(String document);

    @Query("SELECT nombre, apellido, email, documento_identidad, salario_base " +
            "FROM usuario " +
            "WHERE documento_identidad " +
            "IN (:document)")
    Flux<UserApplicationView> finUsersApplicationdByDocumentIn(List<String> document);
}
