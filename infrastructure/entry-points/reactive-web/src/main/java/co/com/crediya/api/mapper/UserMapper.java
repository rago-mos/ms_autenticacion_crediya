package co.com.crediya.api.mapper;

import co.com.crediya.api.dto.CreateUserRequest;
import co.com.crediya.api.dto.CreateUserResponse;
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
                .birthDate(dto.birthDate())
                .address(dto.address())
                .phoneNumber(dto.phoneNumber())
                .role(buildRole(dto.rol()))
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
                .rol(user.getRole() != null ? user.getRole().getName() : null)
                .baseSalary(user.getBaseSalary())
                .build();
    }

    private Role buildRole(String roleName) {
        if (roleName == null || roleName.isBlank()) return null;

        return Role.builder()
                .name(roleName)
                .description("")
                .build();
    }
}
