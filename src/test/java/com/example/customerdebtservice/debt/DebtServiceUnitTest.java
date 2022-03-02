package com.example.customerdebtservice.debt;

import com.example.customerdebtservice.currency.dto.CurrencyData;
import com.example.customerdebtservice.currency.models.Currency;
import com.example.customerdebtservice.currency.services.CurrencyService;
import com.example.customerdebtservice.customer.models.Customer;
import com.example.customerdebtservice.customer.repositories.CustomerRepository;
import com.example.customerdebtservice.customer.services.CustomerService;
import com.example.customerdebtservice.debt.converters.DebtConverter;
import com.example.customerdebtservice.debt.dto.DebtData;
import com.example.customerdebtservice.debt.forms.DebtForm;
import com.example.customerdebtservice.debt.models.Debt;
import com.example.customerdebtservice.debt.repositories.DebtRepository;
import com.example.customerdebtservice.debt.services.impl.DebtServiceImpl;
import com.example.customerdebtservice.shared.exceptions.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class DebtServiceUnitTest {
    @Mock
    private DebtRepository debtRepository;

    @Mock
    private DebtConverter debtConverter;

    @Mock
    private CustomerService customerService;

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private CurrencyService currencyService;

    @InjectMocks
    private DebtServiceImpl debtService;

    private static final List<Debt> debts = new ArrayList<>();
    private static final List<DebtData> expectedDebtDatalist = new ArrayList<>();

    @BeforeAll
    public static void setup() {
        for(long i = 1; i <= 20; i++) {
            Debt debt = new Debt();
            debt.setId(i);
            debt.setAmount(new BigDecimal(i));
            debt.setDueDate(LocalDate.of(2022, 2, (int) i));
            debt.setCurrency(new Currency());
            debt.setCustomer(new Customer());
            debts.add(debt);

            DebtData debtData = new DebtData();
            debtData.setId(i);
            debtData.setAmount(new BigDecimal(i));
            debtData.setDueDate(LocalDate.of(2022, 2, (int) i));
            debtData.setCurrency(new CurrencyData());
            debtData.setCustomerId(i);
            expectedDebtDatalist.add(debtData);
        }
    }

    @Test
    public void getAllDebtsShouldReturnDebtDataList() {
        Page<Debt> debtPage = new PageImpl<>(debts);
        Pageable pageable = PageRequest.of(0, 20);
        when(debtRepository.findAll(pageable)).thenReturn(debtPage);
        when(debtConverter.convert(any(Debt.class))).thenReturn(expectedDebtDatalist.get(0));

        List<DebtData> result = debtService.getDebts(pageable);
        assertEquals(result.size(), debts.size(),
                "Size of initial list of debts doesn't equal to the list returned by service");

    }

    @Test
    public void getDebtByIdShouldReturnDebt() {
        when(debtRepository.findById(any(Long.class))).thenReturn(Optional.ofNullable(debts.get(0)));
        when(debtConverter.convert(any(Debt.class))).thenReturn(expectedDebtDatalist.get(0));

        DebtData result = debtService.getDebtById(1L);
        assertEquals(result.getId(), expectedDebtDatalist.get(0).getId());
        assertEquals(result.getAmount(), expectedDebtDatalist.get(0).getAmount());
        assertEquals(result.getDueDate(), expectedDebtDatalist.get(0).getDueDate());
        assertEquals(result.getCustomerId(), expectedDebtDatalist.get(0).getCustomerId());
        assertEquals(result.getCurrency(), expectedDebtDatalist.get(0).getCurrency());
    }

    @Test
    public void getDebtByIdWithWrongIdShouldReturnResourceNotFoundException() {
        when(debtRepository.findById(any(Long.class))).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> debtService.getDebtById(1L));
    }

    @Test
    public void createDebtShouldReturnNewCreatedDebtData() {
        when(debtRepository.save(any(Debt.class))).thenReturn(debts.get(0));
        when(debtConverter.convert(any(Debt.class))).thenReturn(expectedDebtDatalist.get(0));

        DebtData result = debtService.createDebt(new DebtForm());
        assertEquals(expectedDebtDatalist.get(0), result);
    }

    @Test
    public void updateDebtShouldReturnUpdatedDebtData() {
        when(debtRepository.findById(any(Long.class))).thenReturn(Optional.of(new Debt()));
        when(debtRepository.save(any(Debt.class))).thenReturn(debts.get(0));
        when(debtConverter.convert(any(Debt.class))).thenReturn(expectedDebtDatalist.get(0));

        DebtData result = debtService.updateDebt(1L, new DebtForm());
        assertEquals(expectedDebtDatalist.get(0), result);
    }

    @Test
    public void updateDebtWithWrongIdShouldReturnResourceNotFoundException() {
        when(debtRepository.findById(any(Long.class))).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> debtService.updateDebt(1L, new DebtForm()));
    }

    @Test
    public void deleteDebtShouldCallCustomerRepositorySave() {
        Customer customer = new Customer();
        customer.setId(1L);
        customer.setDebts(debts);
        when(debtRepository.findById(any(Long.class))).thenReturn(Optional.of(debts.get(1)));
        when(customerRepository.save(any(Customer.class))).thenReturn(customer);
        debtService.deleteDebt(1L);
    }

    @Test
    public void deleteCustomerWithWrongIdShouldReturnResourceNotFoundException() {
        when(debtRepository.findById(any(Long.class))).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> debtService.deleteDebt(1L));
    }
}
