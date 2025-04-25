package guru.springframework.spring6restmvc.listeners;

import guru.springframework.spring6restmvc.events.BeerCreatedEvent;
import guru.springframework.spring6restmvc.mappers.BeerMapper;
import guru.springframework.spring6restmvc.repository.BeerAuditRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class BeerCreatedListener {
    private final BeerAuditRepository beerAuditRepository;
    private final BeerMapper beerMapper;

    @EventListener
    public void listen(BeerCreatedEvent event){
        //Create the audit entry
        val beerAudit = beerMapper.beerToBeerAudit(event.getBeer());
        beerAudit.setAuditEventType("BEER_CREATED");

        if(event.getAuthentication() != null && event.getAuthentication().getName() != null){
            beerAudit.setPrincipalName(event.getAuthentication().getName());
        }

        val savedBeerAudit = beerAuditRepository.save(beerAudit);
        log.debug("Beer Audit Saved: " + savedBeerAudit.getId());
    }
}
