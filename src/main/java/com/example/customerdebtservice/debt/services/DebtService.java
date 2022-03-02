package com.example.customerdebtservice.debt.services;

import com.example.customerdebtservice.debt.dto.DebtData;
import com.example.customerdebtservice.debt.forms.DebtForm;
import com.example.customerdebtservice.shared.exceptions.ResourceNotFoundException;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface DebtService {
    /**
     * Retrieves debts from database in a pageable form
     *
     * @param pageable contains Pageable data
     * @return list of specific page number and page size sorted Debts
     */
    List<DebtData> getDebts(Pageable pageable);

    /**
     * Retrieves debt by ID
     *
     * @param id of debt
     * @return debt data
     * @throws ResourceNotFoundException when debt doesn't exist
     */
    DebtData getDebtById(Long id) throws ResourceNotFoundException;

    /**
     * Creates a new debt for existing customer
     *
     * @param debtForm   contains debt attributes
     * @return created debt data
     */
    DebtData createDebt(DebtForm debtForm);

    /**
     * Updates debt values.
     *
     * @param id       of debt
     * @param debtForm contains debt attributes
     * @return updated debt data
     * @throws ResourceNotFoundException when debt with passed ID doesn't exist
     */
    DebtData updateDebt(Long id, DebtForm debtForm) throws ResourceNotFoundException;

    /**
     * Deletes the debt from the database
     *
     * @param id of debt
     * @throws ResourceNotFoundException when debt with passed ID doesn't exist
     */
    void deleteDebt(Long id) throws ResourceNotFoundException;
}
