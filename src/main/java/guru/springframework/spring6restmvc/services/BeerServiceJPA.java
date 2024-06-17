package guru.springframework.spring6restmvc.services;

import guru.springframework.spring6restmvc.entities.Beer;
import guru.springframework.spring6restmvc.exceptions.NotFoundException;
import guru.springframework.spring6restmvc.mappers.BeerMapper;
import guru.springframework.spring6restmvc.model.BeerDTO;
import guru.springframework.spring6restmvc.repository.BeerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Primary
public class BeerServiceJPA implements BeerService{
    @Autowired
    private BeerRepository beerRepository;

    @Autowired
    private BeerMapper beerMapper;

    @Override
    public BeerDTO getBeerById(UUID id) {
        Beer savedBeer = beerRepository.findById(id).orElseThrow(() -> new NotFoundException("No beer found for id: " + id));
        return beerMapper.beerToBeerDTO(savedBeer);
    }

    @Override
    public List<BeerDTO> getBeers() {
        return beerRepository.findAll()
                .stream()
                .map(beerMapper::beerToBeerDTO)
                .collect(Collectors.toList());
    }

    @Override
    public BeerDTO saveNewBeer(BeerDTO beerDTO) {
        return beerMapper.beerToBeerDTO(beerRepository.save(beerMapper.beerDTOtoBeer(beerDTO)));
    }

    @Override
    public void updateBeerById(UUID beerId, BeerDTO beerDTO) {
        Beer savedBeer = beerRepository.findById(beerId).orElseThrow(() -> new NotFoundException("No beer found with id: " + beerId));
        savedBeer.setBeerName(beerDTO.getBeerName());
        savedBeer.setBeerStyle(beerDTO.getBeerStyle());
        savedBeer.setUpc(beerDTO.getUpc());
        savedBeer.setPrice(beerDTO.getPrice());

        beerRepository.save(savedBeer);
    }

    @Override
    public boolean deleteBeerById(UUID id) {
        beerRepository.deleteById(id);
        return true;
    }

    @Override
    public void modifyBeerById(UUID beerId, BeerDTO beerDTO) {
        Beer savedBeer = beerRepository.findById(beerId).orElseThrow(() -> new NotFoundException("No beer found with id: " + beerId));
        if(!beerDTO.getBeerName().isEmpty()) savedBeer.setBeerName(beerDTO.getBeerName());
        if(beerDTO.getBeerStyle() != null) savedBeer.setBeerStyle(beerDTO.getBeerStyle());
        if(!beerDTO.getUpc().isEmpty()) savedBeer.setUpc(beerDTO.getUpc());
        if(beerDTO.getPrice() != null) savedBeer.setPrice(beerDTO.getPrice());

        beerRepository.save(savedBeer);
    }
}
