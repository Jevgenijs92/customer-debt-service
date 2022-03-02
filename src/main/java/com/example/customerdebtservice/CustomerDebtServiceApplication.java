package com.example.customerdebtservice;

import com.example.customerdebtservice.currency.services.CurrencyService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class CustomerDebtServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(CustomerDebtServiceApplication.class, args);
    }

    @Bean
    CommandLineRunner run(CurrencyService currencyService) {
        return args -> {
            currencyService.createCurrency("Euro", "EUR", "€");
            currencyService.createCurrency("Dollar", "USD", "$");
            currencyService.createCurrency("Lats", "LVL", "Ls");
        };
    }
}
