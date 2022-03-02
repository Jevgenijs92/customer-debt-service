package com.example.customerdebtservice.debt;

import com.example.customerdebtservice.currency.converters.CurrencyConverter;
import com.example.customerdebtservice.currency.dto.CurrencyData;
import com.example.customerdebtservice.currency.models.Currency;
import com.example.customerdebtservice.customer.models.Customer;
import com.example.customerdebtservice.debt.converters.DebtConverter;
import com.example.customerdebtservice.debt.dto.DebtData;
import com.example.customerdebtservice.debt.models.Debt;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class DebtConverterUnitTest {
    @Mock
    private CurrencyConverter currencyConverter;

    @InjectMocks
    private DebtConverter debtConverter;

    @Test
    public void convertDebtShouldReturnDebtData() {
        Debt debt = new Debt();
        debt.setId(1L);
        debt.setAmount(new BigDecimal(1));
        debt.setCurrency(new Currency(1L, "Dollar", "USD", "$"));
        debt.setDueDate(LocalDate.of(2022, 3, 2));
        Customer customer = new Customer();
        customer.setId(10L);
        debt.setCustomer(customer);

        when(currencyConverter.convert(any())).thenReturn(new CurrencyData());

        DebtData result = debtConverter.convert(debt);

        assert result != null;
        assertEquals(debt.getId(), result.getId());
        assertEquals(debt.getAmount(), result.getAmount());
        assertEquals(debt.getDueDate(), result.getDueDate());
        assertEquals(debt.getCustomer().getId(), result.getCustomerId());
    }
}
