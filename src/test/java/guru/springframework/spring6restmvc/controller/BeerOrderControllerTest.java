package guru.springframework.spring6restmvc.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;

//To-do: Finish the tests later for all CRUD operations

@SpringBootTest
class BeerOrderControllerTest {
    @Autowired
    private WebApplicationContext mvc;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(mvc)
                .apply(springSecurity())
                .build();
    }

    @Test
    void testGetBeerOrder(){

    }

    @Test
    void testGetBeerOrderById(){

    }
}