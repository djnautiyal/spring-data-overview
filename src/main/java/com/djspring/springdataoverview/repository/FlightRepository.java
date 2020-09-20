package com.djspring.springdataoverview.repository;

import com.djspring.springdataoverview.entity.Flight;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FlightRepository extends PagingAndSortingRepository<Flight, Long>, DeleteByOriginRepository {
    List<Flight> findByOriginIs(String origin);

    List<Flight> findByOriginAndDestinationIs(String origin, String destination);

    List<Flight> findByOriginIn(String... origins);
    List<Flight> findByOriginIgnoreCase(String origin);

    Page<Flight> findByOriginIs(String origin, Pageable pageRequest);

}
