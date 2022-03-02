package com.example.customerdebtservice.currency;

import com.example.customerdebtservice.currency.converters.CurrencyConverter;
import com.example.customerdebtservice.currency.models.Currency;
import com.example.customerdebtservice.currency.repositories.CurrencyRepository;
import com.example.customerdebtservice.currency.services.impl.CurrencyServiceImpl;
import com.example.customerdebtservice.shared.exceptions.ResourceNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CurrencyServiceUnitTest {
    @Mock
    private CurrencyRepository currencyRepository;

    @Mock
    private CurrencyConverter currencyConverter;

    @InjectMocks
    private CurrencyServiceImpl currencyService;

    private static final Currency currency = new Currency(1L, "Euro", "EUR", "€");

    @Test
    public void getCurrencyByCodeShouldReturnCurrency() {
        when(currencyRepository.findCurrencyByCode(any())).thenReturn(Optional.of(currency));

        Currency result = currencyService.getCurrencyByCode(currency.getCode());
        assertEquals(result.getId(), currency.getId());
        assertEquals(result.getName(), currency.getName());
        assertEquals(result.getCode(), currency.getCode());
        assertEquals(result.getSymbol(), currency.getSymbol());
    }

    @Test
    public void getCurrencyByCodeWithWrongCodeShouldReturnResourceNotFoundException() {
        when(currencyRepository.findCurrencyByCode(any())).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> currencyService.getCurrencyByCode("EUR"));
    }
}
