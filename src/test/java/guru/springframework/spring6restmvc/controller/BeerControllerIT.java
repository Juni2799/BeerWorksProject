package guru.springframework.spring6restmvc.controller;

import guru.springframework.spring6restmvc.entities.Beer;
import guru.springframework.spring6restmvc.exceptions.NotFoundException;
import guru.springframework.spring6restmvc.model.BeerDTO;
import guru.springframework.spring6restmvc.repository.BeerRepository;
import guru.springframework.spring6restmvc.services.BeerService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
class BeerControllerIT {
    @Autowired
    private BeerController beerController;

    @Autowired
    private BeerService beerService;

    @Autowired
    private BeerRepository beerRepository;

    @Test
    public void getListBeersTest(){
        List<BeerDTO> beers = beerController.getBeers();

        assertThat(beers.size()).isEqualTo(3);
    }

    @Test
    //below 2 annotations are defined so that after the delete operation, Spring will rollback the database to original setup
    //for other tests to work correctly
    @Transactional
    @Rollback
    public void emptyListTest(){
        beerRepository.deleteAll();
        assertThat(beerController.getBeers().size()).isEqualTo(0);
    }

    @Test
    public void notFoundExceptionTest(){
        assertThrows(NotFoundException.class, () -> beerController.getBeerById(UUID.randomUUID()));
    }

    @Test
    public void getBeerByIdTest(){
        Beer savedBeer = beerRepository.findAll().get(0);

        BeerDTO beerDTO = beerController.getBeerById(savedBeer.getId());

        assertThat(savedBeer.getId()).isEqualTo(beerDTO.getId());
        assertThat(savedBeer.getBeerName()).isEqualTo(beerDTO.getBeerName());
    }

}