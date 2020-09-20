package com.djspring.springdataoverview;

import com.djspring.springdataoverview.entity.Flight;
import com.djspring.springdataoverview.repository.FlightRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static com.djspring.springdataoverview.DerivedQueryTests.createFlight;

@DataJpaTest
public class CustomImplTests {
    @Autowired
    private FlightRepository flightRepository;

    @Test
    public void shouldSaveCustomImpl(){
        final Flight toDelete = createFlight("Dehradun", "Hyderabad");
        final Flight toKeep = createFlight("New Delhi", "Hyderabad");

        flightRepository.save(toKeep);
        flightRepository.save(toDelete);
        flightRepository.deleteByOriginIs("Dehradun");

        Assertions.assertThat(flightRepository.findAll())
                .hasSize(1)
                .first()
                .isEqualToComparingFieldByField(toKeep);




    }

}
