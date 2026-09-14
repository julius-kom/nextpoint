package com.julia.nextpoint.controller;

import com.julia.nextpoint.model.Trip;
import com.julia.nextpoint.service.TripService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import jakarta.validation.Valid;

@RestController
public class TripController {

    private final TripService tripService;

    public TripController(TripService tripService) {
        this.tripService = tripService;
    }

    @GetMapping("/api/trips")
    public List<Trip> getTrips() {
        return tripService.getAllTrips();
    }

    @GetMapping("/api/trips/{id}")
    public ResponseEntity<Trip> getTripById(@PathVariable Long id) {
        Trip trip = tripService.getTripById(id);
        if (trip == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(trip);
    }

    @PostMapping("/api/trips")
    public ResponseEntity<Trip> createTrip(@Valid @RequestBody Trip trip) {
        Trip savedTrip = tripService.createTrip(trip);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedTrip);
    }

    @PutMapping("/api/trips/{id}")
    public ResponseEntity<Trip> updateTrip(
            @PathVariable Long id,
            @Valid @RequestBody Trip trip) {

        Trip updatedTrip = tripService.updateTrip(id, trip);

        return ResponseEntity.ok(updatedTrip);
    }
}
