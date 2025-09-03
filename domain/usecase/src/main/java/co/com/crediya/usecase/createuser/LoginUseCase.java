package co.com.crediya.usecase.createuser;

import co.com.crediya.model.login.LoginDTO;
import co.com.crediya.model.login.TokenDTO;
import co.com.crediya.model.user.gateways.UserRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class LoginUseCase implements ILoginUseCase {

    private final UserRepository userRepository;

    @Override
    public Mono<TokenDTO> login(LoginDTO dto) {
        return userRepository.login(dto);
    }
}
