package guru.springframework.spring6restmvc.entities;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(length = 36, columnDefinition = "varchar(36)", updatable = false, nullable = false )
    private UUID id;

    private String description;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdDate;

    @UpdateTimestamp
    private LocalDateTime lastModifiedDate;

    @Version
    private Long version;

    @Builder.Default
    @ManyToMany
    @JoinTable(name = "beer_category",
            joinColumns = @JoinColumn(name = "category_id"),
            inverseJoinColumns = @JoinColumn(name = "beer_id"))
    private Set<Beer> beers  = new HashSet<>();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Category category)) return false;

        if (getId() != null ? !getId().equals(category.getId()) : category.getId() != null) return false;
        if (getDescription() != null ? !getDescription().equals(category.getDescription()) : category.getDescription() != null)
            return false;
        if (getCreatedDate() != null ? !getCreatedDate().equals(category.getCreatedDate()) : category.getCreatedDate() != null)
            return false;
        if (getLastModifiedDate() != null ? !getLastModifiedDate().equals(category.getLastModifiedDate()) : category.getLastModifiedDate() != null)
            return false;
        if (getVersion() != null ? !getVersion().equals(category.getVersion()) : category.getVersion() != null)
            return false;
        if (getBeers() != null ? !getBeers().equals(category.getBeers()) : category.getBeers() != null) return false;
        return getBeers() == null || getBeers().equals(category.getBeers());
    }

    @Override
    public int hashCode() {
        int result = getId() != null ? getId().hashCode() : 0;
        result = 31 * result + (getDescription() != null ? getDescription().hashCode() : 0);
        result = 31 * result + (getCreatedDate() != null ? getCreatedDate().hashCode() : 0);
        result = 31 * result + (getLastModifiedDate() != null ? getLastModifiedDate().hashCode() : 0);
        result = 31 * result + (getVersion() != null ? getVersion().hashCode() : 0);
        result = 31 * result + (getBeers() != null ? getBeers().hashCode() : 0);
        result = 31 * result + (getBeers() != null ? getBeers().hashCode() : 0);
        return result;
    }
}
