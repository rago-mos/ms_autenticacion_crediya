package co.com.crediya.r2dbc.entities;


import co.com.crediya.r2dbc.enums.RoleEnum;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

@Table(name = "usuario")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Slf4j
public class UserEntity implements UserDetails {

    @Id
    @Column("id_usuario")
    private Long id;

    @Column("nombre")
    private String firstName;

    @Column("apellido")
    private String lastName;

    private String email;

    @Column("documento_identidad")
    private String identityDocument;

    @Column("password")
    private String password;

    @Column("fecha_nacimiento")
    private LocalDate birthDate;

    @Column("direccion")
    private String address;

    @Column("telefono")
    private String phoneNumber;

    @Column("id_rol")
    private Integer role;

    @Column("salario_base")
    private BigDecimal baseSalary;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if (role == null) {
            log.warn("No role found for ID: null");
            return List.of();
        }

        return RoleEnum.fromId(role)
                .map(r -> {
                    log.debug("Resolved role: {}", r.getAuthority());
                    return List.of(new SimpleGrantedAuthority(r.getAuthority()));
                })
                .orElseGet(() -> {
                    log.warn("No role found for ID: {}", role);
                    return List.of();
                });
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return identityDocument;
    }
}
