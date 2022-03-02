package com.example.customerdebtservice.customer;

import com.example.customerdebtservice.customer.forms.CustomerForm;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc
@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class CustomerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    private static final String CUSTOMERS_URL = "/customers";

    private static final JSONObject correctJsonCustomerData = new JSONObject();
    private static final CustomerForm customerForm = new CustomerForm();
    private static final ObjectMapper mapper = new ObjectMapper();

    @BeforeEach
    public void init() throws JSONException {
        String name = "customer";
        String surname = "surname";
        String password = "password";
        String country = "country";
        String email = "random@test.com";

        customerForm.setName(name);
        customerForm.setSurname(surname);
        customerForm.setPassword(password);
        customerForm.setCountry(country);
        customerForm.setEmail(email);

//        correctJsonCustomerData.put("id", 1);
        correctJsonCustomerData.put("name", name);
        correctJsonCustomerData.put("surname", surname);
        correctJsonCustomerData.put("country", country);
        correctJsonCustomerData.put("email", email);
    }


    @Test
    @Order(1)
    public void getCustomersShouldReturnStatus200() throws Exception {
        mockMvc.perform(get(CUSTOMERS_URL))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @Order(2)
    public void getCustomersShouldReturnJSON() throws Exception {
        mockMvc.perform(get(CUSTOMERS_URL))
                .andDo(print())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    @Order(3)
    public void createCustomerShouldReturnStatus201() throws Exception {
        MvcResult result = mockMvc.perform(post(CUSTOMERS_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(customerForm)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(content().json(String.valueOf(correctJsonCustomerData))).andReturn();
        String response = result.getResponse().getContentAsString();
        Integer id = JsonPath.parse(response).read("$.id");
        correctJsonCustomerData.put("id", id);
    }

    @Test
    @Order(4)
    public void createCustomerWithEmptyNameShouldReturnStatusBadRequest() throws Exception {
        customerForm.setName(null);
        mockMvc.perform(post(CUSTOMERS_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(customerForm)))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    @Order(5)
    public void createCustomerWithEmptySurnameShouldReturnStatusBadRequest() throws Exception {
        customerForm.setSurname(null);
        mockMvc.perform(post(CUSTOMERS_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(customerForm)))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    @Order(6)
    public void createCustomerWithEmptyCountryShouldReturnStatusBadRequest() throws Exception {
        customerForm.setCountry(null);
        mockMvc.perform(post(CUSTOMERS_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(customerForm)))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    @Order(7)
    public void createCustomerWithEmptyPasswordShouldReturnStatusBadRequest() throws Exception {
        customerForm.setPassword(null);
        mockMvc.perform(post(CUSTOMERS_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(customerForm)))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    @Order(8)
    public void createCustomerWithEmptyEmailShouldReturnStatusBadRequest() throws Exception {
        customerForm.setEmail(null);
        mockMvc.perform(post(CUSTOMERS_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(customerForm)))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @ParameterizedTest
    @ValueSource(strings = {"wrongemail", "wrong@email", "wrong@email.c"})
    @Order(9)
    public void createCustomerWithWrongEmailShouldReturnStatusBadRequest() throws Exception {
        customerForm.setEmail(null);
        mockMvc.perform(post(CUSTOMERS_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(customerForm)))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    @Order(10)
    public void createExistingCustomerShouldReturnStatus409() throws Exception {
        mockMvc.perform(post(CUSTOMERS_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(customerForm)))
                .andDo(print())
                .andExpect(status().isConflict());
    }

    @Test
    @Order(11)
    public void updateExistingCustomerShouldReturnStatus200() throws Exception {
        final long customerId = correctJsonCustomerData.getLong("id");
        mockMvc.perform(put(CUSTOMERS_URL + "/" + customerId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(customerForm)))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @Order(12)
    public void updateNonExistingCustomerShouldReturnStatusBadRequest() throws Exception {
        final long customerId = 99999L;
        mockMvc.perform(put(CUSTOMERS_URL + "/" + customerId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(customerForm)))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    @Order(13)
    public void updateExistingCustomerWithoutNameShouldReturnStatusBadRequest() throws Exception {
        final long customerId = correctJsonCustomerData.getLong("id");
        customerForm.setName(null);
        mockMvc.perform(put(CUSTOMERS_URL + "/" + customerId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(customerForm)))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    @Order(14)
    public void updateExistingCustomerWithoutSurnameShouldReturnStatusBadRequest() throws Exception {
        final long customerId = correctJsonCustomerData.getLong("id");
        customerForm.setSurname(null);
        mockMvc.perform(put(CUSTOMERS_URL + "/" + customerId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(customerForm)))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    @Order(15)
    public void updateExistingCustomerWithoutCountryShouldReturnStatusBadRequest() throws Exception {
        final long customerId = correctJsonCustomerData.getLong("id");
        customerForm.setCountry(null);
        mockMvc.perform(put(CUSTOMERS_URL + "/" + customerId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(customerForm)))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    @Order(16)
    public void updateExistingCustomerWithoutPasswordShouldReturnStatusBadRequest() throws Exception {
        final long customerId = correctJsonCustomerData.getLong("id");
        customerForm.setPassword(null);
        mockMvc.perform(put(CUSTOMERS_URL + "/" + customerId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(customerForm)))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    @Order(17)
    public void updateExistingCustomerWithoutEmailShouldReturnStatusBadRequest() throws Exception {
        final long customerId = correctJsonCustomerData.getLong("id");
        customerForm.setEmail(null);
        mockMvc.perform(put(CUSTOMERS_URL + "/" + customerId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(customerForm)))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @ParameterizedTest
    @ValueSource(strings = {"wrongemail", "wrong@email", "wrong@email.c"})
    @Order(18)
    public void updateExistingCustomerWithWrongEmailShouldReturnStatusBadRequest(String email) throws Exception {
        final long customerId = correctJsonCustomerData.getLong("id");
        customerForm.setEmail(email);
        mockMvc.perform(put(CUSTOMERS_URL + "/" + customerId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(customerForm)))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    @Order(19)
    public void DeleteExistingCustomerShouldReturnStatus200() throws Exception {
        final long customerId = correctJsonCustomerData.getLong("id");
        mockMvc.perform(delete(CUSTOMERS_URL + "/" + customerId))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @Order(20)
    public void DeleteNonExistingCustomerShouldReturnStatusBadRequest() throws Exception {
        final long customerId = correctJsonCustomerData.getLong("id");
        mockMvc.perform(delete(CUSTOMERS_URL + "/" + customerId))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }
}
