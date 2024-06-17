package guru.springframework.spring6restmvc.mappers;

import guru.springframework.spring6restmvc.entities.Customer;
import guru.springframework.spring6restmvc.model.CustomerDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface CustomerMapper {
    Customer customerDTOtoCustomer(CustomerDTO customerDTO);
    @Mapping(target = "customerName", source = "customer.name")

    CustomerDTO customerToCustomerDTO(Customer customer);
}
