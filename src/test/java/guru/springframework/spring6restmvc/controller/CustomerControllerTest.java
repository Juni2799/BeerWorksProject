package guru.springframework.spring6restmvc.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import guru.springframework.spring6restmvc.model.CustomerDTO;
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
        CustomerDTO customerDTO = getCustomerMockData();
        given(customerService.getCustomerById(customerDTO.getId())).willReturn(customerDTO);

        mockMvc.perform(get("/api/v1/customers/" + customerDTO.getId()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(customerDTO.getId().toString())))
                .andExpect(jsonPath("$.customerName", is(customerDTO.getCustomerName())));
    }

    @Test
    public void getCustomersTest() throws Exception{
        List<CustomerDTO> customerDTOS = getCustomerListMockData();
        given(customerService.getCustomers()).willReturn(customerDTOS);

        mockMvc.perform(get("/api/v1/customers"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()", is(3)));
    }

    @Test
    public void createCustomerTest() throws Exception {
        List<CustomerDTO> customerDTOS = getCustomerListMockData();
        given(customerService.saveNewCustomer(any(CustomerDTO.class))).willReturn(customerDTOS.get(1));

        mockMvc.perform(post("/api/v1/customers")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(customerDTOS.get(0))))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location-By-Id"));
    }

    @Test
    public void updateCustomerTest() throws Exception {
        List<CustomerDTO> customerDTOS = getCustomerListMockData();

        mockMvc.perform(put("/api/v1/customers/" + customerDTOS.get(0).getId())
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(customerDTOS.get(0))))
                .andExpect(status().isNoContent());

        verify(customerService, times(1)).updateCustomerById(any(UUID.class), any(CustomerDTO.class));
    }

    @Test
    public void deleteCustomersByIdTest() throws Exception {
        List<CustomerDTO> customerDTOS = getCustomerListMockData();

        mockMvc.perform(delete("/api/v1/customers/" + customerDTOS.get(0).getId()))
                .andExpect(status().isNoContent());

        ArgumentCaptor<UUID> uuidArgumentCaptor = ArgumentCaptor.forClass(UUID.class);
        verify(customerService, times(1)).deleteCustomerById(uuidArgumentCaptor.capture());

        assertThat(customerDTOS.get(0).getId()).isEqualTo(uuidArgumentCaptor.getValue());
    }

    @Test
    public void modifyCustomerByIdTest() throws Exception{
        List<CustomerDTO> customerDTOS = getCustomerListMockData();

        Map<String, Object> customerMap = new HashMap<>();
        customerMap.put("customerName", "New customer");

        mockMvc.perform(patch("/api/v1/customers/" + customerDTOS.get(0).getId())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(customerMap)))
                .andExpect(status().isNoContent());

        ArgumentCaptor<UUID> uuidArgumentCaptor = ArgumentCaptor.forClass(UUID.class);
        ArgumentCaptor<CustomerDTO> customerArgumentCaptor = ArgumentCaptor.forClass(CustomerDTO.class);
        verify(customerService).modifyCustomerById(uuidArgumentCaptor.capture(), customerArgumentCaptor.capture());

        assertThat(customerDTOS.get(0).getId()).isEqualTo(uuidArgumentCaptor.getValue());
        assertThat(customerMap.get("customerName")).isEqualTo(customerArgumentCaptor.getValue().getCustomerName());
    }

    private CustomerDTO getCustomerMockData(){
        return CustomerDTO.builder()
                .id(UUID.randomUUID())
                .customerName("customerName")
                .build();
    }

    private List<CustomerDTO> getCustomerListMockData(){
        List<CustomerDTO> customerDTOS = new ArrayList<>();
        CustomerDTO customerDTO1 = CustomerDTO.builder()
                .id(UUID.randomUUID())
                .customerName("customerName1")
                .build();

        CustomerDTO customerDTO2 = CustomerDTO.builder()
                .id(UUID.randomUUID())
                .customerName("customerName2")
                .build();

        CustomerDTO customerDTO3 = CustomerDTO.builder()
                .id(UUID.randomUUID())
                .customerName("customerName3")
                .build();

        customerDTOS.add(customerDTO1);
        customerDTOS.add(customerDTO2);
        customerDTOS.add(customerDTO3);
        return customerDTOS;
    }
}
