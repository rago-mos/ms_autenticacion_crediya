package co.com.crediya.api;

import co.com.crediya.api.dto.*;
import co.com.crediya.model.login.TokenDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springdoc.core.annotations.RouterOperation;
import org.springdoc.core.annotations.RouterOperations;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.bind.annotation.RequestMethod.POST;
import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class RouterRest {

    private static final String CREATE_USER_URL = "/api/v1/usuarios";
    private static final String LOGIN_URL = "/api/v1/login";
    private static final String FIND_USER_URL = "/api/v1/usuarios/{documentIdentity}";
    private static final String FIND_USERS_APPLICATIONS_URL = "/api/v1/usuarioSolicitudes";

    @Bean
    @RouterOperations({
            @RouterOperation(method = POST,
                    path = CREATE_USER_URL,
                    beanClass = Handler.class,
                    beanMethod = "listenPostCreateUser",
                    operation = @Operation(operationId = "createUser",
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
            ),
            @RouterOperation(
                    path = FIND_USER_URL,
                    method = RequestMethod.GET,
                    beanClass = Handler.class,
                    beanMethod = "listenGetUserByDocument",
                    operation = @Operation(
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
            ),
            @RouterOperation(
                    path = LOGIN_URL,
                    method = RequestMethod.POST,
                    beanClass = Handler.class,
                    beanMethod = "listenPostLogin",
                    operation = @Operation(
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
            ),
            @RouterOperation(method = POST,
                    path = FIND_USERS_APPLICATIONS_URL,
                    beanClass = Handler.class,
                    beanMethod = "listenPostUserApplications",
                    operation = @Operation(operationId = "getUserApplications",
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
            )
    })
    public RouterFunction<ServerResponse> routerFunction(Handler handler) {
        return route(POST(CREATE_USER_URL), handler::listenPostCreateUser)
                .andRoute(POST(LOGIN_URL), handler::listenPostLogin)
                .andRoute(GET(FIND_USER_URL), handler::listenGetUserByDocument)
                .andRoute(POST(FIND_USERS_APPLICATIONS_URL), handler::listenPostUserApplications);
    }
}
