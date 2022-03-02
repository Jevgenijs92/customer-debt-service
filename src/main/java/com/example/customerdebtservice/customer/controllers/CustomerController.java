package com.example.customerdebtservice.customer.controllers;

import com.example.customerdebtservice.customer.dto.CustomerData;
import com.example.customerdebtservice.customer.forms.CustomerForm;
import com.example.customerdebtservice.customer.services.CustomerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/customers")
@RequiredArgsConstructor
@Validated
@Slf4j
public class CustomerController {

    private final CustomerService customerService;
    private static final String URL_PATH = "/customers";

    @GetMapping
    public ResponseEntity<List<CustomerData>> getCustomers(@PageableDefault(size = 20) Pageable pageable) {
        log.info("GET request: " + URL_PATH);
        return ResponseEntity.ok().body(customerService.getCustomers(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CustomerData> getCustomer(@PathVariable Long id) {
        log.info("GET request: " + URL_PATH + "/" + id);
        return ResponseEntity.ok().body(customerService.getCustomerById(id));
    }

    @PostMapping
    public ResponseEntity<CustomerData> createCustomer(@Valid @RequestBody CustomerForm customerForm,
                                                       BindingResult bindingResult) {
        log.info("POST request: " + URL_PATH);
        final URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path(URL_PATH).toUriString());
        return ResponseEntity.created(uri).body(customerService.createCustomer(customerForm));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CustomerData> updateCustomer(@PathVariable Long id,
                                                       @Valid @RequestBody CustomerForm customerForm,
                                                       BindingResult bindingResult) {
        log.info("PUT request: " + URL_PATH + "/" + id);
        return ResponseEntity.ok().body(customerService.updateCustomer(id, customerForm));
    }

    @DeleteMapping("{id}")
    public ResponseEntity<?> deleteCustomer(@PathVariable Long id) {
        log.info("DELETE request: " + URL_PATH + "/" + id);
        customerService.deleteCustomer(id);
        return ResponseEntity.ok().build();
    }
}
