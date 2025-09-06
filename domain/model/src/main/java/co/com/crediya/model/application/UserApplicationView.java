package co.com.crediya.model.application;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class UserApplicationView {

    private String firstName;
    private String lastName;
    private String email;
    private String identityDocument;
    private BigDecimal baseSalary;

}
