package co.com.crediya.security.jwt.provider;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.SignatureException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.spy;

class JwtProviderTest {

    private JwtProvider jwtProvider;

    @BeforeEach
    void setUp() {
        jwtProvider = new JwtProvider();

        ReflectionTestUtils.setField(jwtProvider, "secret", "my-super-secret-key-12345678901234567890123456789012");
        ReflectionTestUtils.setField(jwtProvider, "expiration", "3600000");
    }

    @Test
    void shouldGenerateAndValidateTokenSuccessfully() {
        UserDetails userDetails = new User("admin", "password", List.of(new SimpleGrantedAuthority("ROLE_ADMIN")));

        String token = jwtProvider.generateToken(userDetails);
        assertNotNull(token);

        assertTrue(jwtProvider.validate(token));

        Claims claims = jwtProvider.getClaims(token);
        assertEquals("admin", claims.getSubject());
        assertNotNull(claims.get("roles"));

        String subject = jwtProvider.getSubject(token);
        assertEquals("admin", subject);
    }

    @Test
    void shouldFailValidationForMalformedToken() {
        String malformedToken = "this.is.not.a.valid.jwt";

        boolean isValid = jwtProvider.validate(malformedToken);
        assertFalse(isValid);
    }

    @Test
    void shouldReturnFalseWhenTokenIsExpired() {
        JwtProvider provider = new JwtProvider();
        ReflectionTestUtils.setField(provider, "secret", "my-super-secret-key-12345678901234567890123456789012");

        JwtProvider spyProvider = spy(provider);
        doThrow(new ExpiredJwtException(null, null, "Token expired")).when(spyProvider).getClaims("expired-token");

        boolean result = spyProvider.validate("expired-token");
        assertFalse(result);
    }

    @Test
    void shouldReturnFalseWhenTokenIsUnsupported() {
        JwtProvider provider = new JwtProvider();
        ReflectionTestUtils.setField(provider, "secret", "my-super-secret-key-12345678901234567890123456789012");

        JwtProvider spyProvider = spy(provider);
        doThrow(new UnsupportedJwtException("Unsupported token")).when(spyProvider).getClaims("unsupported-token");

        boolean result = spyProvider.validate("unsupported-token");
        assertFalse(result);
    }

    @Test
    void shouldReturnFalseWhenTokenIsMalformed() {
        JwtProvider provider = new JwtProvider();
        ReflectionTestUtils.setField(provider, "secret", "my-super-secret-key-12345678901234567890123456789012");

        JwtProvider spyProvider = spy(provider);
        doThrow(new MalformedJwtException("Malformed token")).when(spyProvider).getClaims("malformed-token");

        boolean result = spyProvider.validate("malformed-token");
        assertFalse(result);
    }

    @Test
    void shouldReturnFalseWhenTokenHasBadSignature() {
        JwtProvider provider = new JwtProvider();
        ReflectionTestUtils.setField(provider, "secret", "my-super-secret-key-12345678901234567890123456789012");

        JwtProvider spyProvider = spy(provider);
        doThrow(new SignatureException("Bad signature")).when(spyProvider).getClaims("bad-signature-token");

        boolean result = spyProvider.validate("bad-signature-token");
        assertFalse(result);
    }

    @Test
    void shouldReturnFalseWhenTokenIsIllegal() {
        JwtProvider provider = new JwtProvider();
        ReflectionTestUtils.setField(provider, "secret", "my-super-secret-key-12345678901234567890123456789012");

        JwtProvider spyProvider = spy(provider);
        doThrow(new IllegalArgumentException("Illegal token")).when(spyProvider).getClaims("illegal-token");

        boolean result = spyProvider.validate("illegal-token");
        assertFalse(result);
    }

    @Test
    void shouldReturnFalseWhenTokenHasBadSignatureTwo() {
        String tokenWithBadSignature = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhZG1pbiJ9.bad-signature";

        boolean result = jwtProvider.validate(tokenWithBadSignature);
        assertFalse(result);
    }

    @Test
    void shouldReturnFalseWhenTokenIsIllegalTwo() {
        String illegalToken = "";

        boolean result = jwtProvider.validate(illegalToken);
        assertFalse(result);
    }

    @Test
    void shouldReturnFalseAndLogExpiredJwtException() throws InterruptedException {
        JwtProvider provider = new JwtProvider();
        ReflectionTestUtils.setField(provider, "secret", "my-super-secret-key-12345678901234567890123456789012");
        ReflectionTestUtils.setField(provider, "expiration", "1");

        UserDetails userDetails = new User("admin", "pass", List.of());
        String token = provider.generateToken(userDetails);

        Thread.sleep(10);

        boolean result = provider.validate(token);
        assertFalse(result);
    }

    @Test
    void shouldReturnFalseAndLogUnsupportedJwtException() {
        String unsupportedToken = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhZG1pbiJ9";

        JwtProvider provider = new JwtProvider();
        ReflectionTestUtils.setField(provider, "secret", "my-super-secret-key-12345678901234567890123456789012");

        boolean result = provider.validate(unsupportedToken);
        assertFalse(result);
    }
}