package co.com.crediya.security.config;

import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class PublicPathsTest {

    @Test
    void shouldContainExpectedPublicRoutes() {
        String[] expected = {
                "/api/v1/login",
                "/swagger-ui.html",
                "/swagger-ui/**",
                "/v3/api-docs/**",
                "/webjars/**",
                "/swagger-resources/**",
                "/actuator/health"
        };

        assertArrayEquals(expected, PublicPaths.ROUTES, "Las rutas públicas deben coincidir exactamente");
    }

    @Test
    void shouldInitializeRoutesCorrectly() {
        assertNotNull(PublicPaths.ROUTES, "ROUTES debe estar inicializado");
        assertTrue(PublicPaths.ROUTES.length > 0, "ROUTES debe contener rutas");

        boolean containsHealth = false;
        for (String route : PublicPaths.ROUTES) {
            if ("/actuator/health".equals(route)) {
                containsHealth = true;
                break;
            }
        }

        assertTrue(containsHealth, "Debe incluir /actuator/health como ruta pública");
    }

    @Test
    void shouldContainActuatorHealth() {
        assertTrue(
                Arrays.asList(PublicPaths.ROUTES).contains("/actuator/health"),
                "Debe incluir /actuator/health como ruta pública"
        );
    }
}