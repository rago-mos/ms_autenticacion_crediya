package co.com.crediya.r2dbc.entities;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class RoleEntityTest {

    @Test
    void shouldBuildRoleEntityCorrectly() {
        RoleEntity entity = RoleEntity.builder()
                .idRol(1)
                .name("ADMIN")
                .description("Administrator role")
                .build();

        assertEquals(1, entity.getIdRol());
        assertEquals("ADMIN", entity.getName());
        assertEquals("Administrator role", entity.getDescription());
    }

    @Test
    void shouldUseSettersAndGetters() {
        RoleEntity entity = new RoleEntity();
        entity.setName("USER");
        entity.setDescription("Standard user");

        assertEquals("USER", entity.getName());
        assertEquals("Standard user", entity.getDescription());
    }

    @Test
    void shouldCopyRoleEntityWithToBuilder() {
        RoleEntity original = RoleEntity.builder()
                .name("ADMIN")
                .build();

        RoleEntity copy = original.builder()
                .name("SUPERADMIN")
                .build();

        assertEquals("SUPERADMIN", copy.getName());
    }

    @Test
    void shouldCreateRoleEntityWithAllArgsConstructor() {
        RoleEntity entity = new RoleEntity(1, "ADMIN", "Administrator role");

        assertNotNull(entity);
        assertEquals("ADMIN", entity.getName());
    }

    @Test
    void shouldCreateRoleEntityWithNoArgsConstructor() {
        RoleEntity entity = new RoleEntity();
        assertNotNull(entity);
    }
}