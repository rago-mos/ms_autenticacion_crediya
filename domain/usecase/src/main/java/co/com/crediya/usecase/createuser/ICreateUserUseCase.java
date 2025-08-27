package co.com.crediya.usecase.createuser;

import co.com.crediya.model.user.User;
import reactor.core.publisher.Mono;

public interface ICreateUserUseCase {

    Mono<User> execute(User user);
}
