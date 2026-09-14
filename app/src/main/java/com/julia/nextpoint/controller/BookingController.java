package com.julia.nextpoint.controller;

import com.julia.nextpoint.model.Booking;
import com.julia.nextpoint.model.CreateBookingRequest;
import com.julia.nextpoint.service.BookingService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
public class BookingController {

    private final BookingService bookingService;

    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @PostMapping("/api/bookings")
    public ResponseEntity<Booking> createBooking(@RequestBody CreateBookingRequest request) {
        Booking savedBooking = bookingService.createBooking(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(savedBooking);
    }

    @GetMapping("/api/bookings/{id}")
    public ResponseEntity<Booking> getBookingById(@PathVariable Long id) {
        return ResponseEntity.ok(bookingService.getBookingById(id));
    }

    @PatchMapping("/api/bookings/{id}/cancel")
    public ResponseEntity<Booking> cancelBooking(@PathVariable Long id) {
        return ResponseEntity.ok(bookingService.cancelBooking(id));
    }

}