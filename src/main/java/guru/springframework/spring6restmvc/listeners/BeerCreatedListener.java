package guru.springframework.spring6restmvc.listeners;

import guru.springframework.spring6restmvc.events.*;
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
    public void listen(BeerEvent event){
        //Create the audit entry
        val beerAudit = beerMapper.beerToBeerAudit(event.getBeer());

        String eventType = null;

        if (event instanceof BeerCreatedEvent) {
            eventType = "BEER_CREATED";
        } else if (event instanceof BeerUpdatedEvent) {
            eventType = "BEER_UPDATED";
        } else if (event instanceof BeerPatchedEvent) {
            eventType = "BEER_PATCHED";
        } else if (event instanceof BeerDeletedEvent) {
            eventType = "BEER_DELETED";
        }else{
            eventType = "UNKNOWN";
        }

        beerAudit.setAuditEventType(eventType);

        if(event.getAuthentication() != null && event.getAuthentication().getName() != null){
            beerAudit.setPrincipalName(event.getAuthentication().getName());
        }

        val savedBeerAudit = beerAuditRepository.save(beerAudit);
        log.debug("Beer Audit Saved: " + savedBeerAudit.getId());
    }
}
