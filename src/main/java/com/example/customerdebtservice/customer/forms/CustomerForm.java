package com.example.customerdebtservice.customer.forms;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomerForm {
    @NotBlank(message = "Name cannot be empty")
    private String name;
    @NotBlank(message = "Surname cannot be empty")
    private String surname;
    @NotBlank(message = "Country cannot be empty")
    private String country;
    @NotBlank(message = "Email cannot be empty")
    @Email(message = "Email is not valid",
            regexp = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$") // OWASP
    private String email;
    @NotBlank(message = "Password cannot be empty")
    private String password;
}
