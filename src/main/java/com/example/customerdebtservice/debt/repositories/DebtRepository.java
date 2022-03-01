package com.example.customerdebtservice.debt.repositories;

import com.example.customerdebtservice.debt.models.Debt;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DebtRepository extends JpaRepository<Debt, Long> {
}
