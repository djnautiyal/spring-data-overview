package com.djspring.springdataoverview;

import com.djspring.springdataoverview.entity.Flight;
import com.djspring.springdataoverview.repository.FlightRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@DataJpaTest
public class DerivedQueryTests {

    @Autowired
    private FlightRepository flightRepository;

    private List<Flight> flights;

    @BeforeEach
    public void setUp(){
        flightRepository.deleteAll();
        flights = new ArrayList<>(List.of(
                createFlight("Dehradun", "Hyderabad"),
                createFlight("Dehradun", "Kathmandu"),
                createFlight("Jalandhar", "Hyderabad"),
                createFlight("Dehradun", "Hyderabad"),
                createFlight("Dehradun", "New Delhi"),
                createFlight("New Delhi", "Hyderabad"),
                createFlight("Dehradun", "Hyderabad")
                ));

        flights.forEach(flight -> flightRepository.save(flight));
    }

    @Test
    public void shouldFindFlightsFromDehradun(){
        List<Flight> flightList = flightRepository.findByOriginIs("Dehradun");

        Assertions.assertThat(flightList).hasSize(5);
        Assertions.assertThat(flightList.get(0)).isEqualTo(flights.get(0));
        Assertions.assertThat(flightList.get(1)).isEqualTo(flights.get(1));
        Assertions.assertThat(flightList.get(2)).isEqualTo(flights.get(3));
        Assertions.assertThat(flightList.get(3)).isEqualTo(flights.get(4));
        Assertions.assertThat(flightList.get(4)).isEqualTo(flights.get(6));
    }

    @Test
    public void shouldFindFlightsFromDehradunToHyderabad(){
        List<Flight> flightList = flightRepository.findByOriginAndDestinationIs("Dehradun", "Hyderabad");

        Assertions.assertThat(flightList).hasSize(3);
        Assertions.assertThat(flightList.get(0)).isEqualTo(flights.get(0));
        Assertions.assertThat(flightList.get(1)).isEqualTo(flights.get(3));
        Assertions.assertThat(flightList.get(2)).isEqualTo(flights.get(6));

    }

    @Test
    public void shouldFindFlightsFromNewDelhiOrJalandhar(){
        List<Flight> flightList = flightRepository.findByOriginIn("Jalandhar","New Delhi");

        Assertions.assertThat(flightList).hasSize(2);
        Assertions.assertThat(flightList.get(0)).isEqualTo(flights.get(2));
        Assertions.assertThat(flightList.get(1)).isEqualTo(flights.get(5));
    }

    @Test
    public void shouldFindFlightsFromDehradunIgnoringCase(){
        List<Flight> flightList = flightRepository.findByOriginIgnoreCase("DEHRADUN");

        Assertions.assertThat(flightList).hasSize(5);
        Assertions.assertThat(flightList.get(0)).isEqualTo(flights.get(0));
        Assertions.assertThat(flightList.get(1)).isEqualTo(flights.get(1));
        Assertions.assertThat(flightList.get(2)).isEqualTo(flights.get(3));
        Assertions.assertThat(flightList.get(3)).isEqualTo(flights.get(4));
        Assertions.assertThat(flightList.get(4)).isEqualTo(flights.get(6));
    }

    static Flight createFlight(String origin, String destination){
        Flight flight = new Flight();
        flight.setOrigin(origin);
        flight.setDestination(destination);
        flight.setScheduledAt(LocalDateTime.parse("2020-12-04T12:12:00"));

        return flight;
    }
}
