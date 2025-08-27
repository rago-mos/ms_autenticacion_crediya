package co.com.crediya.api.config;

import co.com.crediya.api.Handler;
import co.com.crediya.api.mapper.UserMapper;
import co.com.crediya.usecase.createuser.ICreateUserUseCase;
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
    public UserMapper userMapper() {
        return Mockito.mock(UserMapper.class);
    }

    @Bean
    public Validator validator() {
        return Mockito.mock(Validator.class);
    }
}
