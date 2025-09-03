package co.com.crediya.r2dbc.enums;

import java.util.Arrays;
import java.util.Optional;

public enum RoleEnum {

        ADMIN(1, "ADMIN"),
        ASESOR(2, "ASESOR"),
        CLIENTE(3, "CLIENTE");

        private final int id;
        private final String authority;

        RoleEnum(int id, String authority) {
            this.id = id;
            this.authority = authority;
        }

        public static Optional<RoleEnum> fromId(int id) {
            return Arrays.stream(values())
                    .filter(role -> role.id == id)
                    .findFirst();
        }

        public String getAuthority() {
            return authority;
        }
}
