package com.example.customerdebtservice.customer;

import com.example.customerdebtservice.customer.converters.CustomerConverter;
import com.example.customerdebtservice.customer.dto.CustomerData;
import com.example.customerdebtservice.customer.models.Customer;
import com.example.customerdebtservice.debt.converters.DebtConverter;
import com.example.customerdebtservice.debt.dto.DebtData;
import com.example.customerdebtservice.debt.models.Debt;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CustomerConverterUnitTest {
    @Mock
    private DebtConverter debtConverter;

    @InjectMocks
    private CustomerConverter customerConverter;

    @Test
    public void convertCustomerShouldReturnCustomerData() {
        Customer customer = new Customer();
        customer.setId(1L);
        customer.setName("Customer");
        customer.setSurname("Surname");
        customer.setPassword("password");
        customer.setEmail("email");

        List<Debt> debts = new ArrayList<>();
        Debt testDebt = new Debt();
        testDebt.setId(10L);
        debts.add(testDebt);
        customer.setDebts(debts);

        when(debtConverter.convert(any())).thenReturn(new DebtData());
        CustomerData result = customerConverter.convert(customer);

        assert result != null;
        assertEquals(customer.getId(), result.getId());
        assertEquals(customer.getName(), result.getName());
        assertEquals(customer.getSurname(), result.getSurname());
        assertEquals(customer.getEmail(), result.getEmail());
    }
}
