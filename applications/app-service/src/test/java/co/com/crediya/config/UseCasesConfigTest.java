package co.com.crediya.config;

import co.com.crediya.r2dbc.RoleReactiveRepository;
import co.com.crediya.r2dbc.UserReactiveRepository;
import co.com.crediya.r2dbc.config.MysqlConnectionProperties;
import co.com.crediya.security.jwt.provider.JwtProvider;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.context.annotation.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.reactive.TransactionalOperator;

import static org.junit.jupiter.api.Assertions.assertTrue;

class UseCasesConfigTest {

    @Test
    void testUseCaseBeansExist() {
        try (AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(TestConfig.class)) {
            String[] beanNames = context.getBeanDefinitionNames();

            boolean useCaseBeanFound = false;
            for (String beanName : beanNames) {
                if (beanName.endsWith("UseCase")) {
                    useCaseBeanFound = true;
                    break;
                }
            }

            assertTrue(useCaseBeanFound, "No beans ending with 'Use Case' were found");
        }
    }

    @Configuration
    @Import(UseCasesConfig.class)
    @ComponentScan(basePackages = {
            "co.com.crediya.usecase",
            "co.com.crediya.r2dbc",
            "co.com.crediya.model"
    })
    static class TestConfig {

        @Bean
        public MyUseCase myUseCase() {
            return new MyUseCase();
        }

        @Bean
        public RoleReactiveRepository roleReactiveRepository() {
            return Mockito.mock(RoleReactiveRepository.class);
        }

        @Bean
        public UserReactiveRepository userReactiveRepository() {
            return Mockito.mock(UserReactiveRepository.class);
        }

        @Bean
        public org.reactivecommons.utils.ObjectMapper reactiveCommonsObjectMapper() {
            return new org.reactivecommons.utils.ObjectMapper() {
                @Override
                public <T> T map(Object src, Class<T> target) {
                    return null;
                }

                @Override
                public <T> T mapBuilder(Object src, Class<T> target) {
                    return null;
                }
            };
        }

        @Bean
        public TransactionalOperator transactionalOperator() {
            return Mockito.mock(TransactionalOperator.class);
        }

        @Bean
        public PasswordEncoder passwordEncoder() {
            return Mockito.mock(PasswordEncoder.class);
        }

        @Bean
        public JwtProvider  jwtProvider() {
            return Mockito.mock(JwtProvider.class);
        }

        @Bean
        public MysqlConnectionProperties mysqlConnectionProperties() {
            return new MysqlConnectionProperties(
                    "localhost",
                    3306,
                    "testdb",
                    "test",
                    "test",
                    "test"
            );
        }
    }

    static class MyUseCase {
        public String execute() {
            return "MyUseCase Test";
        }
    }
}
