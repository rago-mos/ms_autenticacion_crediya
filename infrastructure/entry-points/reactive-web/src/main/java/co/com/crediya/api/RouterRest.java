package co.com.crediya.api;

import co.com.crediya.api.dto.CreateUserRequest;
import co.com.crediya.api.dto.ErrorResponseHandler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springdoc.core.annotations.RouterOperation;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.bind.annotation.RequestMethod.POST;
import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class RouterRest {

    private static final String CREATE_USER_URL = "/api/v1/usuarios";

    @Bean
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
                                    schema = @Schema(type = "string", example = "User created successfully")
                            )
                    ), @ApiResponse(responseCode = "400",
                            description = "Invalid request format",
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
    )
    public RouterFunction<ServerResponse> routerFunction(Handler handler) {
        return route(POST(CREATE_USER_URL), handler::listenPostCreateUser);
    }
}
