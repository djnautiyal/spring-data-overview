package com.djspring.springdataoverview;

import com.djspring.springdataoverview.entity.Flight;
import com.djspring.springdataoverview.repository.FlightRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import static com.djspring.springdataoverview.DerivedQueryTests.createFlight;
import static org.springframework.data.domain.Sort.Direction.DESC;

@DataJpaTest
public class PagingAndSortingTests {

    @Autowired
    private FlightRepository flightRepository;

    private List<Flight> flights;
    LocalDateTime now;

    @BeforeEach
    public void setUp(){
        flightRepository.deleteAll();
        flights = new ArrayList<>(List.of(
                createFlight("Dehradun", "Hyderabad"),
                createFlight("Dehradun", "Kathmandu"),
                createFlight("Jalandhar", "New Delhi"),
                createFlight("Jalandhar", "Hyderabad"),
                createFlight("Dehradun", "New Delhi"),
                createFlight("New Delhi", "Hyderabad"),
                createFlight("New Delhi", "Hyderabad")
        ));
        now = LocalDateTime.now();

        int i=0;
        for(Flight flight: flights){
            flight.setScheduledAt(now.plusHours(i));
            i++;
        }

        flights.forEach(flight -> flightRepository.save(flight));
    }


    @Test
    public void shouldSortFlightsByDestination(){
        final Iterable<Flight> flightList = flightRepository.findAll(Sort.by("destination"));

        final Iterator<Flight> iterator = flightList.iterator();
        Assertions.assertThat(iterator.next().getDestination()).isEqualTo("Hyderabad");
        Assertions.assertThat(iterator.next().getDestination()).isEqualTo("Hyderabad");
        Assertions.assertThat(iterator.next().getDestination()).isEqualTo("Hyderabad");
        Assertions.assertThat(iterator.next().getDestination()).isEqualTo("Hyderabad");
        Assertions.assertThat(iterator.next().getDestination()).isEqualTo("Kathmandu");
        Assertions.assertThat(iterator.next().getDestination()).isEqualTo("New Delhi");
        Assertions.assertThat(iterator.next().getDestination()).isEqualTo("New Delhi");
    }

    @Test
    public void shouldSortFlightsByScheduledAndThenByName(){
        final Iterator<Flight> iterator = flightRepository.findAll(Sort.by("destination", "scheduledAt")).iterator();

        Assertions.assertThat(iterator.next().getScheduledAt()).isEqualTo(now.plusHours(0));
        Assertions.assertThat(iterator.next().getScheduledAt()).isEqualTo(now.plusHours(3));
        Assertions.assertThat(iterator.next().getScheduledAt()).isEqualTo(now.plusHours(5));
        Assertions.assertThat(iterator.next().getScheduledAt()).isEqualTo(now.plusHours(6));
        Assertions.assertThat(iterator.next().getScheduledAt()).isEqualTo(now.plusHours(1));
        Assertions.assertThat(iterator.next().getScheduledAt()).isEqualTo(now.plusHours(2));
        Assertions.assertThat(iterator.next().getScheduledAt()).isEqualTo(now.plusHours(4));

    }

    @Test
    public void shouldPageResults(){
        flightRepository.deleteAll();

        for(int i=0; i<50 ; i++)
            flightRepository.save(createFlight(String.valueOf(i), String.valueOf(i+1)));

        final Page<Flight> page = flightRepository.findAll(PageRequest.of(2, 5));

        Assertions.assertThat(page.getTotalElements()).isEqualTo(50);
        Assertions.assertThat(page.getNumberOfElements()).isEqualTo(5);
        Assertions.assertThat(page.getTotalPages()).isEqualTo(10);

        Assertions.assertThat(page.getContent())
                .extracting(Flight::getDestination)
                .containsExactly("11", "12", "13", "14", "15");

    }

    @Test
    public void shouldPageAndSortResults(){
        flightRepository.deleteAll();

        for(int i=0; i<50 ; i++)
            flightRepository.save(createFlight(String.valueOf(i), String.valueOf(i+1)));

        final Page<Flight> page = flightRepository.findAll(PageRequest.of(2, 5,Sort.by(DESC, "destination")));

        Assertions.assertThat(page.getTotalElements()).isEqualTo(50);
        Assertions.assertThat(page.getNumberOfElements()).isEqualTo(5);
        Assertions.assertThat(page.getTotalPages()).isEqualTo(10);

        Assertions.assertThat(page.getContent())
                .extracting(Flight::getDestination)
                .containsExactly("45", "44", "43", "42", "41");

    }

    @Test
    public void shouldPageAndSortADerivedQuery(){
        flightRepository.deleteAll();

        for(int i=0; i<10 ; i++)
            flightRepository.save(createFlight("Dehradun", String.valueOf(i)));

        for(int i=0; i<10 ; i++)
            flightRepository.save(createFlight("New Delhi", String.valueOf(i)));


        final Page<Flight> page = flightRepository
                .findByOriginIs("Dehradun",
                        PageRequest.of(0,5,Sort.by(DESC, "destination")));

        Assertions.assertThat(page.getTotalElements()).isEqualTo(10);
        Assertions.assertThat(page.getNumberOfElements()).isEqualTo(5);
        Assertions.assertThat(page.getTotalPages()).isEqualTo(2);

        Assertions.assertThat(page.getContent())
                .extracting(Flight::getDestination)
                .containsExactly("9", "8", "7", "6", "5");

    }
}
