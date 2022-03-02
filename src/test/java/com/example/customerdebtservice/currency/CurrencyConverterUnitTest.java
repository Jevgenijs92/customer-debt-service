package com.example.customerdebtservice.currency;

import com.example.customerdebtservice.currency.converters.CurrencyConverter;
import com.example.customerdebtservice.currency.dto.CurrencyData;
import com.example.customerdebtservice.currency.models.Currency;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
public class CurrencyConverterUnitTest {
    @InjectMocks
    private CurrencyConverter currencyConverter;

    @Test
    public void convertCurrencyShouldReturnCurrencyData() {
        Currency currency = new Currency(1L, "Dollar", "USD", "$");
        CurrencyData result = currencyConverter.convert(currency);
        assert result != null;
        assertEquals(currency.getId(), result.getId());
        assertEquals(currency.getName(), result.getName());
        assertEquals(currency.getCode(), result.getCode());
        assertEquals(currency.getSymbol(), result.getSymbol());
    }
}
