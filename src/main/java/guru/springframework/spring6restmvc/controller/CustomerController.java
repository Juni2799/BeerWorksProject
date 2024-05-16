package guru.springframework.spring6restmvc.controller;

import guru.springframework.spring6restmvc.model.Customer;
import guru.springframework.spring6restmvc.services.CustomerService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

    @RequestMapping("/api/v1/customers/{customerId}")
    public Customer getCustomerById(@PathVariable("customerId") UUID customerId){
        return customerService.getCustomerById(customerId);
    }
}
