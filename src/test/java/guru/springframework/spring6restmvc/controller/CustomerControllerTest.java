package guru.springframework.spring6restmvc.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import guru.springframework.spring6restmvc.model.Customer;
import guru.springframework.spring6restmvc.services.CustomerService;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.core.Is.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CustomerController.class)
public class CustomerControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CustomerService customerService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void getCustomerByIdTest() throws Exception {
        Customer customer = getCustomerMockData();
        given(customerService.getCustomerById(customer.getId())).willReturn(customer);

        mockMvc.perform(get("/api/v1/customers/" + customer.getId()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(customer.getId().toString())))
                .andExpect(jsonPath("$.customerName", is(customer.getCustomerName())));
    }

    @Test
    public void getCustomersTest() throws Exception{
        List<Customer> customers = getCustomerListMockData();
        given(customerService.getCustomers()).willReturn(customers);

        mockMvc.perform(get("/api/v1/customers"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()", is(3)));
    }

    @Test
    public void createCustomerTest() throws Exception {
        List<Customer> customers = getCustomerListMockData();
        given(customerService.saveNewCustomer(any(Customer.class))).willReturn(customers.get(1));

        mockMvc.perform(post("/api/v1/customers")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(customers.get(0))))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location-By-Id"));
    }

    @Test
    public void updateCustomerTest() throws Exception {
        List<Customer> customers = getCustomerListMockData();

        mockMvc.perform(put("/api/v1/customers/" + customers.get(0).getId())
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(customers.get(0))))
                .andExpect(status().isNoContent());

        verify(customerService, times(1)).updateCustomerById(any(UUID.class), any(Customer.class));
    }

    @Test
    public void deleteCustomersByIdTest() throws Exception {
        List<Customer> customers = getCustomerListMockData();

        mockMvc.perform(delete("/api/v1/customers/" + customers.get(0).getId()))
                .andExpect(status().isNoContent());

        ArgumentCaptor<UUID> uuidArgumentCaptor = ArgumentCaptor.forClass(UUID.class);
        verify(customerService, times(1)).deleteCustomerById(uuidArgumentCaptor.capture());

        assertThat(customers.get(0).getId()).isEqualTo(uuidArgumentCaptor.getValue());
    }

    @Test
    public void modifyCustomerByIdTest() throws Exception{
        List<Customer> customers = getCustomerListMockData();

        Map<String, Object> customerMap = new HashMap<>();
        customerMap.put("customerName", "New customer");

        mockMvc.perform(patch("/api/v1/customers/" + customers.get(0).getId())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(customerMap)))
                .andExpect(status().isNoContent());

        ArgumentCaptor<UUID> uuidArgumentCaptor = ArgumentCaptor.forClass(UUID.class);
        ArgumentCaptor<Customer> customerArgumentCaptor = ArgumentCaptor.forClass(Customer.class);
        verify(customerService).modifyCustomerById(uuidArgumentCaptor.capture(), customerArgumentCaptor.capture());

        assertThat(customers.get(0).getId()).isEqualTo(uuidArgumentCaptor.getValue());
        assertThat(customerMap.get("customerName")).isEqualTo(customerArgumentCaptor.getValue().getCustomerName());
    }

    private Customer getCustomerMockData(){
        return Customer.builder()
                .id(UUID.randomUUID())
                .customerName("customerName")
                .build();
    }

    private List<Customer> getCustomerListMockData(){
        List<Customer> customers = new ArrayList<>();
        Customer customer1 = Customer.builder()
                .id(UUID.randomUUID())
                .customerName("customerName1")
                .build();

        Customer customer2 = Customer.builder()
                .id(UUID.randomUUID())
                .customerName("customerName2")
                .build();

        Customer customer3 = Customer.builder()
                .id(UUID.randomUUID())
                .customerName("customerName3")
                .build();

        customers.add(customer1);
        customers.add(customer2);
        customers.add(customer3);
        return customers;
    }
}
