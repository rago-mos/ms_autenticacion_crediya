package co.com.crediya.usecase.createuser;

import co.com.crediya.model.exception.BadCredentialsException;
import co.com.crediya.model.login.LoginDTO;
import co.com.crediya.model.login.TokenDTO;
import co.com.crediya.model.user.gateways.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import static org.mockito.Mockito.*;

class LoginUseCaseTest {

    private UserRepository userRepository;
    private LoginUseCase loginUseCase;

    @BeforeEach
    void setUp() {
        userRepository = mock(UserRepository.class);
        loginUseCase = new LoginUseCase(userRepository);
    }

    @Test
    void shouldReturnTokenWhenLoginIsSuccessful() {
        LoginDTO dto = LoginDTO.builder()
                .identityDocument("123456789")
                .password("securePass")
                .build();

        TokenDTO token = new TokenDTO("mocked-jwt-token");

        when(userRepository.login(dto)).thenReturn(Mono.just(token));

        StepVerifier.create(loginUseCase.login(dto))
                .expectNextMatches(t -> t.token().equals("mocked-jwt-token"))
                .verifyComplete();

        verify(userRepository).login(dto);
    }

    @Test
    void shouldThrowBadCredentialsExceptionWhenLoginFails() {
        LoginDTO dto = LoginDTO.builder()
                .identityDocument("123456789")
                .password("wrongPass")
                .build();

        when(userRepository.login(dto)).thenReturn(Mono.error(new BadCredentialsException("bad credentials")));

        StepVerifier.create(loginUseCase.login(dto))
                .expectErrorMatches(e -> e instanceof BadCredentialsException &&
                        e.getMessage().equals("bad credentials"))
                .verify();

        verify(userRepository).login(dto);
    }
}