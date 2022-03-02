package com.example.customerdebtservice.customer.models;

import com.example.customerdebtservice.debt.models.Debt;
import lombok.*;

import javax.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "customer")
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = {"id", "email"})
@ToString(of = {"name", "surname", "email"})
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "sequence_customer")
    private Long id;

    private String name;

    private String surname;

    private String country;

    private String email;

    private String password;

    @OneToMany(mappedBy = "customer", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private List<Debt> debts = new ArrayList<>();

}
