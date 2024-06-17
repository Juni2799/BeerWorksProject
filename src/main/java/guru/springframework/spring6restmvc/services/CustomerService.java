package guru.springframework.spring6restmvc.services;

import guru.springframework.spring6restmvc.model.CustomerDTO;

import java.util.*;

public interface CustomerService {
    List<CustomerDTO> getCustomers();

    CustomerDTO getCustomerById(UUID id);

    CustomerDTO saveNewCustomer(CustomerDTO customerDTO);

    void updateCustomerById(UUID customerId, CustomerDTO customerDTO);

    boolean deleteCustomerById(UUID id);

    void modifyCustomerById(UUID customerId, CustomerDTO customerDTO);
}
