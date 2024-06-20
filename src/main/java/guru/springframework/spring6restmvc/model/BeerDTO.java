package guru.springframework.spring6restmvc.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Created by jt, Spring Framework Guru.
 */
@Builder
@Data
public class BeerDTO {
    private UUID id;
    private Integer version;

    @NotBlank(message = "Name is mandatory")
    @NotNull(message = "Name cannot be null")
    private String beerName;
    @NotNull(message = "Style is mandatory")
    private BeerStyle beerStyle;
    @NotBlank(message = "Upc is mandatory")
    private String upc;
    private Integer quantityOnHand;
    @NotNull(message = "Price is mandatory")
    private BigDecimal price;
    private LocalDateTime createdDate;
    private LocalDateTime updateDate;
}
