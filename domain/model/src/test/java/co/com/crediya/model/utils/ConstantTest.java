package co.com.crediya.model.utils;

import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import static org.junit.jupiter.api.Assertions.*;

class ConstantTest {

    @Test
    void shouldContainExpectedConstants() {
        assertEquals("documentIdentity", Constant.PATH_VARIABLE);
        assertEquals("Path Variable 'document' is required", Constant.MISSING_PATH_VARIABLE);
        assertEquals("User created successfully: {}", Constant.LOG_INFO_USER_CREATED);
        assertEquals("User exists: {}", Constant.LOG_INFO_USER_EXISTS);
        assertEquals("token created successfully", Constant.LOG_INFO_TOKEN);
        assertEquals("A user with this document already exists ", Constant.ERROR_BUSINESS_DOCUMENT);
        assertEquals("A user with this email already exists ", Constant.ERROR_BUSINESS_EMAIL);
        assertEquals("The salary is not valid; it must be between %s and %s", Constant.ERROR_BUSINESS_SALARY);
        assertEquals("Last name is null or blank", Constant.ERROR_REQUEST_LASTNAME);
        assertEquals("firtsName is null or blank", Constant.ERROR_REQUEST_FIRTSNAME);
        assertEquals("bad credentials", Constant.ERROR_BAD_CREDENTIALS);
        assertEquals("role not found", Constant.ERROR_ROLE);
        assertEquals("bad token", Constant.ERROR_BAD_TOKEN);
    }

    @Test
    void shouldNotAllowInstantiation() throws Exception {
        Constructor<Constant> constructor = Constant.class.getDeclaredConstructor();
        constructor.setAccessible(true);

        Exception exception = assertThrows(InvocationTargetException.class, constructor::newInstance);
        assertTrue(exception.getCause() instanceof UnsupportedOperationException);
        assertEquals("util class", exception.getCause().getMessage());
    }
}