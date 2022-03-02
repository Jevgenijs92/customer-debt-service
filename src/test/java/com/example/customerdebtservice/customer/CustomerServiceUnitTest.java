package com.example.customerdebtservice.customer;

import com.example.customerdebtservice.customer.converters.CustomerConverter;
import com.example.customerdebtservice.customer.dto.CustomerData;
import com.example.customerdebtservice.customer.forms.CustomerForm;
import com.example.customerdebtservice.customer.models.Customer;
import com.example.customerdebtservice.customer.repositories.CustomerRepository;
import com.example.customerdebtservice.customer.services.impl.CustomerServiceImpl;
import com.example.customerdebtservice.shared.exceptions.ResourceExistsException;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CustomerServiceUnitTest {
    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private CustomerConverter customerConverter;

    @InjectMocks
    private CustomerServiceImpl customerService;

    private final static List<Customer> customers = new ArrayList<>();
    private final static List<CustomerData> expectedCustomerDataList = new ArrayList<>();

    @BeforeAll
    public static void setup() {
        for(long i = 1; i <= 20; i++) {
            Customer customer = new Customer();
            customer.setId(i);
            customer.setName("Customer " + i);
            customer.setSurname("Surname " + i);
            customer.setCountry("Country " + i);
            customer.setPassword("password"+i);
            customer.setDebts(new ArrayList<>());
            customers.add(customer);

            CustomerData customerData = new CustomerData();
            customerData.setId(i);
            customerData.setName("Customer " + i);
            customerData.setSurname("Surname " + i);
            customerData.setCountry("Country " + i);
            customerData.setDebts(new ArrayList<>());
            expectedCustomerDataList.add(customerData);
        }
    }

    @Test
    public void getAllCustomersShouldReturnCustomerDataList() {
        Page<Customer> customerPage = new PageImpl<>(customers);
        Pageable pageable = PageRequest.of(0, 20);
        when(customerRepository.findAll(pageable)).thenReturn(customerPage);
        when(customerConverter.convert(any(Customer.class))).thenReturn(expectedCustomerDataList.get(0));

        List<CustomerData> result = customerService.getCustomers(pageable);
        assertEquals(result.size(), customers.size(),
                "Size of initial list of customers doesn't equal to the list returned by service");

    }

    @Test
    public void getCustomerByIdShouldReturnCustomer() {
        when(customerRepository.findById(any(Long.class))).thenReturn(Optional.ofNullable(customers.get(0)));
        when(customerConverter.convert(any(Customer.class))).thenReturn(expectedCustomerDataList.get(0));

        CustomerData result = customerService.getCustomerById(1L);
        assertEquals(result.getId(), expectedCustomerDataList.get(0).getId());
        assertEquals(result.getName(), expectedCustomerDataList.get(0).getName());
        assertEquals(result.getSurname(), expectedCustomerDataList.get(0).getSurname());
        assertEquals(result.getCountry(), expectedCustomerDataList.get(0).getCountry());
    }

    @Test
    public void getCustomerByIdWithWrongIdShouldReturnResourceNotFoundException() {
        when(customerRepository.findById(any(Long.class))).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> customerService.getCustomerById(1L));
    }

    @Test
    public void createCustomerShouldReturnNewCreatedCustomerData() {
        when(customerRepository.findByEmail(any())).thenReturn(Optional.empty());
        when(customerRepository.save(any(Customer.class))).thenReturn(customers.get(0));
        when(customerConverter.convert(any(Customer.class))).thenReturn(expectedCustomerDataList.get(0));

        CustomerData result = customerService.createCustomer(new CustomerForm());
        assertEquals(expectedCustomerDataList.get(0), result);
    }

    @Test
    public void createCustomerWithExistingEmailShouldReturnResourceExistsException() {
        when(customerRepository.findByEmail(any())).thenReturn(Optional.of(new Customer()));
        assertThrows(ResourceExistsException.class, () -> customerService.createCustomer(new CustomerForm()));
    }

    @Test
    public void updateCustomerShouldReturnUpdatedCustomerData() {
        when(customerRepository.findById(any(Long.class))).thenReturn(Optional.of(new Customer()));
        when(customerRepository.save(any(Customer.class))).thenReturn(customers.get(0));
        when(customerConverter.convert(any(Customer.class))).thenReturn(expectedCustomerDataList.get(0));

        CustomerData result = customerService.updateCustomer(1L, new CustomerForm());
        assertEquals(expectedCustomerDataList.get(0), result);
    }

    @Test
    public void updateCustomerWithWrongIdShouldReturnResourceNotFoundException() {
        when(customerRepository.findById(any(Long.class))).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> customerService.updateCustomer(1L, new CustomerForm()));
    }

    @Test
    public void deleteCustomerShouldCallRepositoryDeleteById() {
        when(customerRepository.findById(any(Long.class))).thenReturn(Optional.of(new Customer()));
        customerService.deleteCustomer(1L);
        verify(customerRepository).deleteById(any());
    }

    @Test
    public void deleteCustomerWithWrongIdShouldReturnResourceNotFoundException() {
        when(customerRepository.findById(any(Long.class))).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> customerService.deleteCustomer(1L));
    }
}
