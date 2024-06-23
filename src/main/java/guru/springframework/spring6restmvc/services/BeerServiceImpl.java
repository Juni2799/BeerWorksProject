package guru.springframework.spring6restmvc.services;

import guru.springframework.spring6restmvc.model.BeerDTO;
import guru.springframework.spring6restmvc.model.BeerStyle;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

/**
 * Created by jt, Spring Framework Guru.
 */
@Slf4j
@Service
public class BeerServiceImpl implements BeerService {

    private Map<UUID, BeerDTO> beerHashMap;

    public BeerServiceImpl() {
        beerHashMap = new HashMap<>();

        BeerDTO beerDTO1 = BeerDTO.builder()
                .id(UUID.randomUUID())
                .version(1)
                .beerName("Galaxy Cat")
                .beerStyle(BeerStyle.PALE_ALE)
                .upc("12356")
                .price(new BigDecimal("12.99"))
                .quantityOnHand(122)
                .createdDate(LocalDateTime.now())
                .updateDate(LocalDateTime.now())
                .build();

        BeerDTO beerDTO2 = BeerDTO.builder()
                .id(UUID.randomUUID())
                .version(1)
                .beerName("Crank")
                .beerStyle(BeerStyle.PALE_ALE)
                .upc("12356222")
                .price(new BigDecimal("11.99"))
                .quantityOnHand(392)
                .createdDate(LocalDateTime.now())
                .updateDate(LocalDateTime.now())
                .build();

        BeerDTO beerDTO3 = BeerDTO.builder()
                .id(UUID.randomUUID())
                .version(1)
                .beerName("Sunshine City")
                .beerStyle(BeerStyle.IPA)
                .upc("12356")
                .price(new BigDecimal("13.99"))
                .quantityOnHand(144)
                .createdDate(LocalDateTime.now())
                .updateDate(LocalDateTime.now())
                .build();

        beerHashMap.put(beerDTO1.getId(), beerDTO1);
        beerHashMap.put(beerDTO2.getId(), beerDTO2);
        beerHashMap.put(beerDTO3.getId(), beerDTO3);
    }

    @Override
    public Page<BeerDTO> getBeers(String beerName, BeerStyle beerStyle, Boolean showInventory, Integer pageNumber, Integer pageSize) {
        return new PageImpl<>(new ArrayList<>(beerHashMap.values()));
    }

    @Override
    public BeerDTO saveNewBeer(BeerDTO beerDTO) {
        BeerDTO savedBeerDTO = BeerDTO.builder()
                .id(UUID.randomUUID())
                .version(1)
                .beerName(beerDTO.getBeerName())
                .beerStyle(beerDTO.getBeerStyle())
                .upc(beerDTO.getUpc())
                .quantityOnHand(beerDTO.getQuantityOnHand())
                .price(beerDTO.getPrice())
                .createdDate(LocalDateTime.now())
                .updateDate(LocalDateTime.now())
                .build();

        beerHashMap.put(savedBeerDTO.getId(), savedBeerDTO);

        return savedBeerDTO;
    }

    @Override
    public void updateBeerById(UUID beerId, BeerDTO beerDTO) {
        BeerDTO existingBeerDTO = beerHashMap.get(beerId);
        existingBeerDTO.setVersion(existingBeerDTO.getVersion() + 1);
        existingBeerDTO.setBeerName(beerDTO.getBeerName());
        existingBeerDTO.setPrice(beerDTO.getPrice());
        existingBeerDTO.setUpc(beerDTO.getUpc());
        existingBeerDTO.setQuantityOnHand(beerDTO.getQuantityOnHand());
        existingBeerDTO.setUpdateDate(LocalDateTime.now());

        beerHashMap.put(existingBeerDTO.getId(), existingBeerDTO);
    }

    @Override
    public boolean deleteBeerById(UUID id) {
        beerHashMap.remove(id);
        return true;
    }

    @Override
    public void modifyBeerById(UUID beerId, BeerDTO beerDTO) {
        BeerDTO existingBeerDTO = beerHashMap.get(beerId);

        if(StringUtils.hasText(beerDTO.getBeerName())){
            existingBeerDTO.setBeerName(beerDTO.getBeerName());
        }

        if(beerDTO.getBeerStyle() != null){
            existingBeerDTO.setBeerStyle(beerDTO.getBeerStyle());
        }

        if(StringUtils.hasText(beerDTO.getUpc())){
            existingBeerDTO.setUpc(beerDTO.getUpc());
        }

        if(beerDTO.getQuantityOnHand() != null){
            existingBeerDTO.setQuantityOnHand(beerDTO.getQuantityOnHand());
        }

        if(beerDTO.getPrice() != null){
            existingBeerDTO.setPrice(beerDTO.getPrice());
        }

        existingBeerDTO.setVersion(existingBeerDTO.getVersion() + 1);
        existingBeerDTO.setUpdateDate(LocalDateTime.now());

        beerHashMap.put(existingBeerDTO.getId(), existingBeerDTO);
    }

    @Override
    public BeerDTO getBeerById(UUID id) {

        log.debug("Get Beer by Id - in service. Id: " + id.toString());

        return beerHashMap.get(id);
    }
}
