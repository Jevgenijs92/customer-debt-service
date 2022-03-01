package com.example.customerdebtservice.customer.converters;

import com.example.customerdebtservice.customer.dto.CustomerData;
import com.example.customerdebtservice.customer.models.Customer;
import com.example.customerdebtservice.debt.converters.DebtConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class CustomerConverter implements Converter<Customer, CustomerData> {

    private final DebtConverter debtConverter;

    @Override
    public CustomerData convert(Customer source) {
        CustomerData target = new CustomerData();
        target.setId(source.getId());
        target.setName(source.getName());
        target.setSurname(source.getSurname());
        target.setCountry(source.getCountry());
        target.setEmail(source.getEmail());

        if(Objects.nonNull(source.getDebts())){
            target.setDebts(source.getDebts().stream().map(debtConverter::convert).collect(Collectors.toList()));
        }
        return target;
    }
}
