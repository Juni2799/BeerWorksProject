package guru.springframework.spring6restmvc.controller;

import guru.springframework.spring6restmvc.model.Customer;
import guru.springframework.spring6restmvc.services.CustomerService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.hamcrest.core.Is.is;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CustomerController.class)
public class CustomerControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CustomerService customerService;

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
