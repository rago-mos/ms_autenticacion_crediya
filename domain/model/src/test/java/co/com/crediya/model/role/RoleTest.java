package co.com.crediya.model.role;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RoleTest {

    @Test
    void shouldCreateRoleWithBuilder() {
        Role role = Role.builder()
                .idRol(1)
                .name("ADMIN")
                .description("Administrator role")
                .build();

        assertEquals(1, role.getIdRol());
        assertEquals("ADMIN", role.getName());
        assertEquals("Administrator role", role.getDescription());
    }

    @Test
    void shouldUseSettersAndGetters() {
        Role role = new Role();
        role.setIdRol(2);
        role.setName("USER");
        role.setDescription("Standard user");

        assertEquals(2, role.getIdRol());
        assertEquals("USER", role.getName());
        assertEquals("Standard user", role.getDescription());
    }

    @Test
    void shouldCopyRoleWithToBuilder() {
        Role original = Role.builder()
                .idRol(3)
                .name("MANAGER")
                .description("Manager role")
                .build();

        Role copy = original.toBuilder()
                .description("Updated manager role")
                .build();

        assertEquals("MANAGER", copy.getName());
        assertEquals("Updated manager role", copy.getDescription());
        assertEquals(3, copy.getIdRol());
    }

    @Test
    void shouldCreateRoleWithAllArgsConstructor() {
        Role role = new Role(4, "SUPPORT", "Support team");

        assertEquals(4, role.getIdRol());
        assertEquals("SUPPORT", role.getName());
        assertEquals("Support team", role.getDescription());
    }

    @Test
    void shouldCreateRoleWithNoArgsConstructor() {
        Role role = new Role();
        assertNotNull(role);
    }

}