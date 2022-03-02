package com.example.customerdebtservice.debt.forms;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DebtForm {
    @NotNull
    @DecimalMin(value = "0.00", message = "Amount cannot be negative")
    private BigDecimal amount;

    @NotBlank
    @Size(min = 3, max = 3, message = "Currency code should be exactly 3 symbols")
    private String currency;

    @NotNull(message = "Date must not be null")
    private LocalDate dueDate;

    @NotNull(message = "Customer ID must not be null")
    private Long customerId;
}
