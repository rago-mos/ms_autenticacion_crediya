package co.com.crediya.usecase.createuser;

import co.com.crediya.model.role.gateways.RoleRepository;
import co.com.crediya.model.user.User;
import co.com.crediya.model.user.gateways.UserRepository;
import co.com.crediya.usecase.createuser.exception.BusinessException;
import co.com.crediya.usecase.createuser.validator.UserValidator;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class CreateUserUseCase implements ICreateUserUseCase {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    @Override
    public Mono<User> execute(User user) {
        return validateData(user)
                .flatMap(userRepository::saveUser);
    }

    private Mono<User> validateData(User user) {
        return UserValidator.validateFirtsName(user.getFirstName())
                .then(UserValidator.validateLastName(user.getLastName()))
                .then(UserValidator.validateSalary(user.getBaseSalary()))
                .then(asignUser(user))
                .flatMap(this::validateUniqueEmail);
    }

    private Mono<User> asignUser(User user) {
        return roleRepository.findByName(user.getRole().getName().toUpperCase())
                .map(rol -> {
                    user.setRole(rol);
                    return user;
                });
    }

    private Mono<User> validateUniqueEmail(User user) {
        return userRepository.existsByEmail(user.getEmail())
                .flatMap(exists -> {
                    if (Boolean.TRUE.equals(exists)) {
                        return Mono.error(new BusinessException("A user with this email already exists " + user.getEmail()));
                    }
                    return Mono.just(user);
                });
    }
}
