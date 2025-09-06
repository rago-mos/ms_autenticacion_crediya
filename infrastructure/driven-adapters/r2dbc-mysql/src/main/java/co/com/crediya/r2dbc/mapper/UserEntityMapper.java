package co.com.crediya.r2dbc.mapper;

import co.com.crediya.model.application.UserApplicationView;
import co.com.crediya.model.role.Role;
import co.com.crediya.model.user.User;
import co.com.crediya.r2dbc.entities.UserApplicationViewEntity;
import co.com.crediya.r2dbc.entities.UserEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserEntityMapper {

    private final PasswordEncoder passwordEncoder;

    public UserEntity toEntity(User user) {
        return UserEntity.builder()
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .identityDocument(user.getIdentityDocument())
                .password(passwordEncoder.encode(user.getPassword()))
                .birthDate(user.getBirthDate())
                .address(user.getAddress())
                .phoneNumber(user.getPhoneNumber())
                .role(user.getRole() != null ? user.getRole().getIdRol() : null)
                .baseSalary(user.getBaseSalary())
                .build();
    }

    public User toDomain(UserEntity entity) {
        return User.builder()
                .firstName(entity.getFirstName())
                .lastName(entity.getLastName())
                .email(entity.getEmail())
                .identityDocument(entity.getIdentityDocument())
                .password(entity.getPassword())
                .birthDate(entity.getBirthDate())
                .address(entity.getAddress())
                .phoneNumber(entity.getPhoneNumber())
                .role(buildRole(entity.getRole()))
                .baseSalary(entity.getBaseSalary())
                .build();
    }

    public UserApplicationView toView(UserApplicationViewEntity  entity) {
        return UserApplicationView.builder()
                .firstName(entity.getFirstName())
                .lastName(entity.getLastName())
                .email(entity.getEmail())
                .identityDocument(entity.getIdentityDocument())
                .baseSalary(entity.getBaseSalary())
                .build();
    }

    private Role buildRole(Integer rol) {
        if (rol == null) return null;

        return Role.builder()
                .idRol(rol)
                .build();
    }
}
