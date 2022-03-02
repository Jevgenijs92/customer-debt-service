package com.example.customerdebtservice.currency.services.impl;

import com.example.customerdebtservice.currency.converters.CurrencyConverter;
import com.example.customerdebtservice.currency.dto.CurrencyData;
import com.example.customerdebtservice.currency.models.Currency;
import com.example.customerdebtservice.currency.repositories.CurrencyRepository;
import com.example.customerdebtservice.currency.services.CurrencyService;
import com.example.customerdebtservice.shared.exceptions.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CurrencyServiceImpl implements CurrencyService {
    private final CurrencyRepository currencyRepository;

    @Override
    public Currency getCurrencyByCode(String code) throws ResourceNotFoundException {
        return currencyRepository.findCurrencyByCode(code)
                .orElseThrow(() -> new ResourceNotFoundException("Cannot find currency for code: " + code));
    }

}
