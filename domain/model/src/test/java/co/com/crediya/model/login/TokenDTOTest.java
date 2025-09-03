package co.com.crediya.model.login;


import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

class TokenDTOTest {

    @Test
    void shouldCreateTokenDTOCorrectly() {
        TokenDTO dto = new TokenDTO("mocked-jwt-token");

        assertEquals("mocked-jwt-token", dto.token());
    }

    @Test
    void shouldSupportEqualityAndHashCode() {
        TokenDTO dto1 = new TokenDTO("mocked-jwt-token");
        TokenDTO dto2 = new TokenDTO("mocked-jwt-token");

        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void shouldSupportToString() {
        TokenDTO dto = new TokenDTO("mocked-jwt-token");

        assertEquals("TokenDTO[token=mocked-jwt-token]", dto.toString());
    }
}