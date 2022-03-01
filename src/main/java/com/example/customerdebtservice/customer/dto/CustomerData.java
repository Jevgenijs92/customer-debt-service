package com.example.customerdebtservice.customer.dto;

import com.example.customerdebtservice.debt.dto.DebtData;
import lombok.*;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class CustomerData {
    private Long id;
    private String name;
    private String surname;
    private String country;
    private String email;
    private List<DebtData> debts;
}
