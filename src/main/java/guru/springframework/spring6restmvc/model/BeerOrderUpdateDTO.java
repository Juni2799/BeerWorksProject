package guru.springframework.spring6restmvc.model;

import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.Set;
import java.util.UUID;

@Data
@Builder
public class BeerOrderUpdateDTO {
    @NotNull(message = "Customer ref is mandatory")
    private String customerRef;

    @NotNull
    private UUID customerId;

    private Set<BeerOrderLineUpdateDTO> beerOrderLines;

    private BeerOrderShipmentUpdateDTO beerOrderShipment;
}
