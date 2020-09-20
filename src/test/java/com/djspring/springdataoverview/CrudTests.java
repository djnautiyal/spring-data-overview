package com.djspring.springdataoverview;

import com.djspring.springdataoverview.entity.Flight;
import com.djspring.springdataoverview.repository.FlightRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;

//@RunWith(SpringRunner.class)
@DataJpaTest
public class CrudTests {

    @Autowired
    private FlightRepository flightRepository;

    @Test
    public void ShouldPerformCrudOperation(){
        final Flight flight = new Flight();
        flight.setOrigin("Dehradun");
        flight.setDestination("Hyderabad");;
        flight.setScheduledAt(LocalDateTime.parse("2020-12-04T12:12:00"));

        flightRepository.save(flight);

        Assertions.assertThat(flightRepository.findAll())
                .hasSize(1)
                .first()
                .isEqualToComparingFieldByField(flight);

        flightRepository.deleteById(flight.getId());

        Assertions.assertThat(flightRepository.count())
                .isZero();
    }
}
