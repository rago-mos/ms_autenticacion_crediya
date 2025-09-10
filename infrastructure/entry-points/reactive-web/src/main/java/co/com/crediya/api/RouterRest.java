package co.com.crediya.api;

import org.springdoc.core.annotations.RouterOperation;
import org.springdoc.core.annotations.RouterOperations;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
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
                    beanMethod = "listenPostCreateUser"
            ),
            @RouterOperation(
                    path = FIND_USER_URL,
                    method = RequestMethod.GET,
                    beanClass = Handler.class,
                    beanMethod = "listenGetUserByDocument"
            ),
            @RouterOperation(
                    path = LOGIN_URL,
                    method = RequestMethod.POST,
                    beanClass = Handler.class,
                    beanMethod = "listenPostLogin"
            ),
            @RouterOperation(method = POST,
                    path = FIND_USERS_APPLICATIONS_URL,
                    beanClass = Handler.class,
                    beanMethod = "listenPostUserApplications"
            )
    })
    public RouterFunction<ServerResponse> routerFunction(Handler handler) {
        return route(POST(CREATE_USER_URL), handler::listenPostCreateUser)
                .andRoute(POST(LOGIN_URL), handler::listenPostLogin)
                .andRoute(GET(FIND_USER_URL), handler::listenGetUserByDocument)
                .andRoute(POST(FIND_USERS_APPLICATIONS_URL), handler::listenPostUserApplications);
    }
}
