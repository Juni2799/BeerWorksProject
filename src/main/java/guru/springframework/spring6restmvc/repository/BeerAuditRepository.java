package guru.springframework.spring6restmvc.repository;

import guru.springframework.spring6restmvc.entities.BeerAudit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface BeerAuditRepository extends JpaRepository<BeerAudit, UUID> {
}
