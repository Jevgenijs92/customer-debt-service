package com.example.customerdebtservice.currency.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class CurrencyData {
    private Long id;
    private String name;
    private String code;
    private String symbol;
}
