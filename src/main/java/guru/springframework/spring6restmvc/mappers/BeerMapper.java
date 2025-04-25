package guru.springframework.spring6restmvc.mappers;

import guru.springframework.spring6restmvc.entities.Beer;
import guru.springframework.spring6restmvc.entities.BeerAudit;
import guru.springframework.spring6restmvc.model.BeerDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface BeerMapper {
    @Mapping(target = "categories", ignore = true)
    @Mapping(target = "beerOrderLines", ignore = true)
    Beer beerDTOtoBeer(BeerDTO beerDTO);

    BeerDTO beerToBeerDTO(Beer beer);

    @Mapping(target = "createdDateAudit", ignore = true)
    @Mapping(target = "principalName", ignore = true)
    @Mapping(target = "auditEventType", ignore = true)
    @Mapping(target = "auditId", ignore = true)
    BeerAudit beerToBeerAudit(Beer beer);
}
