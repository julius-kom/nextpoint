package com.julia.nextpoint.service;

import com.julia.nextpoint.client.PaymentClient;
import com.julia.nextpoint.event.BookingCreatedEvent;
import com.julia.nextpoint.event.BookingEventPublisher;
import com.julia.nextpoint.model.Booking;
import com.julia.nextpoint.model.BookingStatus;
import com.julia.nextpoint.model.CreateBookingRequest;
import com.julia.nextpoint.model.Trip;
import com.julia.nextpoint.repository.BookingRepository;
import com.julia.nextpoint.repository.TripRepository;
import org.springframework.stereotype.Service;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;

@Service
public class BookingService {

    private final BookingRepository bookingRepository;
    private final TripRepository tripRepository;
    private final PaymentClient paymentClient;
    private final BookingEventPublisher bookingEventPublisher;

    public BookingService(BookingRepository bookingRepository,
                          TripRepository tripRepository,
                          PaymentClient paymentClient,
                          BookingEventPublisher bookingEventPublisher) {
        this.bookingRepository = bookingRepository;
        this.tripRepository = tripRepository;
        this.paymentClient = paymentClient;
        this.bookingEventPublisher = bookingEventPublisher;
    }

    @Transactional //transaction will roll back if an exception is thrown
    public Booking createBooking(CreateBookingRequest request) {

        Trip trip = tripRepository.findById(request.getTripId())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Trip not found"
                ));

        // Проверяем дату поездки
        if (trip.getDepartureDate() != null &&
                trip.getDepartureDate().isBefore(LocalDate.now())) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Cannot book a past trip"
            );
        }

        // Проверяем количество путешественников
        if (request.getTravelers() <= 0) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Travelers must be greater than zero"
            );
        }

        // Проверяем наличие мест
        if (trip.getAvailableSeats() < request.getTravelers()) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Not enough available seats"
            );
        }

        int totalPrice = trip.getPrice() * request.getTravelers();

        Booking booking = new Booking(
                request.getTripId(),
                request.getTravelers(),
                totalPrice
        );

        int remainingSeats = trip.getAvailableSeats() - request.getTravelers();

        trip.setAvailableSeats(remainingSeats);
        tripRepository.save(trip);

        Booking savedBooking = bookingRepository.save(booking);

        boolean paymentSuccessful = paymentClient.pay(savedBooking.getId(), totalPrice);

        if (paymentSuccessful) {
            savedBooking.setStatus(BookingStatus.CONFIRMED);
            bookingEventPublisher.publishBookingCreated(
                    new BookingCreatedEvent(
                            savedBooking.getId(),
                            savedBooking.getTripId(),
                            savedBooking.getTravelers(),
                            savedBooking.getTotalPrice())
                    );
        } else {
            savedBooking.setStatus(BookingStatus.PAYMENT_FAILED);

            trip.setAvailableSeats(
                    trip.getAvailableSeats() + request.getTravelers()
            );

            tripRepository.save(trip);
        }

        return bookingRepository.save(savedBooking);
    }

    public Booking getBookingById(Long id) {
        return bookingRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Booking not found"
                ));
    }

    @Transactional
    public Booking cancelBooking(Long id) {
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Booking not found"
                ));
        if (booking.getStatus() == BookingStatus.CANCELLED) {
            return booking;
        }
        if (booking.getStatus() != BookingStatus.CONFIRMED) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Only confirmed booking can be cancelled");
        }
        Trip trip = tripRepository.findById(booking.getTripId())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Trip not found"));
        trip.setAvailableSeats(trip.getAvailableSeats() + booking.getTravelers());
        booking.setStatus(BookingStatus.CANCELLED);
        tripRepository.save(trip);
        return bookingRepository.save(booking);
    }
}