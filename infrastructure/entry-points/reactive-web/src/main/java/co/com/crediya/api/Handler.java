package co.com.crediya.api;

import co.com.crediya.api.dto.CreateUserRequest;
import co.com.crediya.api.dto.LoginRequest;
import co.com.crediya.api.mapper.LoginMapper;
import co.com.crediya.api.mapper.UserMapper;
import co.com.crediya.api.validator.RequestValidator;
import co.com.crediya.usecase.createuser.ICreateUserUseCase;
import co.com.crediya.usecase.createuser.ILoginUseCase;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;

import java.util.Map;

import static co.com.crediya.model.utils.Constant.*;
import static org.springframework.http.HttpStatus.BAD_REQUEST;


@Component
@RequiredArgsConstructor
public class Handler {

    public static final Logger log = LoggerFactory.getLogger(Handler.class);
    private final ICreateUserUseCase createUserUseCase;
    private final ILoginUseCase loginUseCase;
    private final UserMapper userMapper;
    private final Validator validator;
    private final LoginMapper loginMapper;


    @PreAuthorize("hasAnyAuthority('ADMIN', 'ASESOR')")
    public Mono<ServerResponse> listenPostCreateUser(ServerRequest request) {
        return request.bodyToMono(CreateUserRequest.class)
                .flatMap(userRequest ->
                    RequestValidator.validate(userRequest, validator)
                        .flatMap(validated ->
                            createUserUseCase.execute(userMapper.toModel(validated))
                                .flatMap(created -> {
                                    log.info(LOG_INFO_USER_CREATED, created.getEmail());
                                    return ServerResponse.status(201).bodyValue(userMapper.toResponse(created));
                                })
                        )
            );
    }

    @PreAuthorize("hasAuthority('CLIENTE')")
    public Mono<ServerResponse> listenGetUserByDocument(ServerRequest request) {

        return Mono.justOrEmpty(request.pathVariable(PATH_VARIABLE))
            .map(String::trim)
            .switchIfEmpty(Mono.error(new ResponseStatusException(BAD_REQUEST, MISSING_PATH_VARIABLE)))
            .flatMap(createUserUseCase::existsUserByDocument)
            .flatMap(exists -> {
                log.info(LOG_INFO_USER_EXISTS, exists);
                return ServerResponse.status(200)
                        .bodyValue(Map.of("exists", exists));
            });
    }

    public Mono<ServerResponse> listenPostLogin(ServerRequest request) {

        return request.bodyToMono(LoginRequest.class)
                .flatMap(loginRequest ->
                        RequestValidator.validate(loginRequest, validator)
                                .flatMap(validated ->
                                        loginUseCase.login(loginMapper.toModel(validated))
                                                .flatMap(tokenDTO -> {
                                                    log.info(LOG_INFO_TOKEN);
                                                    return ServerResponse.status(201).bodyValue(tokenDTO);
                                                })
                                )
                );
    }

}
