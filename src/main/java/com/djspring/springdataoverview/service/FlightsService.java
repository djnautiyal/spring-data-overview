package com.djspring.springdataoverview.service;

import com.djspring.springdataoverview.entity.Flight;
import com.djspring.springdataoverview.repository.FlightRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
public class FlightsService {
    @Autowired
    private final FlightRepository flightRepository;

    public FlightsService(FlightRepository flightRepository) {
        this.flightRepository = flightRepository;
    }

    public void saveFlight(Flight flight){
        flightRepository.save(flight);
        throw new RuntimeException("saveFlight Failed");
    }

    @Transactional
    public void saveFlightTransactional(Flight flight){
        flightRepository.save(flight);
        throw new RuntimeException("saveFlight Failed");
    }
}
