package co.com.crediya.api.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import static org.junit.jupiter.api.Assertions.*;

class OpenApiConfigTest {

    @Test
    void shouldRegisterOpenApiBeanCorrectly() {
        var context = new AnnotationConfigApplicationContext(OpenApiConfig.class);
        OpenAPI openAPI = context.getBean(OpenAPI.class);

        assertNotNull(openAPI);
        Info info = openAPI.getInfo();
        assertNotNull(info);
        assertEquals("User Management API", info.getTitle());
        assertEquals("v1.0", info.getVersion());
        assertEquals("API for user registration and management", info.getDescription());

        License license = info.getLicense();
        assertNotNull(license);
        assertEquals("MIT License", license.getName());
        assertEquals("https://opensource.org/licenses/MIT", license.getUrl());
    }

}