package guru.springframework.spring6restmvc.services;

import guru.springframework.spring6restmvc.model.BeerCsvRecord;
import org.junit.jupiter.api.Test;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class BeerCsvServiceImplTest {
    BeerCsvServiceImpl beerCsvService = new BeerCsvServiceImpl();

    @Test
    public void convertCsvTest() throws FileNotFoundException {
        File file = ResourceUtils.getFile("classpath:csvdata/beers.csv");
        List<BeerCsvRecord> beerCsvRecords = beerCsvService.convertCSV(file);

        System.out.println(beerCsvRecords.size());
        assertThat(beerCsvRecords.size()).isGreaterThan(0);
    }
}
