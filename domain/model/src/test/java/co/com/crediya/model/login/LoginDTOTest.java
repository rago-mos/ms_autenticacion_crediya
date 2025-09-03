package co.com.crediya.model.login;


import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

class LoginDTOTest {

    @Test
    void shouldBuildLoginDTOCorrectly() {
        LoginDTO dto = LoginDTO.builder()
                .identityDocument("123456789")
                .password("securePass")
                .build();

        assertEquals("123456789", dto.identityDocument());
        assertEquals("securePass", dto.password());
    }

    @Test
    void shouldSupportEqualityAndHashCode() {
        LoginDTO dto1 = LoginDTO.builder()
                .identityDocument("123456789")
                .password("securePass")
                .build();

        LoginDTO dto2 = LoginDTO.builder()
                .identityDocument("123456789")
                .password("securePass")
                .build();

        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void shouldSupportToString() {
        LoginDTO dto = LoginDTO.builder()
                .identityDocument("123456789")
                .password("securePass")
                .build();

        String expected = "LoginDTO[identityDocument=123456789, password=securePass]";
        assertEquals(expected, dto.toString());
    }
}