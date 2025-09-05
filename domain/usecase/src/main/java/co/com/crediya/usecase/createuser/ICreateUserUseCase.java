package co.com.crediya.usecase.createuser;

import co.com.crediya.model.application.UserApplicationView;
import co.com.crediya.model.user.User;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

public interface ICreateUserUseCase {

    Mono<User> execute(User user);
    Mono<Boolean> existsUserByDocument(String document);
    Flux<UserApplicationView> findUsersByIdentityDocument(List<String> document);
}
