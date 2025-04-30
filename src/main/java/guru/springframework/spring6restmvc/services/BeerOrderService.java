package guru.springframework.spring6restmvc.services;

import guru.springframework.spring6restmvc.entities.BeerOrder;
import guru.springframework.spring6restmvc.model.BeerOrderCreateDTO;
import guru.springframework.spring6restmvc.model.BeerOrderDTO;
import guru.springframework.spring6restmvc.model.BeerOrderUpdateDTO;
import org.springframework.data.domain.Page;

import java.util.UUID;

public interface BeerOrderService {
    BeerOrderDTO getBeerById(UUID id);

    Page<BeerOrderDTO> getBeers(Integer pageNumber, Integer pageSize);

    void deleteBeerOrder(UUID beerOrderId);

    BeerOrder createOrder(BeerOrderCreateDTO beerOrderCreateDTO);

    BeerOrderDTO updateOrder(UUID beerOrderId, BeerOrderUpdateDTO beerOrderUpdateDTO);
}
