package guru.springframework.spring6restmvc.model;

import guru.springframework.spring6restmvc.entities.BeerOrderLineStatus;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@Builder
@Data
public class BeerOrderLineDTO {
    private UUID id;
    private Long version;
    private LocalDateTime createdDate;
    private LocalDateTime lastModifiedDate;
    private BeerOrderLineStatus beerOrderLineStatus;
    private Integer orderQuantity;
    private Integer quantityAllocated;
    private BeerDTO beer;
}
