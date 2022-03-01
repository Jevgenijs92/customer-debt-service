package com.example.customerdebtservice.debt.dto;

import com.example.customerdebtservice.currency.dto.CurrencyData;
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
    private Long customerId;
    private CurrencyData currency;
}
