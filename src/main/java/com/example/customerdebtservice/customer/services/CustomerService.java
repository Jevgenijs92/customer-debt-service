package com.example.customerdebtservice.customer.services;

import com.example.customerdebtservice.customer.dto.CustomerData;
import com.example.customerdebtservice.customer.forms.CustomerForm;
import com.example.customerdebtservice.shared.exceptions.ResourceExistsException;
import com.example.customerdebtservice.shared.exceptions.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Pageable;

import java.util.List;

@Service
public interface CustomerService {
    /**
     * Retrieves customers from database in a pageable form (page number, page size, sort)
     *
     * @param pageable contains Customer and Pageable data
     * @return list of specific page number and page size sorted Customer Data
     */
    List<CustomerData> getCustomers(Pageable pageable);

    /**
     * Retrieves Customer from database and returns Customer Data
     * or throws Customer Not Found Exception if customer doesn't exist
     *
     * @param id - customer ID
     * @return - Customer Data object
     * @throws ResourceNotFoundException when customer is not found in the database
     */
    CustomerData getCustomerById(Long id) throws ResourceNotFoundException;

    /**
     * Creates a new Customer from Customer Form
     *
     * @param customerForm - contains all customer fields
     * @return created Customer Data
     * @throws ResourceExistsException when Customer already exists (e.g. such email exists in system)
     */
    CustomerData createCustomer(CustomerForm customerForm) throws ResourceExistsException;

    /**
     * Updates customer by replacing all existing attributes with the new attributes
     * ONLY IF the passed attribute is not null.
     * In other case, e.g., if attribute is null then it will not be overwritten.
     *
     * @param id           - Customer ID
     * @param customerForm - containing customer attributes
     * @return updated Customer Data
     * @throws ResourceNotFoundException when customer is not found in the database
     */
    CustomerData updateCustomer(Long id, CustomerForm customerForm) throws ResourceNotFoundException;

    /**
     * Deletes customer from database
     *
     * @param id - Customer ID
     * @throws ResourceNotFoundException when customer is not found in the database
     */
    void deleteProduct(Long id) throws ResourceNotFoundException;
}
