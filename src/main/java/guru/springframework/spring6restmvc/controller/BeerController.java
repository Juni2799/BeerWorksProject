package guru.springframework.spring6restmvc.controller;

import guru.springframework.spring6restmvc.model.Beer;
import guru.springframework.spring6restmvc.services.BeerService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

/**
 * Created by jt, Spring Framework Guru.
 */
@Slf4j
@AllArgsConstructor
@RestController
public class BeerController {
    private final BeerService beerService;

    @PostMapping("/api/v1/beers")
    public ResponseEntity createNewBeer(@RequestBody Beer beer){
        Beer savedBeer = beerService.saveNewBeer(beer);

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Location-By-Id", "/api/v1/beers/" + savedBeer.getId().toString());

        return new ResponseEntity(httpHeaders, HttpStatus.CREATED);
    }

    @PutMapping("/api/v1/beers/{beerId}")
    public ResponseEntity updateExistingBeer(@PathVariable("beerId") UUID beerId, @RequestBody Beer beer){
        beerService.updateBeerById(beerId, beer);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("/api/v1/beers/{beerId}")
    public ResponseEntity deleteBeerById(@PathVariable("beerId") UUID id){
        beerService.deleteBeerById(id);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @PatchMapping("/api/v1/beers/{beerId}")
    public ResponseEntity modifyBeerById(@PathVariable("beerId") UUID beerId, @RequestBody Beer beer){
        beerService.modifyBeerById(beerId, beer);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @RequestMapping("/api/v1/beers")
    public List<Beer> getBeers(){
        return beerService.getBeers();
    }

    @RequestMapping("/api/v1/beers/{beerId}")
    public Beer getBeerById(@PathVariable("beerId") UUID beerId){

        log.debug("Get Beer by Id - in controller");

        return beerService.getBeerById(beerId);
    }

}
