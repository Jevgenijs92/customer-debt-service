package com.example.customerdebtservice.debt;

import com.example.customerdebtservice.customer.forms.CustomerForm;
import com.example.customerdebtservice.debt.forms.DebtForm;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.jayway.jsonpath.JsonPath;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest
@WithMockUser(roles = "USER")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class DebtIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    private static final String DEBT_URL = "/debts";

    private static final JSONObject correctJsonDebtData = new JSONObject();
    private static final DebtForm debtForm = new DebtForm();
    private static final ObjectMapper mapper = new ObjectMapper();
    private static Long customerId = 1L;

    @BeforeAll
    public static void setup() {
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }

    @BeforeEach
    public void init() throws JSONException {
        String amount = "100.55";
        String currency = "EUR";
        String dueDate = "2022-02-15";

        debtForm.setAmount(new BigDecimal(amount));
        debtForm.setCurrency(currency);
        debtForm.setDueDate(LocalDate.of(2022, 2, 15));
        debtForm.setCustomerId(customerId);

//        correctJsonDebtData.put("id", 1);
        correctJsonDebtData.put("amount", amount);
        correctJsonDebtData.put("dueDate", dueDate);
        correctJsonDebtData.put("currency", currency);
        correctJsonDebtData.put("customerId", String.valueOf(customerId));
    }

    @Test
    @Order(1)
    public void createCustomerShouldReturnStatus201() throws Exception {
        CustomerForm customerForm = new CustomerForm();
        String name = "customer";
        String surname = "surname";
        String password = "password";
        String country = "country";
        String email = "email@email.com";
        customerForm.setName(name);
        customerForm.setSurname(surname);
        customerForm.setPassword(password);
        customerForm.setCountry(country);
        customerForm.setEmail(email);
        MvcResult result = mockMvc.perform(post("/customers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(customerForm)))
                .andDo(print())
                .andExpect(status().isCreated()).andReturn();
        String response = result.getResponse().getContentAsString();
        int id = JsonPath.parse(response).read("$.id");
        customerId = (long) id;
    }

    @Test
    @Order(2)
    public void getDebtsShouldReturnStatus200() throws Exception {
        mockMvc.perform(get(DEBT_URL))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @Order(3)
    public void getDebtsShouldReturnJSON() throws Exception {
        mockMvc.perform(get(DEBT_URL))
                .andDo(print())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    @Order(4)
    public void createDebtShouldReturnStatus201() throws Exception {
        MvcResult result = mockMvc.perform(post(DEBT_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(debtForm)))
                .andDo(print())
                .andExpect(status().isCreated()).andReturn();

        String response = result.getResponse().getContentAsString();
        int id = JsonPath.parse(response).read("$.id");
        correctJsonDebtData.put("id", id);
    }

    @Test
    @Order(5)
    public void createDebtWithEmptyAmountShouldReturnStatusBadRequest() throws Exception {
        debtForm.setAmount(null);
        mockMvc.perform(post(DEBT_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(debtForm)))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    @Order(6)
    public void createDebtWithNegativeAmountShouldReturnStatusBadRequest() throws Exception {
        debtForm.setAmount(new BigDecimal("-100"));
        mockMvc.perform(post(DEBT_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(debtForm)))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    @Order(7)
    public void createDebtWithEmptyCurrencyShouldReturnStatusBadRequest() throws Exception {
        debtForm.setCurrency(null);
        mockMvc.perform(post(DEBT_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(debtForm)))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    @Order(8)
    public void createDebtWithWrongCurrencyShouldReturnStatusBadRequest() throws Exception {
        debtForm.setCurrency("wrong");
        mockMvc.perform(post(DEBT_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(debtForm)))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    @Order(9)
    public void createDebtWithNonExistingCurrencyShouldReturnStatusBadRequest() throws Exception {
        debtForm.setCurrency("GBP");
        mockMvc.perform(post(DEBT_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(debtForm)))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    @Order(10)
    public void createDebtWithEmptyDueDateShouldReturnStatusBadRequest() throws Exception {
        debtForm.setDueDate(null);
        mockMvc.perform(post(DEBT_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(debtForm)))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    @Order(11)
    public void createDebtWithEmptyCustomerIdShouldReturnStatusBadRequest() throws Exception {
        debtForm.setCustomerId(null);
        mockMvc.perform(post(DEBT_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(debtForm)))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    @Order(12)
    public void createDebtWithNonExistingCustomerIdShouldReturnStatusBadRequest() throws Exception {
        debtForm.setCustomerId(9999L);
        mockMvc.perform(post(DEBT_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(debtForm)))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    @Order(13)
    public void updateExistingDebtShouldReturnStatus200() throws Exception {
        final long debtId = correctJsonDebtData.getLong("id");
        mockMvc.perform(put(DEBT_URL + "/" + debtId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(debtForm)))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @Order(14)
    public void updateNonExistingDebtShouldReturnStatusBadRequest() throws Exception {
        final long debtId = 99999L;
        mockMvc.perform(put(DEBT_URL + "/" + debtId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(debtForm)))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    @Order(15)
    public void updateDebtWithEmptyAmountShouldReturnStatusBadRequest() throws Exception {
        final long debtId = correctJsonDebtData.getLong("id");
        debtForm.setAmount(null);
        mockMvc.perform(put(DEBT_URL + "/" + debtId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(debtForm)))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    @Order(16)
    public void updateDebtWithNegativeAmountShouldReturnStatusBadRequest() throws Exception {
        final long debtId = correctJsonDebtData.getLong("id");
        debtForm.setAmount(new BigDecimal("-100"));
        mockMvc.perform(put(DEBT_URL + "/" + debtId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(debtForm)))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    @Order(17)
    public void updateDebtWithEmptyCurrencyShouldReturnStatusBadRequest() throws Exception {
        final long debtId = correctJsonDebtData.getLong("id");
        debtForm.setCurrency(null);
        mockMvc.perform(put(DEBT_URL + "/" + debtId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(debtForm)))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    @Order(18)
    public void updateDebtWithWrongCurrencyShouldReturnStatusBadRequest() throws Exception {
        final long debtId = correctJsonDebtData.getLong("id");
        debtForm.setCurrency("wrong");
        mockMvc.perform(put(DEBT_URL + "/" + debtId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(debtForm)))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    @Order(19)
    public void updateDebtWithNonExistingCurrencyShouldReturnStatusBadRequest() throws Exception {
        final long debtId = correctJsonDebtData.getLong("id");
        debtForm.setCurrency("GBP");
        mockMvc.perform(put(DEBT_URL + "/" + debtId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(debtForm)))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    @Order(20)
    public void updateDebtWithEmptyDueDateShouldReturnStatusBadRequest() throws Exception {
        final long debtId = correctJsonDebtData.getLong("id");
        debtForm.setDueDate(null);
        mockMvc.perform(put(DEBT_URL + "/" + debtId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(debtForm)))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    @Order(21)
    public void updateDebtWithEmptyCustomerIdShouldReturnStatusBadRequest() throws Exception {
        final long debtId = correctJsonDebtData.getLong("id");
        debtForm.setCustomerId(null);
        mockMvc.perform(put(DEBT_URL + "/" + debtId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(debtForm)))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    @Order(22)
    public void DeleteExistingDebtShouldReturnStatus200() throws Exception {
        final long debtId = correctJsonDebtData.getLong("id");
        mockMvc.perform(delete(DEBT_URL + "/" + debtId))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @Order(23)
    public void afterDeletingDebtGetCustomerReturnStatus200() throws Exception {
        final long customerId = correctJsonDebtData.getLong("id");
        mockMvc.perform(get("/customers/" + customerId))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @Order(24)
    public void DeleteNonExistingDebtShouldReturnStatusBadRequest() throws Exception {
        final long debtId = correctJsonDebtData.getLong("id");
        mockMvc.perform(delete(DEBT_URL + "/" + debtId))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    @Order(25)
    public void afterDeletingDebtGetCustomerReturnJson() throws Exception {
        final long customerId = correctJsonDebtData.getLong("id");
        mockMvc.perform(get("/customers/" + customerId))
                .andDo(print())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

}
