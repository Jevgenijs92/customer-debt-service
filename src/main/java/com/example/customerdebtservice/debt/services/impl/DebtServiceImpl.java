package com.example.customerdebtservice.debt.services.impl;

import com.example.customerdebtservice.currency.services.CurrencyService;
import com.example.customerdebtservice.customer.services.CustomerService;
import com.example.customerdebtservice.debt.converters.DebtConverter;
import com.example.customerdebtservice.debt.dto.DebtData;
import com.example.customerdebtservice.debt.forms.DebtForm;
import com.example.customerdebtservice.debt.models.Debt;
import com.example.customerdebtservice.debt.repositories.DebtRepository;
import com.example.customerdebtservice.debt.services.DebtService;
import com.example.customerdebtservice.shared.exceptions.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class DebtServiceImpl implements DebtService {

    private final DebtRepository debtRepository;
    private final DebtConverter debtConverter;
    private final CustomerService customerService;
    private final CurrencyService currencyService;

    @Override
    public List<DebtData> getDebts(Pageable pageable) {
        log.info("Retrieving all debts");
        return debtRepository.findAll(pageable).toList().stream().map(debtConverter::convert)
                .collect(Collectors.toList());
    }

    @Override
    public DebtData getDebtById(Long id) throws ResourceNotFoundException {
        log.info("Retrieving debt by ID: " + id);
        return debtConverter.convert(findDebtByIdOrThrow(id));
    }

    @Override
    public DebtData createDebt(DebtForm debtForm) {
        log.info("Creating new debt for customer ID: " + debtForm.getCustomerId());
        Debt debt = new Debt();
        populateDebtFormToDebt(debtForm, debt);
        return debtConverter.convert(debtRepository.save(debt));
    }

    @Override
    public DebtData updateDebt(Long id, DebtForm debtForm) throws ResourceNotFoundException {
        log.info("Updating debt with ID: " + id);
        Debt debt = findDebtByIdOrThrow(id);
        populateDebtFormToDebt(debtForm, debt);
        return debtConverter.convert(debtRepository.save(debt));
    }

    @Override
    public void deleteDebt(Long id) throws ResourceNotFoundException {
        log.info("Deleting debt with ID: " + id);
        Debt debt = findDebtByIdOrThrow(id);
        debt.setCustomer(null);
        debtRepository.deleteById(id);
    }

    private Debt findDebtByIdOrThrow(Long id) {
        return debtRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Debt not found. ID: " + id));
    }

    private void populateDebtFormToDebt(DebtForm source, Debt target) {
        target.setAmount(source.getAmount());
        target.setCurrency(currencyService.getCurrencyByCode(source.getCurrency()));
        target.setDueDate(source.getDueDate());
        target.setCustomer(customerService.findCustomerByIdOrThrow(source.getCustomerId()));
    }
}
