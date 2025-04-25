package guru.springframework.spring6restmvc.services;

import guru.springframework.spring6restmvc.entities.Beer;
import guru.springframework.spring6restmvc.exceptions.NotFoundException;
import guru.springframework.spring6restmvc.mappers.BeerMapper;
import guru.springframework.spring6restmvc.model.BeerDTO;
import guru.springframework.spring6restmvc.model.BeerStyle;
import guru.springframework.spring6restmvc.repository.BeerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.UUID;

@Service
@Primary
public class BeerServiceJPA implements BeerService{
    private static final int DEFAULT_PAGE = 0;
    private static final int DEFAULT_PAGE_SIZE = 25;
    @Autowired
    private BeerRepository beerRepository;

    @Autowired
    private BeerMapper beerMapper;

    @Autowired
    private CacheManager cacheManager;

    @Cacheable(cacheNames = "beerCache", key = "#id")
    @Override
    public BeerDTO getBeerById(UUID id) {
        Beer savedBeer = beerRepository.findById(id).orElseThrow(() -> new NotFoundException("No beer found for id: " + id));
        return beerMapper.beerToBeerDTO(savedBeer);
    }

    @Cacheable(cacheNames = "beerListCache")
    @Override
    public Page<BeerDTO> getBeers(String beerName, BeerStyle beerStyle, Boolean showInventory, Integer pageNumber, Integer pageSize) {
        PageRequest pageRequest = buildPageRequest(pageNumber, pageSize);

        Page<Beer> beerPage;

        if(StringUtils.hasText(beerName) && beerStyle == null){
            beerPage = listBeersByBeerName(beerName, pageRequest);
        }else if(!StringUtils.hasText(beerName) && beerStyle != null){
            beerPage = listBeersByBeerStyle(beerStyle, pageRequest);
        }else if(StringUtils.hasText(beerName) && beerStyle != null){
            beerPage = listBeersByBeerNameAndBeerStyle(beerName, beerStyle, pageRequest);
        }else{
            beerPage = beerRepository.findAll(pageRequest);
        }

        if(showInventory!=null && !showInventory){
            beerPage.forEach(beer -> beer.setQuantityOnHand(null));
        }

//        return beerList
//                .stream()
//                .map(beerMapper::beerToBeerDTO)
//                .collect(Collectors.toList());

        return beerPage.map(beerMapper::beerToBeerDTO);
    }

    private PageRequest buildPageRequest(Integer pageNumber, Integer pageSize){
        int queryPageNumber;
        int queryPageSize;

        if(pageNumber != null && pageNumber > 0){
            queryPageNumber = pageNumber - 1;
        }else{
            queryPageNumber = DEFAULT_PAGE;
        }

        if(pageSize == null){
            queryPageSize = DEFAULT_PAGE_SIZE;
        }else{
            if(pageSize > 1000){
                queryPageSize = 1000;
            }else{
                queryPageSize = pageSize;
            }
        }

        Sort sort = Sort.by(Sort.Order.asc("beerName"));

        return PageRequest.of(queryPageNumber, queryPageSize, sort);
    }

    private Page<Beer> listBeersByBeerName(String beerName, PageRequest pageRequest){
        return beerRepository.findBeerByBeerNameIsLikeIgnoreCase("%" + beerName + "%", pageRequest);
    }

    private Page<Beer> listBeersByBeerStyle(BeerStyle beerStyle, PageRequest pageRequest){
        return beerRepository.findBeerByBeerStyle(beerStyle, pageRequest);
    }

    private Page<Beer> listBeersByBeerNameAndBeerStyle(String beerName, BeerStyle beerStyle, PageRequest pageRequest){
        return beerRepository.findBeerByBeerNameIsLikeIgnoreCaseAndBeerStyle("%" + beerName + "%", beerStyle, pageRequest);
    }

    @Override
    public BeerDTO saveNewBeer(BeerDTO beerDTO) {
        //For deleting cache for every new update
        cacheManager.getCache("beerListCache").clear();

        return beerMapper.beerToBeerDTO(beerRepository.save(beerMapper.beerDTOtoBeer(beerDTO)));
    }

    @Override
    public void updateBeerById(UUID beerId, BeerDTO beerDTO) {
        //For deleting cache for every new update
        cacheManager.getCache("beerCache").evict(beerId);
        cacheManager.getCache("beerListCache").clear();

        Beer savedBeer = beerRepository.findById(beerId).orElseThrow(() -> new NotFoundException("No beer found with id: " + beerId));
        savedBeer.setBeerName(beerDTO.getBeerName());
        savedBeer.setBeerStyle(beerDTO.getBeerStyle());
        savedBeer.setUpc(beerDTO.getUpc());
        savedBeer.setPrice(beerDTO.getPrice());

        beerRepository.save(savedBeer);
    }

    @Override
    public boolean deleteBeerById(UUID id) {
        //For deleting cache for every new update
        cacheManager.getCache("beerCache").evict(id);
        cacheManager.getCache("beerListCache").clear();

        beerRepository.deleteById(id);
        return true;
    }

    @Override
    public void modifyBeerById(UUID beerId, BeerDTO beerDTO) {
        //For deleting cache for every new update
        cacheManager.getCache("beerCache").evict(beerId);
        cacheManager.getCache("beerListCache").clear();

        Beer savedBeer = beerRepository.findById(beerId).orElseThrow(() -> new NotFoundException("No beer found with id: " + beerId));
        if(!beerDTO.getBeerName().isEmpty()) savedBeer.setBeerName(beerDTO.getBeerName());
        if(beerDTO.getBeerStyle() != null) savedBeer.setBeerStyle(beerDTO.getBeerStyle());
        if(!beerDTO.getUpc().isEmpty()) savedBeer.setUpc(beerDTO.getUpc());
        if(beerDTO.getPrice() != null) savedBeer.setPrice(beerDTO.getPrice());

        beerRepository.save(savedBeer);
    }
}
