package com.example.customerdebtservice.customer.services.impl;

import com.example.customerdebtservice.customer.converters.CustomerConverter;
import com.example.customerdebtservice.customer.dto.CustomerData;
import com.example.customerdebtservice.customer.forms.CustomerForm;
import com.example.customerdebtservice.customer.models.Customer;
import com.example.customerdebtservice.customer.repositories.CustomerRepository;
import com.example.customerdebtservice.customer.services.CustomerService;
import com.example.customerdebtservice.shared.exceptions.ResourceExistsException;
import com.example.customerdebtservice.shared.exceptions.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;
    private final CustomerConverter customerConverter;
    private final PasswordEncoder passwordEncoder;

    @Override
    public List<CustomerData> getCustomers(Pageable pageable) {
        log.info("Retrieving all customers");
        return customerRepository.findAll(pageable).toList().stream().map(customerConverter::convert)
                .collect(Collectors.toList());
    }

    @Override
    public CustomerData getCustomerById(Long id) throws ResourceNotFoundException {
        log.info("Retrieving customer with ID: " + id);
        return customerConverter.convert(findCustomerByIdOrThrow(id));
    }

    @Override
    public CustomerData createCustomer(CustomerForm customerForm) throws ResourceExistsException {
        log.info("Creating new customer with email: " + customerForm.getEmail());
        if (customerRepository.findByEmail(customerForm.getEmail()).isPresent()) {
            throw new ResourceExistsException("Customer with email " + customerForm.getEmail() + " already exists");
        }

        final Customer customer = new Customer();
        populateCustomerFormToCustomer(customerForm, customer);
        return customerConverter.convert(customerRepository.save(customer));
    }

    @Override
    public CustomerData updateCustomer(Long id, CustomerForm customerForm) throws ResourceNotFoundException {
        log.info("Updating customer with ID: " + id);
        Customer customer = findCustomerByIdOrThrow(id);
        populateCustomerFormToCustomer(customerForm, customer);
        return customerConverter.convert(customerRepository.save(customer));
    }

    @Override
    public void deleteCustomer(Long id) {
        log.info("Deleting customer with ID: " + id);
        findCustomerByIdOrThrow(id);
        customerRepository.deleteById(id);
    }

    @Override
    public Customer findCustomerByIdOrThrow(Long id) {
        return customerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found. ID: " + id));
    }

    private void populateCustomerFormToCustomer(final CustomerForm source, final Customer target) {
        if (Objects.nonNull(source) && Objects.nonNull(target)) {
            target.setName(source.getName());
            target.setSurname(source.getSurname());
            target.setCountry(source.getCountry());
            target.setEmail(source.getEmail());
            target.setPassword(passwordEncoder.encode(source.getPassword()));
        }
    }
}
