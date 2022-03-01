package com.example.customerdebtservice.debt.dto;

import com.example.customerdebtservice.currency.models.Currency;
import com.example.customerdebtservice.customer.dto.CustomerData;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class DebtData {
    private Long id;
    private BigDecimal amount;
    private LocalDate dueDate;
    private CustomerData customer;
    private Currency currency;
}
