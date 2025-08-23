package co.com.crediya.model.user;
import lombok.*;
import lombok.NoArgsConstructor;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class User {

    private String name;
    private String lastName;
    private String email;
    private Long identityDocument;
    private Long phoneNumber;
    private Integer roleId;
    private Long baseSalary;

}
