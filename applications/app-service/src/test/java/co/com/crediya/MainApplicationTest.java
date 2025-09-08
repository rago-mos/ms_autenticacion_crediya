package co.com.crediya;

import co.com.crediya.r2dbc.RoleReactiveRepository;
import co.com.crediya.r2dbc.UserReactiveRepository;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

@SpringBootTest(
        classes = MainApplication.class,
        properties = {
                "jwt.secret=my-super-secret-key-12345678901234567890123456789012",
                "jwt.expiration=3600000",
                "server.port=8002",
                "spring.autoconfigure.exclude=org.springframework.boot.autoconfigure.r2dbc.R2dbcAutoConfiguration",
                "spring.flyway.enabled=false",
        }
)
class MainApplicationTest {

    @MockitoBean
    private RoleReactiveRepository roleReactiveRepository;

    @MockitoBean
    private UserReactiveRepository userReactiveRepository;

    @Test
    void contextLoads() {
    }

}