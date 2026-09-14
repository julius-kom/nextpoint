package com.julia.nextpoint.service;

import com.julia.nextpoint.model.Trip;
import com.julia.nextpoint.repository.TripRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class TripService {

    private final TripRepository tripRepository;

    public TripService(TripRepository tripRepository) {
        this.tripRepository = tripRepository;
    }

    public List<Trip> getAllTrips() {
        return tripRepository.findAll();
    }

    public Trip getTripById(Long id) {
        return tripRepository.findById(id).orElse(null);
    }

    public Trip createTrip(Trip trip) {
        return tripRepository.save(trip);
    }

    public Trip updateTrip(Long id, Trip updatedTrip) {

        Trip trip = tripRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Trip not found"
                ));

        trip.setFrom(updatedTrip.getFrom());
        trip.setTo(updatedTrip.getTo());
        trip.setPrice(updatedTrip.getPrice());
        trip.setDepartureDate(updatedTrip.getDepartureDate());
        trip.setAvailableSeats(updatedTrip.getAvailableSeats());

        return tripRepository.save(trip);
    }
}