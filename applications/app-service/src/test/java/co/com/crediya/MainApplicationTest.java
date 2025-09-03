package co.com.crediya;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(
        classes = MainApplication.class,
        properties = {
                "jwt.secret=my-super-secret-key-12345678901234567890123456789012",
                "jwt.expiration=3600000"
        }
)
class MainApplicationTest {

    @Test
    void contextLoads() {
    }

    @Test
    void shouldRunMainWithoutErrors() {
        System.setProperty("jwt.secret", "my-super-secret-key-12345678901234567890123456789012");
        System.setProperty("jwt.expiration", "3600000");

        MainApplication.main(new String[]{});
    }
}