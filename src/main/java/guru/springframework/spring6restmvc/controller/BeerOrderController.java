package guru.springframework.spring6restmvc.controller;

import guru.springframework.spring6restmvc.entities.BeerOrder;
import guru.springframework.spring6restmvc.entities.Customer;
import guru.springframework.spring6restmvc.model.BeerOrderCreateDTO;
import guru.springframework.spring6restmvc.model.BeerOrderDTO;
import guru.springframework.spring6restmvc.model.BeerOrderUpdateDTO;
import guru.springframework.spring6restmvc.services.BeerOrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class BeerOrderController {
    private static final String BEER_ORDER_PATH = "/api/v1/beerOrders";
    private static final String BEER_ORDER_BY_ID = BEER_ORDER_PATH + "/{beerOrderId}";

    private final BeerOrderService beerOrderService;

    @GetMapping(BEER_ORDER_PATH)
    public Page<BeerOrderDTO> listBeerOrders(@RequestParam(required = false) Integer pageNumber,
                                             @RequestParam(required = false) Integer pageSize){
        return beerOrderService.getBeers(pageNumber, pageSize);
    }

    @GetMapping(BEER_ORDER_BY_ID)
    public ResponseEntity<BeerOrderDTO> getBeerOrderById(@PathVariable UUID beerOrderId){
        return ResponseEntity.ok(beerOrderService.getBeerById(beerOrderId));
    }

    @DeleteMapping(BEER_ORDER_BY_ID)
    public ResponseEntity<Void> deleteById(@PathVariable UUID beerOrderId){
        beerOrderService.deleteBeerOrder(beerOrderId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping(BEER_ORDER_PATH)
    public ResponseEntity<Void> createOrder(@RequestBody BeerOrderCreateDTO beerOrderCreateDTO) {
        BeerOrder savedOrder =  beerOrderService.createOrder(beerOrderCreateDTO);

        return ResponseEntity.created(URI.create(BEER_ORDER_PATH + "/" + savedOrder.getId().toString())).build();
    }

    @PutMapping(BEER_ORDER_BY_ID)
    public ResponseEntity<BeerOrderDTO> updateOrder(@PathVariable UUID beerOrderId, @RequestBody BeerOrderUpdateDTO beerOrderUpdateDTO) {
        return ResponseEntity.ok(beerOrderService.updateOrder(beerOrderId, beerOrderUpdateDTO));
    }
}
