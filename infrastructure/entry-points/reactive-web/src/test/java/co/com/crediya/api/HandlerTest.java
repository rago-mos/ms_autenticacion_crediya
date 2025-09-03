package co.com.crediya.api;


import co.com.crediya.api.dto.CreateUserRequest;
import co.com.crediya.api.dto.LoginRequest;
import co.com.crediya.api.mapper.LoginMapper;
import co.com.crediya.api.mapper.UserMapper;
import co.com.crediya.model.login.LoginDTO;
import co.com.crediya.model.login.TokenDTO;
import co.com.crediya.model.user.User;
import co.com.crediya.usecase.createuser.ICreateUserUseCase;
import co.com.crediya.usecase.createuser.ILoginUseCase;
import jakarta.validation.Validator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Set;

import static org.mockito.Mockito.*;
import static org.springframework.http.HttpStatus.BAD_REQUEST;


class HandlerTest {

    private ICreateUserUseCase createUserUseCase;
    private ILoginUseCase loginUseCase;
    private UserMapper userMapper;
    private Validator validator;
    private LoginMapper loginMapper;
    private Handler handler;

    @BeforeEach
    void setUp() {
        createUserUseCase = mock(ICreateUserUseCase.class);
        loginUseCase = mock(ILoginUseCase.class);
        userMapper = new UserMapper();
        validator = mock(Validator.class);
        loginMapper = new LoginMapper();
        handler = new Handler(createUserUseCase, loginUseCase, userMapper, validator, loginMapper);
    }

    @Test
    void shouldCreateUserSuccessfully() {
        CreateUserRequest request = new CreateUserRequest(
                "Rubén", "Gómez", "ruben@example.com", "securePass", "123456789",
                LocalDate.of(1990, 1, 1), "Palmira", "3001234567", 1, new BigDecimal("5000000")
        );

        User user = userMapper.toModel(request);
        when(validator.validate(request)).thenReturn(Set.of());
        when(createUserUseCase.execute(any(User.class))).thenReturn(Mono.just(user));

        ServerRequest serverRequest = mock(ServerRequest.class);
        when(serverRequest.bodyToMono(CreateUserRequest.class)).thenReturn(Mono.just(request));

        Mono<ServerResponse> response = handler.listenPostCreateUser(serverRequest);

        StepVerifier.create(response)
                .expectNextMatches(res -> res.statusCode().value() == 201)
                .verifyComplete();
    }

    @Test
    void shouldReturnUserExistenceByDocument() {
        ServerRequest request = mock(ServerRequest.class);
        when(request.pathVariable("documentIdentity")).thenReturn("123456789");
        when(createUserUseCase.existsUserByDocument("123456789")).thenReturn(Mono.just(true));

        Mono<ServerResponse> response = handler.listenGetUserByDocument(request);

        StepVerifier.create(response)
                .expectNextMatches(res -> res.statusCode().value() == 200)
                .verifyComplete();
    }

    @Test
    void shouldFailWhenPathVariableIsMissing() {
        ServerRequest request = mock(ServerRequest.class);
        when(request.pathVariable("documentIdentity")).thenReturn(null); // o "" si prefieres

        Mono<ServerResponse> response = handler.listenGetUserByDocument(request);

        StepVerifier.create(response)
                .expectErrorMatches(e -> e instanceof ResponseStatusException &&
                        ((ResponseStatusException) e).getStatusCode().equals(BAD_REQUEST) &&
                        e.getMessage().contains("Path Variable 'document' is required"))
                .verify();
    }

    @Test
    void shouldLoginSuccessfully() {
        LoginRequest loginRequest = new LoginRequest("123456789", "securePass");
        LoginDTO dto = loginMapper.toModel(loginRequest);
        TokenDTO token = new TokenDTO("mocked-token");

        when(validator.validate(loginRequest)).thenReturn(Set.of());
        when(loginUseCase.login(dto)).thenReturn(Mono.just(token));

        ServerRequest request = mock(ServerRequest.class);
        when(request.bodyToMono(LoginRequest.class)).thenReturn(Mono.just(loginRequest));

        Mono<ServerResponse> response = handler.listenPostLogin(request);

        StepVerifier.create(response)
                .expectNextMatches(res -> res.statusCode().value() == 201)
                .verifyComplete();
    }
}