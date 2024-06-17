package guru.springframework.spring6restmvc.controller;

import guru.springframework.spring6restmvc.entities.Beer;
import guru.springframework.spring6restmvc.entities.Customer;
import guru.springframework.spring6restmvc.exceptions.NotFoundException;
import guru.springframework.spring6restmvc.mappers.CustomerMapper;
import guru.springframework.spring6restmvc.model.BeerDTO;
import guru.springframework.spring6restmvc.model.CustomerDTO;
import guru.springframework.spring6restmvc.repository.CustomerRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
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
    
    @Autowired
    private CustomerMapper customerMapper;

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

    @Test
    @Transactional
    @Rollback
    public void deleteByIdTest(){
        Customer savedCustomer = customerRepository.findAll().get(0);

        ResponseEntity responseEntity = customerController.deleteCustomerById(savedCustomer.getId());

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.valueOf(204));
        assertThat(customerRepository.findById(savedCustomer.getId())).isEmpty();
    }

    @Test
    @Transactional
    @Rollback
    public void saveNewBeerTest(){
        CustomerDTO customerDTO = CustomerDTO.builder()
                .customerName("test customer")
                .build();

        ResponseEntity responseEntity = customerController.createNewCustomer(customerDTO);

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.valueOf(201));
        assertThat(responseEntity.getHeaders().get("Location-By-Id")).isNotNull();

        String[] headerDetails = responseEntity.getHeaders().get("Location-By-Id").get(0).split("/");
        UUID savedUUID = UUID.fromString(headerDetails[4]);

        Customer savedNewCustomer = customerRepository.findById(savedUUID).get();
        assertThat(savedNewCustomer.getName()).isEqualTo(customerDTO.getCustomerName());
    }

    @Test
    @Transactional
    @Rollback
    public void updateBeerByIdTest(){
        Customer savedCustomer = customerRepository.findAll().get(0);
        CustomerDTO customerDTO = customerMapper.customerToCustomerDTO(savedCustomer);
        customerDTO.setId(null);
        customerDTO.setVersion(null);
        customerDTO.setCustomerName("Updated Customer Name");

        ResponseEntity responseEntity = customerController.updateExistingCustomer(savedCustomer.getId(), customerDTO);

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(204));

        Customer updatedCustomer = customerRepository.findById(savedCustomer.getId()).get();
        assertThat(updatedCustomer.getName()).isEqualTo(customerDTO.getCustomerName());
    }

    @Test
    public void updateBeerByIdNotFoundExceptionTest(){
        assertThrows(NotFoundException.class,() -> customerController.updateExistingCustomer(UUID.randomUUID(), CustomerDTO.builder().build()));
    }
}