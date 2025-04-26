package guru.springframework.spring6restmvc.model;

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
    private Integer orderQuantity;
    private Integer quantityAllocated;
    private BeerDTO beer;
}
