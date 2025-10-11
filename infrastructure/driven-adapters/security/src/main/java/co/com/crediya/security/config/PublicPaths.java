package co.com.crediya.security.config;

public class PublicPaths {
    public static final String[] ROUTES = {
            "/api/v1/login",
            "/swagger-ui.html",
            "/swagger-ui/**",
            "/v3/api-docs/**",
            "/webjars/**",
            "/swagger-resources/**",
            "/actuator/health"
    };
}
