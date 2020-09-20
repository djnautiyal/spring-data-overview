package com.djspring.springdataoverview;

import com.djspring.springdataoverview.repository.FlightRepository;
import com.djspring.springdataoverview.service.FlightsService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static com.djspring.springdataoverview.DerivedQueryTests.createFlight;

@SpringBootTest
public class TransactionalTests {
    @Autowired
    private FlightRepository flightRepository;

    @Autowired
    private FlightsService flightsService;

    @BeforeEach
    public void setUp(){
        flightRepository.deleteAll();
    }

    @Test
    public void shouldNotRollBackWhenThereIsNoTransaction(){
        try{
            flightsService.saveFlight(createFlight("Dehradun", "Mumbai"));
        }catch(Exception e){

        }finally {
            Assertions.assertThat(flightRepository.findAll())
                    .isNotEmpty();
        }
    }

    @Test
    public void shouldNotRollBackWhenThereIsATransaction(){
        try{
            flightsService.saveFlightTransactional(createFlight("Dehradun", "Mumbai"));
        }catch(Exception e){

        }finally {
            Assertions.assertThat(flightRepository.findAll())
                    .isEmpty();
        }
    }
}
