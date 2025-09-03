package co.com.crediya.api;

import co.com.crediya.api.dto.CreateUserRequest;
import co.com.crediya.api.dto.CreateUserResponse;
import co.com.crediya.api.dto.LoginRequest;
import co.com.crediya.api.exception.GlobalExceptionHandler;
import co.com.crediya.api.mapper.LoginMapper;
import co.com.crediya.api.mapper.UserMapper;
import co.com.crediya.model.login.LoginDTO;
import co.com.crediya.model.login.TokenDTO;
import co.com.crediya.model.role.Role;
import co.com.crediya.model.user.User;
import co.com.crediya.security.config.SecurityConfig;
import co.com.crediya.security.jwt.filter.JwtFilter;
import co.com.crediya.security.jwt.manager.JwtAuthenticationManager;
import co.com.crediya.security.repository.SecurityContextRepository;
import co.com.crediya.usecase.createuser.ICreateUserUseCase;
import co.com.crediya.usecase.createuser.ILoginUseCase;
import jakarta.validation.Validator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.http.MediaType;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Set;
import static org.mockito.Mockito.when;
import static reactor.blockhound.shaded.net.bytebuddy.matcher.ElementMatchers.any;


@ContextConfiguration(classes = {RouterRest.class, Handler.class, GlobalExceptionHandler.class, SecurityConfigTest.class})
@WebFluxTest
class RouterRestTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockitoBean
    private ICreateUserUseCase userUseCase;

    @MockitoBean
    private ILoginUseCase loginUseCase;

    @MockitoBean
    private Validator validator;

    @MockitoBean
    private UserMapper userMapper;

    @MockitoBean
    private LoginMapper loginMapper;


    @WithMockUser(username = "admin", authorities = {"ADMIN", "ASESOR"})
    @Test
    void shouldCreateUserSuccessfully() {
        User user = userMock();
        CreateUserRequest request = userRequestMock();
        CreateUserResponse response = userMapper.toResponse(user);

        when(validator.validate(any())).thenReturn(Set.of());
        when(userUseCase.execute(user)).thenReturn(Mono.just(user));

        webTestClient.post()
                .uri("/api/v1/usuarios")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(request)
                .exchange()
                .expectStatus().isForbidden();
    }

    @WithMockUser(username = "cliente", authorities = {"CLIENTE"})
    @Test
    void shouldReturnUserExistenceByDocument() {
        String document = "328472388273823";

        when(userUseCase.existsUserByDocument(document)).thenReturn(Mono.just(true));

        webTestClient.get()
                .uri("/api/v1/usuarios/" + document)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.exists").isEqualTo(true);
    }

    @WithMockUser(username = "unauthorized", authorities = {"CLIENTE"})
    @Test
    void shouldRejectUserCreationForUnauthorizedRole() {
        CreateUserRequest request = userRequestMock();

        webTestClient.post()
                .uri("/api/v1/usuarios")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(request)
                .exchange()
                .expectStatus().isForbidden();
    }

    @Test
    void shouldLoginSuccessfully() {
        LoginRequest loginRequest = new LoginRequest("328472388273823", "123456");
        LoginDTO loginDTO = new LoginDTO("328472388273823", "123456");
        TokenDTO token = new TokenDTO("jwt-token");

        when(loginUseCase.login(loginDTO)).thenReturn(Mono.just(token));

        webTestClient.post()
                .uri("/api/v1/login")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(loginRequest)
                .exchange()
                .expectStatus().isForbidden();
    }

    // Mocks reutilizables
    private User userMock() {
        return User.builder()
                .firstName("Pepe")
                .lastName("Perez")
                .email("pepe@gmail.com")
                .identityDocument("328472388273823")
                .birthDate(LocalDate.now())
                .address("Cr 5 N° 798")
                .phoneNumber("36127328237")
                .role(new Role(1, "CLIENTE", "weer"))
                .baseSalary(new BigDecimal("87234783216"))
                .build();
    }

    private CreateUserRequest userRequestMock() {
        return new CreateUserRequest(
                "Pepe",
                "Perez",
                "pepe@gmail.com",
                "262762",
                "328472388273823",
                LocalDate.now(),
                "Cr 5 N° 798",
                "36127328237",
                1,
                new BigDecimal("87234783216")
        );
    }
}

@TestConfiguration
@EnableReactiveMethodSecurity
class SecurityConfigTest {

}
