package com.example.customerdebtservice.debt.repositories;

import com.example.customerdebtservice.customer.models.Customer;
import com.example.customerdebtservice.debt.models.Debt;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DebtRepository extends JpaRepository<Debt, Long> {
    List<Debt> findAllByCustomerId(Long customerId);
}
