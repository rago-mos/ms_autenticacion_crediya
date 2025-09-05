package co.com.crediya.r2dbc;

import co.com.crediya.model.application.UserApplicationView;
import co.com.crediya.model.exception.BadCredentialsException;
import co.com.crediya.model.login.LoginDTO;
import co.com.crediya.model.login.TokenDTO;
import co.com.crediya.model.user.User;
import co.com.crediya.model.user.gateways.UserRepository;
import co.com.crediya.r2dbc.entities.UserEntity;
import co.com.crediya.r2dbc.helper.ReactiveAdapterOperations;
import co.com.crediya.r2dbc.mapper.UserEntityMapper;
import co.com.crediya.security.jwt.provider.JwtProvider;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.reactive.TransactionalOperator;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

import static co.com.crediya.model.utils.Constant.ERROR_BAD_CREDENTIALS;

@Repository
public class UserReactiveRepositoryAdapter extends ReactiveAdapterOperations<
    User,
    UserEntity,
    Long,
    UserReactiveRepository
> implements UserRepository {

    private final UserEntityMapper userEntityMapper;
    private final TransactionalOperator transactionalOperator;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;

    public UserReactiveRepositoryAdapter(UserReactiveRepository repository, ObjectMapper mapper,
                                         UserEntityMapper userEntityMapper,
                                         TransactionalOperator transactionalOperator,
                                         PasswordEncoder passwordEncoder,
                                         JwtProvider jwtProvider) {
        super(repository, mapper, d -> mapper.map(d, User.class/* change for domain model */));
        this.userEntityMapper = userEntityMapper;
        this.transactionalOperator = transactionalOperator;
        this.passwordEncoder = passwordEncoder;
        this.jwtProvider = jwtProvider;
    }

    @Override
    public Mono<User> saveUser(User user) {
        return transactionalOperator.execute(status ->
                repository.save(userEntityMapper.toEntity(user))
                        .map(userEntityMapper::toDomain)
        ).then(Mono.just(user));
    }

    @Override
    public Mono<Boolean> existsByEmail(String email) {
        return repository.existsByEmail(email);
    }

    @Override
    public Mono<Boolean> existsByIdentityDocument(String document) {
        return repository.existsByIdentityDocument(document);
    }

    @Override
    public Mono<TokenDTO> login(LoginDTO dto) {
        return repository.findByIdentityDocument(dto.identityDocument())
                .filter(userEntity -> passwordEncoder.matches(dto.password(), userEntity.getPassword()))
                .map(userEntity ->  new TokenDTO(jwtProvider.generateToken(userEntity)))
                .switchIfEmpty(Mono.error(new BadCredentialsException(ERROR_BAD_CREDENTIALS)));
    }

    @Override
    public Flux<UserApplicationView> findUsersByIdentityDocument(List<String> document) {
        return repository.finUsersApplicationdByDocumentIn(document);
    }
}
