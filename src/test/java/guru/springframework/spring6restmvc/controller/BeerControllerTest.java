package guru.springframework.spring6restmvc.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import guru.springframework.spring6restmvc.model.BeerDTO;
import guru.springframework.spring6restmvc.services.BeerService;
import guru.springframework.spring6restmvc.services.BeerServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.core.Is.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(BeerController.class)
class BeerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BeerService beerService;

    //ObjectMapper is used to create Java POJO to JSON and vice-versa. By autowiring ObjectMapper in Spring Context,
    //we get an ObjectMapper instance which is auto-configured by Spring with certain console display formats which we
    //don't have to do manually.
    @Autowired
    private ObjectMapper objectMapper;

    //Alternate method to create ArgumentCaptor, other way is shown in deleteBeerByIdTest() test
    @Captor
    private ArgumentCaptor<UUID> uuidArgumentCaptor;

    @Captor
    private ArgumentCaptor<BeerDTO> beerArgumentCaptor;

    BeerServiceImpl beerServiceImpl;

    @BeforeEach
    void setUp() {
        beerServiceImpl = new BeerServiceImpl();
    }

    @Test
    void getBeerById() throws Exception {
        BeerDTO testBeerDTO = beerServiceImpl.getBeers().get(0);

        //mockito is used here for mocking beerService and returning relevant data
        //given(beerService.getBeerById(any(UUID.class))).willReturn(testBeer);
        given(beerService.getBeerById(testBeerDTO.getId())).willReturn(testBeerDTO);

        //mockMvc is used here for mocking controller behaviour and assertions
        mockMvc.perform(get("/api/v1/beers/" + testBeerDTO.getId())
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(testBeerDTO.getId().toString())))
                .andExpect(jsonPath("$.beerName", is(testBeerDTO.getBeerName())));
    }

    @Test
    void getListBeers() throws Exception {
        given(beerService.getBeers()).willReturn(beerServiceImpl.getBeers());

        mockMvc.perform(get("/api/v1/beers"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()", is(3)));
    }

    @Test
    public void createBeerTest() throws Exception {
        BeerDTO beerDTO = beerServiceImpl.getBeers().get(0);
        beerDTO.setVersion(null);
        beerDTO.setId(null);

        given(beerService.saveNewBeer(any(BeerDTO.class))).willReturn(beerServiceImpl.getBeers().get(1));

        mockMvc.perform(post("/api/v1/beers")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(beerDTO)))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location-By-Id"));
    }

    @Test
    void testCreateBeerNullBeerName() throws Exception {

        BeerDTO beerDTO = BeerDTO.builder().build();

        given(beerService.saveNewBeer(any(BeerDTO.class))).willReturn(beerServiceImpl.getBeers().get(1));

        MvcResult mvcResult = mockMvc.perform(post("/api/v1/beers")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(beerDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.length()", is(5)))
                .andReturn();

        System.out.println(mvcResult.getResponse().getContentAsString());
    }

    @Test
    public void updateBeerTest() throws Exception {
        BeerDTO beerDTO = beerServiceImpl.getBeers().get(0);

        mockMvc.perform(put("/api/v1/beers/" + beerDTO.getId())
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(beerDTO)))
                .andExpect(status().isNoContent());

        verify(beerService, times(1)).updateBeerById(any(UUID.class), any(BeerDTO.class));
    }

    @Test
    public void deleteBeerByIdTest() throws Exception {
        BeerDTO beerDTO = beerServiceImpl.getBeers().get(0);

        mockMvc.perform(delete("/api/v1/beers/" + beerDTO.getId()))
                .andExpect(status().isNoContent());

        //ArgumentCaptor<UUID> uuidArgumentCaptor = ArgumentCaptor.forClass(UUID.class);
        verify(beerService, times(1)).deleteBeerById(uuidArgumentCaptor.capture());

        assertThat(beerDTO.getId()).isEqualTo(uuidArgumentCaptor.getValue());
    }

    @Test
    public void patchBeerByIdTest() throws Exception{
        BeerDTO beerDTO = beerServiceImpl.getBeers().get(0);

        Map<String, Object> beerMap = new HashMap<>();
        beerMap.put("beerName", "New Name");

        mockMvc.perform(patch("/api/v1/beers/" + beerDTO.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(beerMap)))
                .andExpect(status().isNoContent());

        verify(beerService, times(1)).modifyBeerById(uuidArgumentCaptor.capture(), beerArgumentCaptor.capture());
        assertThat(beerDTO.getId()).isEqualTo(uuidArgumentCaptor.getValue());
        assertThat(beerMap.get("beerName")).isEqualTo(beerArgumentCaptor.getValue().getBeerName());
    }
}