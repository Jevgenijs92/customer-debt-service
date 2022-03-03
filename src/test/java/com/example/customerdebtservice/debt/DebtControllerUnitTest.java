package com.example.customerdebtservice.debt;

import com.example.customerdebtservice.currency.dto.CurrencyData;
import com.example.customerdebtservice.debt.controllers.DebtController;
import com.example.customerdebtservice.debt.dto.DebtData;
import com.example.customerdebtservice.debt.forms.DebtForm;
import com.example.customerdebtservice.debt.services.DebtService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
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

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(DebtController.class)
@WithMockUser(roles = "USER")
public class DebtControllerUnitTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private DebtService debtService;

    private static final String DEBTS_URL = "/debts";
    private static final ObjectMapper mapper = new ObjectMapper();
    private final DebtForm debtForm = new DebtForm();
    private final DebtData debtData = new DebtData();

    @BeforeEach
    public void init() {
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        debtForm.setAmount(new BigDecimal("100"));
        debtForm.setCurrency("USD");
        debtForm.setCustomerId(1L);
        debtForm.setDueDate(LocalDate.of(2022, 3, 2));


        debtData.setId(1L);
        debtData.setAmount(new BigDecimal("100"));
        debtData.setCurrency(new CurrencyData(1L, "Dollar", "USD", "$"));
        debtData.setCustomerId(1L);
        debtData.setDueDate(LocalDate.of(2022, 3, 2));
    }


    @Test
    public void getDebtsShouldReturnDebtsDataList() throws Exception {
        List<DebtData> debtDataList = new ArrayList<>();
        debtDataList.add(debtData);

        when(debtService.getDebts(any())).thenReturn(debtDataList);

        mockMvc.perform(get(DEBTS_URL))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(debtDataList)));
    }

    @Test
    public void getDebtsWithoutParametersShouldReturnDefaultPageParameters() throws Exception {
        final int pageNumber = 0;
        final int pageSize = 20;
        mockMvc.perform(get(DEBTS_URL))
                .andExpect(status().isOk());

        ArgumentCaptor<Pageable> pageableCaptor = ArgumentCaptor.forClass(Pageable.class);
        verify(debtService).getDebts(pageableCaptor.capture());
        PageRequest pageable = (PageRequest) pageableCaptor.getValue();

        assertEquals(pageNumber, pageable.getPageNumber(),
                "Default page number " + pageNumber + " doesn't equal to actual page number " + pageable.getPageNumber());
        assertEquals(pageSize, pageable.getPageSize(),
                "Default page size " + pageSize + " doesn't equal to actual page size " + pageable.getPageSize());
    }

    @ParameterizedTest
    @ValueSource(ints = {0, 1, 2, 5, 10})
    public void getDebtsWithPagesShouldReturnCorrectPageNumber(int pageNumber) throws Exception {
        final int pageSize = 10;
        mockMvc.perform(get(DEBTS_URL)
                        .param("page", String.valueOf(pageNumber))
                        .param("size", String.valueOf(pageSize)))
                .andExpect(status().isOk());

        ArgumentCaptor<Pageable> pageableCaptor = ArgumentCaptor.forClass(Pageable.class);
        verify(debtService).getDebts(pageableCaptor.capture());
        PageRequest pageable = (PageRequest) pageableCaptor.getValue();

        assertEquals(pageNumber, pageable.getPageNumber(),
                "Requested page number " + pageNumber + " doesn't equal to actual page number " + pageable.getPageNumber());
        assertEquals(pageSize, pageable.getPageSize(),
                "Requested page size " + pageSize + " doesn't equal to actual page size " + pageable.getPageSize());
    }

    @ParameterizedTest
    @ValueSource(ints = {0, 1, 2, 5, 10})
    public void getDebtsWithPagesShouldReturnCorrectPageSize(int pageSize) throws Exception {
        final int pageNumber = 1;
        mockMvc.perform(get(DEBTS_URL)
                        .param("page", String.valueOf(pageNumber))
                        .param("size", String.valueOf(pageSize)))
                .andExpect(status().isOk());

        ArgumentCaptor<Pageable> pageableCaptor = ArgumentCaptor.forClass(Pageable.class);
        verify(debtService).getDebts(pageableCaptor.capture());
        PageRequest pageable = (PageRequest) pageableCaptor.getValue();

        assertEquals(pageNumber, pageable.getPageNumber(),
                "Requested page number " + pageNumber + " doesn't equal to actual page number " + pageable.getPageNumber());
        assertEquals(pageSize == 0 ? 20 : pageSize, pageable.getPageSize(),
                "Requested page size " + pageSize + " doesn't equal to actual page size " + pageable.getPageSize());
    }

    @Test
    public void getDebtsWithPagesAndSortShouldHaveCorrectSortParameter() throws Exception {
        final int pageSize = 20;
        final int pageNumber = 0;
        final String sortDirection = "desc";
        String sortBy = "id";
        mockMvc.perform(get(DEBTS_URL)
                        .param("sort", sortBy + "," + sortDirection))
                .andExpect(status().isOk());

        ArgumentCaptor<Pageable> pageableCaptor = ArgumentCaptor.forClass(Pageable.class);
        verify(debtService).getDebts(pageableCaptor.capture());
        PageRequest pageable = (PageRequest) pageableCaptor.getValue();
        System.out.println(pageable);
        assertEquals(PageRequest.of(pageNumber, pageSize, Sort.by(sortBy).descending()), pageable);
    }

    @Test
    public void getDebtsByIdShouldReturnDebtsData() throws Exception {
        final Long debtsId = 1L;

        when(debtService.getDebtById(debtsId)).thenReturn(debtData);

        mockMvc.perform(get(DEBTS_URL + "/" + debtsId)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(debtData)));
    }

    @Test
    public void createDebtShouldReturnCreatedDebtData() throws Exception {
        when(debtService.createDebt(any(DebtForm.class))).thenReturn(debtData);

        mockMvc.perform(post(DEBTS_URL)
                        .content(mapper.writeValueAsString(debtForm))
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isCreated())
                .andExpect(content().json(mapper.writeValueAsString(debtData)));
    }

    @Test
    public void createDebtWithEmptyAmountShouldReturnBadRequest() throws Exception {
        debtForm.setAmount(null);
        mockMvc.perform(post(DEBTS_URL)
                        .content(mapper.writeValueAsString(debtForm))
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void createDebtWithNegativeAmountShouldReturnBadRequest() throws Exception {
        debtForm.setAmount(new BigDecimal("-20"));
        mockMvc.perform(post(DEBTS_URL)
                        .content(mapper.writeValueAsString(debtForm))
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void createDebtWithEmptyCurrencyShouldReturnBadRequest() throws Exception {
        debtForm.setCurrency(null);
        mockMvc.perform(post(DEBTS_URL)
                        .content(mapper.writeValueAsString(debtForm))
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void createDebtWithWrongCurrencyShouldReturnBadRequest() throws Exception {
        debtForm.setCurrency("wrong");
        mockMvc.perform(post(DEBTS_URL)
                        .content(mapper.writeValueAsString(debtForm))
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void createDebtWithEmptyDueDateShouldReturnBadRequest() throws Exception {
        debtForm.setDueDate(null);
        mockMvc.perform(post(DEBTS_URL)
                        .content(mapper.writeValueAsString(debtForm))
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void createDebtWithEmptyCustomerIdShouldReturnBadRequest() throws Exception {
        debtForm.setCustomerId(null);
        mockMvc.perform(post(DEBTS_URL)
                        .content(mapper.writeValueAsString(debtForm))
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void updateDebtShouldReturnStatus200() throws Exception {
        final Long debtId = 1L;
        when(debtService.updateDebt(debtId, debtForm)).thenReturn(debtData);

        mockMvc.perform(put(DEBTS_URL + "/" + debtId)
                        .content(mapper.writeValueAsString(debtForm))
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk());
    }

    @Test
    public void deleteDebtShouldReturnStatus200() throws Exception {
        final long debtId = 1L;
        mockMvc.perform(delete(DEBTS_URL + "/" + debtId))
                .andExpect(status().isOk());
    }
}
