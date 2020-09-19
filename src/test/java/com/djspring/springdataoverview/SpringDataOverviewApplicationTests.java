package com.djspring.springdataoverview;

import com.djspring.springdataoverview.entity.Flight;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.time.LocalDateTime;
import java.util.List;

@DataJpaTest
class SpringDataOverviewApplicationTests {

	@Autowired
	private EntityManager entityManager;

	@Test
	public void verifyFlightsCanBeSaved() {
		final Flight flight = new Flight();
		flight.setOrigin("Dehradun");
		flight.setDestination("Hyderabad");;
		flight.setScheduledAt(LocalDateTime.parse("2020-12-04T12:12:00"));

		entityManager.persist(flight);

		final TypedQuery<Flight> results = entityManager.createQuery("SELECT f FROM Flight f", Flight.class);

		final List<Flight> flightList =  results.getResultList();

		Assertions.assertThat(flightList).hasSize(1).first().isEqualTo(flight);

	}

}
