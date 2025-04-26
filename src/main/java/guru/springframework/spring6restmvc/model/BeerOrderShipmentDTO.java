package guru.springframework.spring6restmvc.model;

import guru.springframework.spring6restmvc.entities.BeerOrder;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@Builder
@Data
public class BeerOrderShipmentDTO {
    private UUID id;
    private Long version;
    @NotBlank
    private String trackingNumber;
    private LocalDateTime createdDate;
    private LocalDateTime lastModifiedDate;
}
