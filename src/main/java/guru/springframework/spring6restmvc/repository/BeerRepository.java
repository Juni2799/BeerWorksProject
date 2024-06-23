package guru.springframework.spring6restmvc.repository;

import guru.springframework.spring6restmvc.entities.Beer;
import guru.springframework.spring6restmvc.model.BeerStyle;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface BeerRepository extends JpaRepository<Beer, UUID> {
    Page<Beer> findBeerByBeerNameIsLikeIgnoreCase(String beerName, Pageable pageable);

    Page<Beer> findBeerByBeerStyle(BeerStyle beerStyle,  Pageable pageable);

    Page<Beer> findBeerByBeerNameIsLikeIgnoreCaseAndBeerStyle(String beerName, BeerStyle beerStyle,  Pageable pageable);
}
