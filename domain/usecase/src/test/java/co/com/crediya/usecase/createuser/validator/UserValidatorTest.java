package co.com.crediya.usecase.createuser.validator;

import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;

import static org.junit.jupiter.api.Assertions.*;

class UserValidatorTest {

    @Test
    void shouldCoverPrivateConstructor() throws Exception {
        Constructor<UserValidator> constructor = UserValidator.class.getDeclaredConstructor();
        constructor.setAccessible(true);
        UserValidator instance = constructor.newInstance();
        assertNotNull(instance);
    }

}