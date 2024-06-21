package guru.springframework.spring6restmvc.controller;

import guru.springframework.spring6restmvc.entities.Beer;
import guru.springframework.spring6restmvc.exceptions.NotFoundException;
import guru.springframework.spring6restmvc.mappers.BeerMapper;
import guru.springframework.spring6restmvc.model.BeerDTO;
import guru.springframework.spring6restmvc.repository.BeerRepository;
import guru.springframework.spring6restmvc.services.BeerService;
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
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
class BeerControllerIT {
    @Autowired
    private BeerController beerController;

    @Autowired
    private BeerMapper beerMapper;

    @Autowired
    private BeerRepository beerRepository;

    @Test
    public void getListBeersTest(){
        List<BeerDTO> beers = beerController.getBeers();

        assertThat(beers.size()).isEqualTo(2413);
    }

    @Test
    //below 2 annotations are defined so that after the delete operation, Spring will roll back the database to original setup
    //for other tests to work correctly
    @Transactional
    @Rollback
    public void emptyListTest(){
        beerRepository.deleteAll();
        assertThat(beerController.getBeers().size()).isEqualTo(0);
    }

    @Test
    public void beerIdNotFoundExceptionTest(){
        assertThrows(NotFoundException.class, () -> beerController.getBeerById(UUID.randomUUID()));
    }

    @Test
    public void getBeerByIdTest(){
        Beer savedBeer = beerRepository.findAll().get(0);

        BeerDTO beerDTO = beerController.getBeerById(savedBeer.getId());

        assertThat(savedBeer.getId()).isEqualTo(beerDTO.getId());
        assertThat(savedBeer.getBeerName()).isEqualTo(beerDTO.getBeerName());
    }

    @Test
    @Transactional
    @Rollback
    public void deleteByIdTest(){
        Beer savedBeer = beerRepository.findAll().get(0);

        ResponseEntity responseEntity = beerController.deleteBeerById(savedBeer.getId());

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.valueOf(204));
        assertThat(beerRepository.findById(savedBeer.getId())).isEmpty();
    }

    @Test
    @Transactional
    @Rollback
    public void saveNewBeerTest(){
        BeerDTO beerDTO = BeerDTO.builder()
                .beerName("test beer")
                .build();

        ResponseEntity responseEntity = beerController.createNewBeer(beerDTO);

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.valueOf(201));
        assertThat(responseEntity.getHeaders().get("Location-By-Id")).isNotNull();

        String[] headerDetails = responseEntity.getHeaders().get("Location-By-Id").get(0).split("/");
        UUID savedUUID = UUID.fromString(headerDetails[4]);

        Beer savedNewBeer = beerRepository.findById(savedUUID).get();
        assertThat(savedNewBeer.getBeerName()).isEqualTo(beerDTO.getBeerName());
    }

    @Test
    @Transactional
    @Rollback
    public void updateBeerByIdTest(){
        Beer savedBeer = beerRepository.findAll().get(0);
        BeerDTO beerDTO = beerMapper.beerToBeerDTO(savedBeer);
        beerDTO.setId(null);
        beerDTO.setVersion(null);
        beerDTO.setBeerName("Updated Beer Name");

        ResponseEntity responseEntity = beerController.updateExistingBeer(savedBeer.getId(), beerDTO);

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(204));

        Beer updatedBeer = beerRepository.findById(savedBeer.getId()).get();
        assertThat(updatedBeer.getBeerName()).isEqualTo(beerDTO.getBeerName());
    }

    @Test
    public void updateBeerByIdNotFoundExceptionTest(){
        assertThrows(NotFoundException.class,() -> beerController.updateExistingBeer(UUID.randomUUID(), BeerDTO.builder().build()));
    }
}