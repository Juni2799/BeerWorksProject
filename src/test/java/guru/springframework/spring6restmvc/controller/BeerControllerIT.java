package guru.springframework.spring6restmvc.controller;

import guru.springframework.spring6restmvc.entities.Beer;
import guru.springframework.spring6restmvc.exceptions.NotFoundException;
import guru.springframework.spring6restmvc.mappers.BeerMapper;
import guru.springframework.spring6restmvc.model.BeerDTO;
import guru.springframework.spring6restmvc.model.BeerStyle;
import guru.springframework.spring6restmvc.repository.BeerRepository;
import org.hamcrest.core.IsNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.core.Is.is;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
class BeerControllerIT {
    @Autowired
    private BeerController beerController;

    @Autowired
    private BeerMapper beerMapper;

    @Autowired
    private BeerRepository beerRepository;

    @Autowired
    private WebApplicationContext wac;
    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(wac)
                .apply(springSecurity())
                .build();
    }

    @Test
    public void getListBeersTest(){
        Page<BeerDTO> beers = beerController.getBeers(null, null, false, 1, 2413);

        assertThat(beers.getContent().size()).isEqualTo(1000);
    }

    //Added a simple check for verifying that Spring Security Config is implemented or not
    @Test
    public void invalidAuthenticationTest() throws Exception {
        mockMvc.perform(get("/api/v1/beers"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void queryBeerListByNameTest() throws Exception {
        mockMvc.perform(get("/api/v1/beers")
                        .with(httpBasic("user1", "password"))
                        .queryParam("beerName", "IPA")
                        .queryParam("pageSize", "800"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.size()", is(336)));
    }

    @Test
    public void queryBeerListByBeerStyleTest() throws Exception {
        mockMvc.perform(get("/api/v1/beers")
                        .with(httpBasic("user1", "password"))
                        .queryParam("beerStyle", BeerStyle.IPA.name())
                        .queryParam("pageSize", "800"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.size()", is(549)));
    }

    @Test
    void tesListBeersByStyleAndNameShowInventoryTrue() throws Exception {
        mockMvc.perform(get("/api/v1/beers")
                        .with(httpBasic("user1", "password"))
                        .queryParam("beerName", "IPA")
                        .queryParam("beerStyle", BeerStyle.IPA.name())
                        .queryParam("showInventory", "true")
                        .queryParam("pageSize", "800"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.size()", is(310)))
                .andExpect(jsonPath("$.content[0].quantityOnHand").value(IsNull.notNullValue()));
    }

    @Test
    void tesListBeersByStyleAndNameShowInventoryFalse() throws Exception {
        mockMvc.perform(get("/api/v1/beers")
                        .with(httpBasic("user1", "password"))
                        .queryParam("beerName", "IPA")
                        .queryParam("beerStyle", BeerStyle.IPA.name())
                        .queryParam("showInventory", "false")
                        .queryParam("pageSize", "800"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.size()", is(310)))
                .andExpect(jsonPath("$.content[0].quantityOnHand").value(IsNull.nullValue()));
    }

    @Test
    void tesListBeersByStyleAndName() throws Exception {
        mockMvc.perform(get("/api/v1/beers")
                        .with(httpBasic("user1", "password"))
                        .queryParam("beerName", "IPA")
                        .queryParam("beerStyle", BeerStyle.IPA.name())
                        .queryParam("pageSize", "800"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.size()", is(310)));
    }

    @Test
    public void tesListBeersByStyleAndNameShowInventoryTruePage2() throws Exception {
        mockMvc.perform(get("/api/v1/beers")
                        .with(httpBasic("user1", "password"))
                        .queryParam("beerName", "IPA")
                        .queryParam("beerStyle", BeerStyle.IPA.name())
                        .queryParam("showInventory", "true")
                        .queryParam("pageNumber", "2")
                        .queryParam("pageSize", "50"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.size()", is(50)))
                .andExpect(jsonPath("$.content[0].quantityOnHand").value(IsNull.notNullValue()));
    }

    @Test
    //below 2 annotations are defined so that after the delete operation, Spring will roll back the database to original setup
    //for other tests to work correctly
    @Transactional
    @Rollback
    public void emptyListTest(){
        beerRepository.deleteAll();
        assertThat(beerController.getBeers(null, null, false, null, null).getContent().size()).isEqualTo(0);
    }

    @Test
    public void beerIdNotFoundExceptionTest(){
        assertThrows(NotFoundException.class, () -> beerController.getBeerById(UUID.randomUUID()));
    }

    @Test
    public void getBeerByIdTest(){
        Beer savedBeer = beerRepository.findAll().get(0);

        BeerDTO beerDTO = beerController.getBeerById(savedBeer.getId());

        assertThat(savedBeer.getId()).isEqualTo(beerDTO.getId());
        assertThat(savedBeer.getBeerName()).isEqualTo(beerDTO.getBeerName());
    }

    @Test
    @Transactional
    @Rollback
    public void deleteByIdTest(){
        Beer savedBeer = beerRepository.findAll().get(0);

        ResponseEntity responseEntity = beerController.deleteBeerById(savedBeer.getId());

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.valueOf(204));
        assertThat(beerRepository.findById(savedBeer.getId())).isEmpty();
    }

    @Test
    @Transactional
    @Rollback
    public void saveNewBeerTest(){
        BeerDTO beerDTO = BeerDTO.builder()
                .beerName("test beer")
                .build();

        ResponseEntity responseEntity = beerController.createNewBeer(beerDTO);

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.valueOf(201));
        assertThat(responseEntity.getHeaders().get("Location-By-Id")).isNotNull();

        String[] headerDetails = responseEntity.getHeaders().get("Location-By-Id").get(0).split("/");
        UUID savedUUID = UUID.fromString(headerDetails[4]);

        Beer savedNewBeer = beerRepository.findById(savedUUID).get();
        assertThat(savedNewBeer.getBeerName()).isEqualTo(beerDTO.getBeerName());
    }

    @Test
    @Transactional
    @Rollback
    public void updateBeerByIdTest(){
        Beer savedBeer = beerRepository.findAll().get(0);
        BeerDTO beerDTO = beerMapper.beerToBeerDTO(savedBeer);
        beerDTO.setId(null);
        beerDTO.setVersion(null);
        beerDTO.setBeerName("Updated Beer Name");

        ResponseEntity responseEntity = beerController.updateExistingBeer(savedBeer.getId(), beerDTO);

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(204));

        Beer updatedBeer = beerRepository.findById(savedBeer.getId()).get();
        assertThat(updatedBeer.getBeerName()).isEqualTo(beerDTO.getBeerName());
    }

    @Test
    public void updateBeerByIdNotFoundExceptionTest(){
        assertThrows(NotFoundException.class,() -> beerController.updateExistingBeer(UUID.randomUUID(), BeerDTO.builder().build()));
    }
}