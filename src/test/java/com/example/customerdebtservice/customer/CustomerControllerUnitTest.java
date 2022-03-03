package com.example.customerdebtservice.customer;

import com.example.customerdebtservice.customer.controllers.CustomerController;
import com.example.customerdebtservice.customer.dto.CustomerData;
import com.example.customerdebtservice.customer.forms.CustomerForm;
import com.example.customerdebtservice.customer.services.CustomerService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(CustomerController.class)
@WithMockUser(roles = "USER")
public class CustomerControllerUnitTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CustomerService customerService;

    private static final String CUSTOMERS_URL = "/customers";
    private static final ObjectMapper mapper = new ObjectMapper();
    private final CustomerForm customerForm = new CustomerForm();
    private final CustomerData customerData = new CustomerData();


    @BeforeEach
    public void init() {
        customerForm.setName("Name");
        customerForm.setSurname("Surname");
        customerForm.setEmail("email@email.com");
        customerForm.setCountry("Latvia");
        customerForm.setPassword("password");

        customerData.setId(1L);
        customerData.setName("Name");
        customerData.setSurname("Surname");
        customerData.setEmail("email@email.com");
        customerData.setCountry("Latvia");
        customerData.setDebts(new ArrayList<>());
    }


    @Test
    public void getCustomerShouldReturnCustomerDataList() throws Exception {
        List<CustomerData> customerDataList = new ArrayList<>();
        customerDataList.add(customerData);

        when(customerService.getCustomers(any())).thenReturn(customerDataList);

        mockMvc.perform(get(CUSTOMERS_URL))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(customerDataList)));
    }

    @Test
    public void getCustomersWithoutParametersShouldReturnDefaultPageParameters() throws Exception {
        final int pageNumber = 0;
        final int pageSize = 20;
        mockMvc.perform(get(CUSTOMERS_URL))
                .andExpect(status().isOk());

        ArgumentCaptor<Pageable> pageableCaptor = ArgumentCaptor.forClass(Pageable.class);
        verify(customerService).getCustomers(pageableCaptor.capture());
        PageRequest pageable = (PageRequest) pageableCaptor.getValue();

        assertEquals(pageNumber, pageable.getPageNumber(),
                "Default page number " + pageNumber + " doesn't equal to actual page number " + pageable.getPageNumber());
        assertEquals(pageSize, pageable.getPageSize(),
                "Default page size " + pageSize + " doesn't equal to actual page size " + pageable.getPageSize());
    }

    @ParameterizedTest
    @ValueSource(ints = {0, 1, 2, 5, 10})
    public void getProductsWithPagesShouldReturnCorrectPageNumber(int pageNumber) throws Exception {
        final int pageSize = 10;
        mockMvc.perform(get(CUSTOMERS_URL)
                        .param("page", String.valueOf(pageNumber))
                        .param("size", String.valueOf(pageSize)))
                .andExpect(status().isOk());

        ArgumentCaptor<Pageable> pageableCaptor = ArgumentCaptor.forClass(Pageable.class);
        verify(customerService).getCustomers(pageableCaptor.capture());
        PageRequest pageable = (PageRequest) pageableCaptor.getValue();

        assertEquals(pageNumber, pageable.getPageNumber(),
                "Requested page number " + pageNumber + " doesn't equal to actual page number " + pageable.getPageNumber());
        assertEquals(pageSize, pageable.getPageSize(),
                "Requested page size " + pageSize + " doesn't equal to actual page size " + pageable.getPageSize());
    }

    @ParameterizedTest
    @ValueSource(ints = {0, 1, 2, 5, 10})
    public void getCustomersWithPagesShouldReturnCorrectPageSize(int pageSize) throws Exception {
        final int pageNumber = 1;
        mockMvc.perform(get(CUSTOMERS_URL)
                        .param("page", String.valueOf(pageNumber))
                        .param("size", String.valueOf(pageSize)))
                .andExpect(status().isOk());

        ArgumentCaptor<Pageable> pageableCaptor = ArgumentCaptor.forClass(Pageable.class);
        verify(customerService).getCustomers(pageableCaptor.capture());
        PageRequest pageable = (PageRequest) pageableCaptor.getValue();

        assertEquals(pageNumber, pageable.getPageNumber(),
                "Requested page number " + pageNumber + " doesn't equal to actual page number " + pageable.getPageNumber());
        assertEquals(pageSize == 0 ? 20 : pageSize, pageable.getPageSize(),
                "Requested page size " + pageSize + " doesn't equal to actual page size " + pageable.getPageSize());
    }

    @Test
    public void getCustomersWithPagesAndSortShouldHaveCorrectSortParameter() throws Exception {
        final int pageSize = 20;
        final int pageNumber = 0;
        final String sortDirection = "desc";
        String sortBy = "id";
        mockMvc.perform(get(CUSTOMERS_URL)
                        .param("sort", sortBy + "," + sortDirection))
                .andExpect(status().isOk());

        ArgumentCaptor<Pageable> pageableCaptor = ArgumentCaptor.forClass(Pageable.class);
        verify(customerService).getCustomers(pageableCaptor.capture());
        PageRequest pageable = (PageRequest) pageableCaptor.getValue();
        System.out.println(pageable);
        assertEquals(PageRequest.of(pageNumber, pageSize, Sort.by(sortBy).descending()), pageable);
    }

    @Test
    public void getCustomerByIdShouldReturnCustomerData() throws Exception {
        final Long customerId = 1L;

        when(customerService.getCustomerById(customerId)).thenReturn(customerData);

        mockMvc.perform(get(CUSTOMERS_URL + "/" + customerId)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(customerData)));
    }

    @Test
    public void createCustomerShouldReturnCreatedCustomerData() throws Exception {
        when(customerService.createCustomer(any(CustomerForm.class))).thenReturn(customerData);

        mockMvc.perform(post(CUSTOMERS_URL)
                        .content(mapper.writeValueAsString(customerForm))
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isCreated())
                .andExpect(content().json(mapper.writeValueAsString(customerData)));
    }

    @Test
    public void createCustomerWithEmptyNameShouldReturnBadRequest() throws Exception {
        customerForm.setName(null);
        mockMvc.perform(post(CUSTOMERS_URL)
                        .content(mapper.writeValueAsString(customerForm))
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void createCustomerWithEmptySurnameShouldReturnBadRequest() throws Exception {
        customerForm.setSurname(null);
        mockMvc.perform(post(CUSTOMERS_URL)
                        .content(mapper.writeValueAsString(customerForm))
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void createCustomerWithEmptyCountryShouldReturnBadRequest() throws Exception {
        customerForm.setCountry(null);
        mockMvc.perform(post(CUSTOMERS_URL)
                        .content(mapper.writeValueAsString(customerForm))
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void createCustomerWithEmptyPasswordShouldReturnBadRequest() throws Exception {
        customerForm.setPassword(null);
        mockMvc.perform(post(CUSTOMERS_URL)
                        .content(mapper.writeValueAsString(customerForm))
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void createCustomerWithEmptyEmailShouldReturnBadRequest() throws Exception {
        customerForm.setEmail(null);
        mockMvc.perform(post(CUSTOMERS_URL)
                        .content(mapper.writeValueAsString(customerForm))
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isBadRequest());
    }

    @ParameterizedTest
    @ValueSource(strings = {"wrongemail", "wrong@email", "wrong@email.c"})
    public void createCustomerWithWrongEmailShouldReturnBadRequest(String email) throws Exception {
        customerForm.setEmail(email);
        mockMvc.perform(post(CUSTOMERS_URL)
                        .content(mapper.writeValueAsString(customerForm))
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void updateCustomerShouldReturnStatus200() throws Exception {
        final Long customerId = 1L;
        CustomerForm customerBeforeUpdate = new CustomerForm();
        when(customerService.updateCustomer(customerId, customerBeforeUpdate)).thenReturn(customerData);

        mockMvc.perform(put(CUSTOMERS_URL + "/" + customerId)
                        .content(mapper.writeValueAsString(customerForm))
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk());
    }

    @Test
    public void deleteCustomerShouldReturnStatus200() throws Exception {
        final long customerId = 1L;
        mockMvc.perform(delete(CUSTOMERS_URL + "/" + customerId))
                .andExpect(status().isOk());
    }
}
