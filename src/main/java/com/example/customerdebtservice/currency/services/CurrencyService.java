package com.example.customerdebtservice.currency.services;

import com.example.customerdebtservice.currency.dto.CurrencyData;
import com.example.customerdebtservice.currency.models.Currency;
import com.example.customerdebtservice.shared.exceptions.ResourceExistsException;
import com.example.customerdebtservice.shared.exceptions.ResourceNotFoundException;
import org.springframework.stereotype.Service;

@Service
public interface CurrencyService {
    /**
     * Retrieve Currency object by code
     * @param code contains 3 symbols
     * @return Currency object
     * @throws ResourceNotFoundException when Currency doesn't exist
     */
    Currency getCurrencyByCode(String code) throws ResourceNotFoundException;
}
