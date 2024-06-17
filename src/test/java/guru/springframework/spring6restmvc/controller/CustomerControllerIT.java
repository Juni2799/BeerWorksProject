package guru.springframework.spring6restmvc.controller;

import guru.springframework.spring6restmvc.entities.Customer;
import guru.springframework.spring6restmvc.exceptions.NotFoundException;
import guru.springframework.spring6restmvc.model.CustomerDTO;
import guru.springframework.spring6restmvc.repository.CustomerRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class CustomerControllerIT {
    @Autowired
    private CustomerController customerController;

    @Autowired
    private CustomerRepository customerRepository;

    @Test
    public void getCustomerListTest(){
        List<CustomerDTO> customers = customerController.getCustomers();

        assertThat(customers.size()).isEqualTo(3);
    }

    @Test
    @Transactional
    @Rollback
    public void emptyListTest(){
        customerRepository.deleteAll();

        assertThat(customerRepository.findAll().size()).isEqualTo(0);
    }

    @Test
    public void getCustomerByIdTest(){
        Customer savedCustomer = customerRepository.findAll().get(0);

        CustomerDTO customerDTO = customerController.getCustomerById(savedCustomer.getId());

        assertThat(savedCustomer.getId()).isEqualTo(customerDTO.getId());
        assertThat(savedCustomer.getName()).isEqualTo(customerDTO.getCustomerName());
    }

    @Test
    public void customerNotFoundExceptionTest(){
        assertThrows(NotFoundException.class, () -> customerController.getCustomerById(UUID.randomUUID()));
    }

}