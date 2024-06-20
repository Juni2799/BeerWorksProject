package guru.springframework.spring6restmvc.entities;

import guru.springframework.spring6restmvc.model.BeerStyle;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Beer {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(length = 36, columnDefinition = "varchar", updatable = false, nullable = false)
    private UUID id;

    @Version
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
