package co.com.crediya.api;

import co.com.crediya.api.dto.CreateUserRequest;
import co.com.crediya.api.mapper.UserMapper;
import co.com.crediya.api.validator.RequestValidator;
import co.com.crediya.usecase.createuser.ICreateUserUseCase;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;

import java.util.Map;

import static co.com.crediya.api.utils.Constant.MISSING_PATH_VARIABLE;
import static co.com.crediya.api.utils.Constant.PATH_VARIABLE;
import static org.springframework.http.HttpStatus.BAD_REQUEST;


@Component
@RequiredArgsConstructor
public class Handler {

    public static final Logger log = LoggerFactory.getLogger(Handler.class);
    private final ICreateUserUseCase createUserUseCase;
    private final UserMapper userMapper;
    private final Validator validator;

    public Mono<ServerResponse> listenPostCreateUser(ServerRequest request) {
        return request.bodyToMono(CreateUserRequest.class)
                .flatMap(userRequest ->
                    RequestValidator.validate(userRequest, validator)
                        .flatMap(validated ->
                            createUserUseCase.execute(userMapper.toModel(validated))
                                .flatMap(created -> {
                                    log.info("User created successfully: {}", created.getEmail());
                                    return ServerResponse.status(201).bodyValue(userMapper.toResponse(created));
                                })
                        )
            );
    }

    public Mono<ServerResponse> listenGetUserByDocument(ServerRequest request) {

        return Mono.justOrEmpty(request.pathVariable(PATH_VARIABLE))
            .map(String::trim)
            .switchIfEmpty(Mono.error(new ResponseStatusException(BAD_REQUEST, MISSING_PATH_VARIABLE)))
            .flatMap(createUserUseCase::existsUserByDocument)
            .flatMap(exists -> {
                log.info("User exists: {}", exists);
                return ServerResponse.status(200)
                        .bodyValue(Map.of("exists", exists));
            });
    }

}
