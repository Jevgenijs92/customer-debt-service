package com.example.customerdebtservice.debt.converters;

import com.example.customerdebtservice.currency.converters.CurrencyConverter;
import com.example.customerdebtservice.debt.dto.DebtData;
import com.example.customerdebtservice.debt.models.Debt;
import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
@RequiredArgsConstructor
public class DebtConverter implements Converter<Debt, DebtData> {

    private final CurrencyConverter currencyConverter;

    @Override
    public DebtData convert(Debt source) {
        DebtData target = new DebtData();
        target.setId(source.getId());
        target.setAmount(source.getAmount());
        target.setDueDate(source.getDueDate());
        if(Objects.nonNull(source.getCustomer())){
            target.setCustomerId(source.getCustomer().getId());
        }
        if(Objects.nonNull(source.getCurrency())){
            target.setCurrency(currencyConverter.convert(source.getCurrency()));
        }
        return target;
    }
}
