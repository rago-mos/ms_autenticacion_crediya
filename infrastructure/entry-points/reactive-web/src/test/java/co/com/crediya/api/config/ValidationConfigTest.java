package co.com.crediya.api.config;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import jakarta.validation.Validator;

class ValidationConfigTest {

    @Test
    void shouldRegisterValidatorBean() {
        var context = new AnnotationConfigApplicationContext(ValidationConfig.class);
        Validator validator = context.getBean(Validator.class);
        assertNotNull(validator);
    }
}
