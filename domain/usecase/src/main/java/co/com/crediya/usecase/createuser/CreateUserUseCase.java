package co.com.crediya.usecase.createuser;

import co.com.crediya.model.role.gateways.RoleRepository;
import co.com.crediya.model.user.User;
import co.com.crediya.model.user.gateways.UserRepository;
import co.com.crediya.model.exception.BusinessException;
import co.com.crediya.usecase.createuser.validator.UserValidator;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

import static co.com.crediya.model.utils.Constant.ERROR_BUSINESS_DOCUMENT;
import static co.com.crediya.model.utils.Constant.ERROR_BUSINESS_EMAIL;

@RequiredArgsConstructor
public class CreateUserUseCase implements ICreateUserUseCase {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    @Override
    public Mono<User> execute(User user) {
        return validateData(user)
                .flatMap(userRepository::saveUser)
                .flatMap(this::setRole);
    }

    @Override
    public Mono<Boolean> existsUserByDocument(String document) {
        return userRepository.existsByIdentityDocument(document);
    }

    private Mono<User> validateData(User user) {
        return UserValidator.validateFirtsName(user.getFirstName())
                .then(UserValidator.validateLastName(user.getLastName()))
                .then(UserValidator.validateSalary(user.getBaseSalary()))
                .then(this.validateUniqueUser(user));
    }

    private Mono<User> setRole(User user) {
        return roleRepository.findById(user.getRole().getIdRol())
                .map(rol -> {
                    user.setRole(rol);
                    return user;
                });
    }

    private Mono<User> validateUniqueUser(User user) {
        return userRepository.existsByEmail(user.getEmail())
                .flatMap(existsEmail -> Boolean.TRUE.equals(existsEmail)
                        ? Mono.error(new BusinessException(ERROR_BUSINESS_EMAIL +
                        user.getEmail()))
                        : userRepository.existsByIdentityDocument(user.getIdentityDocument()))
                .flatMap(existsDocument -> Boolean.TRUE.equals(existsDocument)
                        ? Mono.error(new BusinessException(ERROR_BUSINESS_DOCUMENT +
                        user.getIdentityDocument()))
                        :Mono.just(user));
    }

}
