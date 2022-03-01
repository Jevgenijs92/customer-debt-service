package com.example.customerdebtservice.currency.repositories;

import com.example.customerdebtservice.currency.models.Currency;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CurrencyRepository extends JpaRepository<Currency, Long> {
}
