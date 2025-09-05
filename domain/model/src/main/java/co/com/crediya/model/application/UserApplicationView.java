package co.com.crediya.model.application;

import java.math.BigDecimal;

public interface UserApplicationView {
    String getFirstName();
    String getLastName();
    String getEmail();
    String getIdentityDocument();
    BigDecimal getBaseSalary();
}

