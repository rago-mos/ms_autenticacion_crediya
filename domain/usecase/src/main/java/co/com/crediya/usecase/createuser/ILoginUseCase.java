package co.com.crediya.usecase.createuser;

import co.com.crediya.model.login.LoginDTO;
import co.com.crediya.model.login.TokenDTO;
import reactor.core.publisher.Mono;

public interface ILoginUseCase {

    Mono<TokenDTO> login(LoginDTO dto);
}
