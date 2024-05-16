package guru.springframework.spring6restmvc.controller;

import guru.springframework.spring6restmvc.model.Beer;
import guru.springframework.spring6restmvc.model.Customer;
import guru.springframework.spring6restmvc.services.CustomerService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Slf4j
@AllArgsConstructor
@RestController
public class CustomerController {
    private final CustomerService customerService;

    @RequestMapping("/api/v1/customers")
    public List<Customer> getCustomers(){
        return customerService.getCustomers();
    }

    @PostMapping("/api/v1/customers")
    public ResponseEntity createNewCustomer(@RequestBody Customer customer){
        Customer savedCustomer = customerService.saveNewCustomer(customer);

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Location-By-Id", "/api/v1/customers/" + savedCustomer.getId().toString());

        return new ResponseEntity(httpHeaders, HttpStatus.CREATED);
    }

    @PutMapping("/api/v1/customers/{customerId}")
    public ResponseEntity updateExistingCustomer(@PathVariable("customerId") UUID customerId, @RequestBody Customer customer){
        customerService.updateCustomerById(customerId, customer);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("/api/v1/customers/{customerId}")
    public ResponseEntity deleteCustomerById(@PathVariable("customerId") UUID id){
        customerService.deleteBeerById(id);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @PatchMapping("/api/v1/customers/{customerId}")
    public ResponseEntity modifyCustomerById(@PathVariable("customerId") UUID customerId, @RequestBody Customer customer){
        customerService.modifyBeerById(customerId, customer);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }


    @RequestMapping("/api/v1/customers/{customerId}")
    public Customer getCustomerById(@PathVariable("customerId") UUID customerId){
        return customerService.getCustomerById(customerId);
    }
}
