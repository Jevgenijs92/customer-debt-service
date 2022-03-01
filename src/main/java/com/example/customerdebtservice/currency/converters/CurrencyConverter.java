package com.example.customerdebtservice.currency.converters;

import com.example.customerdebtservice.currency.dto.CurrencyData;
import com.example.customerdebtservice.currency.models.Currency;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class CurrencyConverter implements Converter<Currency, CurrencyData> {
    @Override
    public CurrencyData convert(Currency source) {
        CurrencyData target = new CurrencyData();
        target.setId(source.getId());
        target.setName(source.getName());
        target.setCode(source.getCode());
        target.setSymbol(source.getSymbol());
        return target;
    }
}
