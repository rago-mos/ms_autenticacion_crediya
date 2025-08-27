package co.com.crediya.r2dbc.entities;


import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.math.BigDecimal;
import java.time.LocalDate;

@Table(name = "usuario")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class UserEntity {

    @Id
    @Column("id_usuario")
    private Long id;

    @Column("nombre")
    private String firstName;

    @Column("apellido")
    private String lastName;

    private String email;

    @Column("documento_identidad")
    private Long identityDocument;

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

}
