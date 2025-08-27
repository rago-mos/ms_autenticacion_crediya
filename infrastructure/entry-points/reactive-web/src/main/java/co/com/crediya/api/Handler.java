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
import reactor.core.publisher.Mono;

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
                                    return ServerResponse.status(201).bodyValue("User created successfully");
                                })
                        )
            );
    }

}
