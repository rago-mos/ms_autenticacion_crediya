package co.com.crediya.model.user.gateways;

import co.com.crediya.model.login.LoginDTO;
import co.com.crediya.model.login.TokenDTO;
import co.com.crediya.model.user.User;
import reactor.core.publisher.Mono;

public interface UserRepository {

    Mono<User> saveUser(User user);
    Mono<Boolean> existsByEmail(String email);
    Mono<Boolean> existsByIdentityDocument(String document);
    Mono<TokenDTO> login(LoginDTO dto);
}
