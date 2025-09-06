package co.com.crediya.r2dbc.entities;

import lombok.*;
import org.springframework.data.relational.core.mapping.Column;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class UserApplicationViewEntity {

    @Column("nombre")
    private String firstName;

    @Column("apellido")
    private String lastName;

    @Column("email")
    private String email;

    @Column("documento_identidad")
    private String identityDocument;

    @Column("salario_base")
    private BigDecimal baseSalary;
}
