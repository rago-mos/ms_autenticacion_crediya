package co.com.crediya.api;

import co.com.crediya.api.dto.CreateUserRequest;
import co.com.crediya.api.dto.CreateUserResponse;
import co.com.crediya.api.exception.GlobalExceptionHandler;
import co.com.crediya.api.mapper.UserMapper;
import co.com.crediya.model.role.Role;
import co.com.crediya.model.user.User;
import co.com.crediya.usecase.createuser.ICreateUserUseCase;
import jakarta.validation.Validator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static reactor.blockhound.shaded.net.bytebuddy.matcher.ElementMatchers.any;

@ContextConfiguration(classes = {RouterRest.class, Handler.class, GlobalExceptionHandler.class})
@WebFluxTest
class RouterRestTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockitoBean
    private ICreateUserUseCase userUseCase;

    @MockitoBean
    private UserMapper userMapper;

    @MockitoBean
    private Validator validator;

    @Test
    void shouldRegisterSuccessfully() {
        User user = userMock();
        CreateUserRequest request = userRequestMock();
        CreateUserResponse response = CreateUserResponse.builder()
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .identityDocument(user.getIdentityDocument())
                .birthDate(user.getBirthDate())
                .address(user.getAddress())
                .phoneNumber(user.getPhoneNumber())
                .rol(user.getRole().getName())
                .baseSalary(user.getBaseSalary())
                .build();

        when(validator.validate(any())).thenReturn(Set.of());
        when(userMapper.toModel(request)).thenReturn(user);
        when(userUseCase.execute(user)).thenReturn(Mono.just(user));
        when(userMapper.toResponse(user)).thenReturn(response);

        webTestClient.post()
                .uri("/api/v1/usuarios")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(request)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(CreateUserResponse.class)
                .value(r -> {
                    assertEquals("Pepe", r.firstName());
                    assertEquals("Perez", r.lastName());
                    assertEquals("pepe@gmail.com", r.email());
                    assertEquals("CLIENTE", r.rol());
                });
    }


    private User userMock() {
        return User.builder()
                .firstName("Pepe")
                .lastName("Perez")
                .email("pepe@gmail.com")
                .identityDocument(328472388273823L)
                .birthDate(LocalDate.now())
                .address("Cr 5 N° 798")
                .phoneNumber("36127328237")
                .role(new Role(1, "CLIENTE", "weer"))
                .baseSalary(new BigDecimal(87234783216L))
                .build();
    }

    private CreateUserRequest userRequestMock() {
        return new CreateUserRequest(
                "Pepe",
                "Perez",
                "pepe@gmail.com",
                328472388273823L,
                LocalDate.now(),
                "Cr 5 N° 798",
                "36127328237",
                "CLIENTE",
                new BigDecimal(87234783216L)
        );
    }
}
