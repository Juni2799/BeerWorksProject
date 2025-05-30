package guru.springframework.spring6restmvc.events;

import guru.springframework.spring6restmvc.entities.Beer;
import org.springframework.security.core.Authentication;

public interface BeerEvent {
    Beer getBeer();
    Authentication getAuthentication();
}
