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
    @SequenceGenerator(name = "sequence_currency", sequenceName = "sequence_currency", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequence_currency")
    private Long id;

    private String name;

    private String code;

    private String symbol;
}
