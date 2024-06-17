package guru.springframework.spring6restmvc.services;

import guru.springframework.spring6restmvc.entities.Beer;
import guru.springframework.spring6restmvc.entities.Customer;
import guru.springframework.spring6restmvc.exceptions.NotFoundException;
import guru.springframework.spring6restmvc.mappers.CustomerMapper;
import guru.springframework.spring6restmvc.model.CustomerDTO;
import guru.springframework.spring6restmvc.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Primary
public class CustomerServiceJPA implements CustomerService{
    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private CustomerMapper customerMapper;

    @Override
    public List<CustomerDTO> getCustomers() {
        return customerRepository.findAll()
                .stream()
                .map(customerMapper::customerToCustomerDTO)
                .collect(Collectors.toList());
    }

    @Override
    public CustomerDTO getCustomerById(UUID id) {
        Customer savedCustomer = customerRepository.findById(id).orElseThrow(() -> new NotFoundException("No customer found with id: " + id));
        return customerMapper.customerToCustomerDTO(savedCustomer);
    }

    @Override
    public CustomerDTO saveNewCustomer(CustomerDTO customerDTO) {
        return customerMapper.customerToCustomerDTO(customerRepository.save(customerMapper.customerDTOtoCustomer(customerDTO)));
    }

    @Override
    public void updateCustomerById(UUID customerId, CustomerDTO customerDTO) {
        Customer savedCustomer = customerRepository.findById(customerId).orElseThrow(() -> new NotFoundException("No beer found with id: " + customerId));
        savedCustomer.setName(customerDTO.getCustomerName());

        customerRepository.save(savedCustomer);
    }

    @Override
    public boolean deleteCustomerById(UUID id) {
        customerRepository.deleteById(id);
        return true;
    }

    @Override
    public void modifyCustomerById(UUID customerId, CustomerDTO customerDTO) {
        Customer savedCustomer = customerRepository.findById(customerId).orElseThrow(() -> new NotFoundException("No beer found with id: " + customerId));
        if(!customerDTO.getCustomerName().isEmpty()) savedCustomer.setName(customerDTO.getCustomerName());

        customerRepository.save(savedCustomer);
    }
}
