package com.example.customerdebtservice.currency.services;

import com.example.customerdebtservice.currency.dto.CurrencyData;
import com.example.customerdebtservice.currency.models.Currency;
import com.example.customerdebtservice.shared.exceptions.ResourceNotFoundException;
import org.springframework.stereotype.Service;

@Service
public interface CurrencyService {
    Currency getCurrencyByCode(String code) throws ResourceNotFoundException;
    CurrencyData createCurrency(String name, String code, String symbol);
}
