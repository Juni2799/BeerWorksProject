package guru.springframework.spring6restmvc.services;

import guru.springframework.spring6restmvc.model.BeerDTO;
import guru.springframework.spring6restmvc.model.BeerStyle;

import java.util.List;
import java.util.UUID;

/**
 * Created by jt, Spring Framework Guru.
 */
public interface BeerService {

    BeerDTO getBeerById(UUID id);

    List<BeerDTO> getBeers(String beerName, BeerStyle beerStyle, Boolean showInventory);

    BeerDTO saveNewBeer(BeerDTO beerDTO);

    void updateBeerById(UUID beerId, BeerDTO beerDTO);

    boolean deleteBeerById(UUID id);

    void modifyBeerById(UUID beerId, BeerDTO beerDTO);
}
