package guru.springframework.spring6restmvc.services;

import guru.springframework.spring6restmvc.model.Customer;

import java.util.*;

public interface CustomerService {
    List<Customer> getCustomers();

    Customer getCustomerById(UUID id);

    Customer saveNewCustomer(Customer customer);

    void updateCustomerById(UUID customerId, Customer customer);

    void deleteBeerById(UUID id);

    void modifyBeerById(UUID customerId, Customer customer);
}
