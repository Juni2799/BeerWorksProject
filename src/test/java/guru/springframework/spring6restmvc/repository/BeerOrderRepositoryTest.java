package guru.springframework.spring6restmvc.repository;

import guru.springframework.spring6restmvc.entities.Beer;
import guru.springframework.spring6restmvc.entities.BeerOrder;
import guru.springframework.spring6restmvc.entities.Customer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
public class BeerOrderRepositoryTest {
    @Autowired
    private BeerRepository beerRepository;
    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private BeerOrderRepository beerOrderRepository;

    Beer testBeer;
    Customer testCustomer;

    @BeforeEach
    void setUp(){
        testBeer = beerRepository.findAll().get(0);
        testCustomer = customerRepository.findAll().get(0);
    }

    @Test
    @Transactional
    @Rollback
    void testBeerOrders(){
        BeerOrder beerOrder = BeerOrder.builder()
                .customerRef("test Order")
                .customer(testCustomer)
                .build();

        BeerOrder savedBeerOrder = beerOrderRepository.saveAndFlush(beerOrder);

        //System.out.println(savedBeerOrder);
    }
}
