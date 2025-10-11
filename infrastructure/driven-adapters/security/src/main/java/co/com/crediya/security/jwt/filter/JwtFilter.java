package co.com.crediya.security.jwt.filter;

import org.springframework.http.HttpHeaders;
import org.springframework.security.web.server.util.matcher.ServerWebExchangeMatcher;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

@Component
public class JwtFilter implements WebFilter {

    private final ServerWebExchangeMatcher publicPathsMatcher;

    public JwtFilter(ServerWebExchangeMatcher publicPathsMatcher) {
        this.publicPathsMatcher = publicPathsMatcher;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        return publicPathsMatcher.matches(exchange)
                .flatMap(matchResult -> {
                    if (matchResult.isMatch()) {
                        return chain.filter(exchange);
                    }

                    String auth = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
                    if (auth == null || !auth.startsWith("Bearer ")) {
                        return chain.filter(exchange);
                    }

                    String token = auth.replace("Bearer ", "");
                    exchange.getAttributes().put("token", token);
                    return chain.filter(exchange);
                });
    }
}
