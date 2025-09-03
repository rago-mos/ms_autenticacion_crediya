package co.com.crediya.r2dbc.enums;

import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class RoleEnumTest {

    @Test
    void shouldReturnAdminRoleFromId() {
        Optional<RoleEnum> role = RoleEnum.fromId(1);
        assertTrue(role.isPresent());
        assertEquals(RoleEnum.ADMIN, role.get());
        assertEquals("ADMIN", role.get().getAuthority());
    }

    @Test
    void shouldReturnAsesorRoleFromId() {
        Optional<RoleEnum> role = RoleEnum.fromId(2);
        assertTrue(role.isPresent());
        assertEquals(RoleEnum.ASESOR, role.get());
        assertEquals("ASESOR", role.get().getAuthority());
    }

    @Test
    void shouldReturnClienteRoleFromId() {
        Optional<RoleEnum> role = RoleEnum.fromId(3);
        assertTrue(role.isPresent());
        assertEquals(RoleEnum.CLIENTE, role.get());
        assertEquals("CLIENTE", role.get().getAuthority());
    }

    @Test
    void shouldReturnEmptyForInvalidId() {
        Optional<RoleEnum> role = RoleEnum.fromId(99);
        assertFalse(role.isPresent());
    }

    @Test
    void shouldExposeAuthorityCorrectly() {
        assertEquals("ADMIN", RoleEnum.ADMIN.getAuthority());
        assertEquals("ASESOR", RoleEnum.ASESOR.getAuthority());
        assertEquals("CLIENTE", RoleEnum.CLIENTE.getAuthority());
    }
}