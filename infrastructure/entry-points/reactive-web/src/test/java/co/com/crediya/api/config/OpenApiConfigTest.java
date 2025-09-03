package co.com.crediya.api.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class OpenApiConfigTest {

    @Test
    void shouldCreateOpenApiBeanSuccessfully() {
        OpenApiConfig config = new OpenApiConfig();
        OpenAPI openAPI = config.customOpenAPI();

        assertNotNull(openAPI);

        Info info = openAPI.getInfo();
        assertNotNull(info);
        assertEquals("User Management API", info.getTitle());
        assertEquals("v1.0", info.getVersion());
        assertEquals("API for user registration and management", info.getDescription());
        assertEquals("MIT License", info.getLicense().getName());
        assertEquals("https://opensource.org/licenses/MIT", info.getLicense().getUrl());

        assertNotNull(openAPI.getSecurity());
        assertFalse(openAPI.getSecurity().isEmpty());
        assertTrue(openAPI.getSecurity().get(0).containsKey("bearerAuth"));

        Components components = openAPI.getComponents();
        assertNotNull(components);
        SecurityScheme scheme = components.getSecuritySchemes().get("bearerAuth");
        assertNotNull(scheme);
        assertEquals(SecurityScheme.Type.HTTP, scheme.getType());
        assertEquals("bearer", scheme.getScheme());
        assertEquals("JWT", scheme.getBearerFormat());
        assertEquals(SecurityScheme.In.HEADER, scheme.getIn());
    }

}