package com.example.customerdebtservice.debt.models;

import com.example.customerdebtservice.currency.models.Currency;
import com.example.customerdebtservice.customer.models.Customer;
import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = {"id"})
@ToString(of = {"id", "amount"})
public class Debt {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "sequence_debt")
    private Long id;

    private BigDecimal amount;

    @Column(name = "due_date")
    private LocalDate dueDate;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.DETACH)
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @ManyToOne
    @JoinColumn(name = "currency_id", referencedColumnName = "id")
    private Currency currency;

}
