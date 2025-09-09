package co.com.crediya.api;

import co.com.crediya.api.dto.*;
import co.com.crediya.api.mapper.LoginMapper;
import co.com.crediya.api.mapper.UserMapper;
import co.com.crediya.api.validator.RequestValidator;
import co.com.crediya.model.login.TokenDTO;
import co.com.crediya.usecase.createuser.ICreateUserUseCase;
import co.com.crediya.usecase.createuser.ILoginUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
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



    @Operation(operationId = "createUser",
            summary = "Register a new user",
            description = "The system receives user data and sends a registration confirmation",
            requestBody = @RequestBody(required = true,
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CreateUserRequest.class)
                    )
            ),
            responses = {@ApiResponse(responseCode = "201",
                    description = "User created successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CreateUserResponse.class)
                    )
            ), @ApiResponse(responseCode = "400",
                    description = "Invalid request format",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponseHandler.class)
                    )
            ), @ApiResponse(responseCode = "403",
                    description = "Access denied",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponseHandler.class)
                    )
            ), @ApiResponse(responseCode = "409",
                    description = "Conflict: Email already exists or salary is invalid",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponseHandler.class),
                            examples = {
                                    @ExampleObject(name = "EmailConflict", value = "{\"timestamp\": \"2025-08-26T14:22:56.402Z\",\"status\":409,\"error\":\"BusinessException\",\"message\":\"A user with this email already exists\"}"),
                                    @ExampleObject(name = "SalaryConflict", value = "{\"timestamp\": \"2025-08-26T14:22:56.402Z\",\"status\":409,\"error\":\"BusinessException\",\"message\":\"The salary is not valid\"}")
                            }
                    )
            ), @ApiResponse(responseCode = "500",
                    description = "Internal server error",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponseHandler.class)
                    )
            )}
    )
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


    @Operation(
            operationId = "userExistsByDocument",
            summary = "Verify if a user exists by document number",
            parameters = {
                    @Parameter(
                            name = "documentIdentity",
                            in = ParameterIn.PATH,
                            required = true,
                            description = "User's document number to verify."
                    )
            },
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Verification result",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    examples = @ExampleObject(
                                            name = "exists",
                                            value = "{\"exists\": true}"
                                    )
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Parameter 'document' invalid or blank"
                    ),
                    @ApiResponse(responseCode = "403",
                            description = "Access denied",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorResponseHandler.class)
                            )
                    ),
            }
    )
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


    @Operation(operationId = "getUserApplications",
            summary = "User query by document",
            description = "The system receives the user's document and returns an object with basic user information",
            requestBody = @RequestBody(required = true,
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = UserApplicationsRequest.class)
                    )
            ),
            responses = {@ApiResponse(responseCode = "200",
                    description = "Data returned successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = UserApplicationResponse.class)
                    )
            ), @ApiResponse(responseCode = "400",
                    description = "Invalid request format",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponseHandler.class)
                    )
            ), @ApiResponse(responseCode = "403",
                    description = "Access denied",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponseHandler.class)
                    )
            ), @ApiResponse(responseCode = "500",
                    description = "Internal server error",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponseHandler.class)
                    )
            )}
    )
    @PreAuthorize("hasAuthority('ASESOR')")
    public Mono<ServerResponse> listenPostUserApplications(ServerRequest request) {

        return request.bodyToMono(UserApplicationsRequest.class)
                .doOnNext(req -> log.info("documents: {}", req.documents()))
                .flatMapMany(req ->
                        createUserUseCase.findUsersByIdentityDocument(req.documents()))
                .map(userMapper::toDTO)
                .collectList()
                .doOnNext(res -> log.info(LOG_LIST_USERS, res.size()))
                .flatMap(res -> ServerResponse.ok()
                            .contentType(MediaType.APPLICATION_JSON)
                            .bodyValue(res));
    }


    @Operation(
            operationId = "tokenCreate",
            summary = "Generate token",
            requestBody = @RequestBody(required = true,
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = LoginRequest.class)
                    )
            ),
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "token created successfully",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = TokenDTO.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "Bad credentials"
                    )
            }
    )
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
