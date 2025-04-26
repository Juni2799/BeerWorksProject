package guru.springframework.spring6restmvc.model;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@Builder
@Data
public class BeerOrderDTO {
    private UUID id;
    private Long version;
    private LocalDateTime createdDate;
    private LocalDateTime lastModifiedDate;
    @NotNull(message = "Customer ref is mandatory")
    private String customerRef;
    private CustomerDTO customer;
    private Set<BeerOrderLineDTO> beerOrderLines;
    private BeerOrderShipmentDTO beerOrderShipment;
}
