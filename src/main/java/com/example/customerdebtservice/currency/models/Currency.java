package com.example.customerdebtservice.currency.models;

import lombok.*;

import javax.persistence.*;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = {"name", "code", "symbol"})
@ToString(of = {"symbol"})
public class Currency {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "sequence_currency")
    private Long id;

    private String name;

    private String code;

    private String symbol;
}
