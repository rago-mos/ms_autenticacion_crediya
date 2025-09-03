package co.com.crediya.api.mapper;

import co.com.crediya.api.dto.LoginRequest;
import co.com.crediya.model.login.LoginDTO;
import org.springframework.stereotype.Component;

@Component
public class LoginMapper {

    public LoginDTO toModel(LoginRequest dto) {
        return LoginDTO.builder()
                .identityDocument(dto.identityDocument())
                .password(dto.password())
                .build();
    }
}
