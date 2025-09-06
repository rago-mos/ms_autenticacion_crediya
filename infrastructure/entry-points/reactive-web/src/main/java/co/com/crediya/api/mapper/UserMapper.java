package co.com.crediya.api.mapper;

import co.com.crediya.api.dto.CreateUserRequest;
import co.com.crediya.api.dto.CreateUserResponse;
import co.com.crediya.api.dto.UserApplicationResponse;
import co.com.crediya.model.application.UserApplicationView;
import co.com.crediya.model.role.Role;
import co.com.crediya.model.user.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public User toModel(CreateUserRequest dto) {
        return User.builder()
                .firstName(dto.firstName())
                .lastName(dto.lastName())
                .email(dto.email())
                .identityDocument(dto.identityDocument())
                .password(dto.password())
                .birthDate(dto.birthDate())
                .address(dto.address())
                .phoneNumber(dto.phoneNumber())
                .role(buildRole(dto.idRol()))
                .baseSalary(dto.baseSalary())
                .build();
    }

    public CreateUserResponse toResponse(User user) {
        return CreateUserResponse.builder()
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .identityDocument(user.getIdentityDocument())
                .birthDate(user.getBirthDate())
                .address(user.getAddress())
                .phoneNumber(user.getPhoneNumber())
                .rol(user.getRole().getName())
                .baseSalary(user.getBaseSalary())
                .build();
    }

    public UserApplicationResponse toDTO(UserApplicationView view) {
        return UserApplicationResponse.builder()
                .firstName(view.getFirstName())
                .lastName(view.getLastName())
                .email(view.getEmail())
                .identityDocument(view.getIdentityDocument())
                .baseSalary(view.getBaseSalary())
                .build();
    }

    private Role buildRole(Integer rol) {
        if (rol == null) return null;

        return Role.builder()
                .idRol(rol)
                .name("")
                .description("")
                .build();
    }
}
