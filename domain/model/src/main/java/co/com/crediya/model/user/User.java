package co.com.crediya.model.user;
import co.com.crediya.model.role.Role;
import lombok.*;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class User {

    private String firstName;
    private String lastName;
    private String email;
    private Long identityDocument;
    private LocalDate birthDate;
    private String address;
    private String phoneNumber;
    private Role role;
    private BigDecimal baseSalary;

}
