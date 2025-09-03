package co.com.crediya.api.mapper;

import co.com.crediya.api.dto.LoginRequest;
import co.com.crediya.model.login.LoginDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LoginMapperTest {

    private LoginMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = new LoginMapper();
    }

    @Test
    void shouldMapLoginRequestToLoginDTOCorrectly() {
        LoginRequest request = new LoginRequest("123456789", "securePass");

        LoginDTO dto = mapper.toModel(request);

        assertEquals("123456789", dto.identityDocument());
        assertEquals("securePass", dto.password());
    }
}