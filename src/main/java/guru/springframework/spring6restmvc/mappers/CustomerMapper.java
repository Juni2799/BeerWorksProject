package guru.springframework.spring6restmvc.mappers;

import guru.springframework.spring6restmvc.entities.Customer;
import guru.springframework.spring6restmvc.model.CustomerDTO;
import org.mapstruct.Mapper;

@Mapper
public interface CustomerMapper {
    Customer CustomerDTOtoCustomer(CustomerDTO customerDTO);

    CustomerDTO CustomerToCustomerDTO(Customer customer);
}
