package co.com.crediya.api.config;

import co.com.crediya.api.mapper.LoginMapper;
import co.com.crediya.api.mapper.UserMapper;
import co.com.crediya.usecase.createuser.ICreateUserUseCase;
import co.com.crediya.usecase.createuser.ILoginUseCase;
import jakarta.validation.Validator;
import org.mockito.Mockito;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

@TestConfiguration
class MockUseCaseConfig {

    @Bean
    public ICreateUserUseCase createUserUseCase() {
        return Mockito.mock(ICreateUserUseCase.class);
    }

    @Bean
    public ILoginUseCase loginUseCase() {
        return Mockito.mock(ILoginUseCase.class);
    }

    @Bean
    public UserMapper userMapper() {
        return Mockito.mock(UserMapper.class);
    }

    @Bean
    public LoginMapper loginMapper() {
        return Mockito.mock(LoginMapper.class);
    }

    @Bean
    public Validator validator() {
        return Mockito.mock(Validator.class);
    }
}
