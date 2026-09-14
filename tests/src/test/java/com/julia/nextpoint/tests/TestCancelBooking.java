package com.julia.nextpoint.tests;

import com.julia.nextpoint.api.BookingApi;
import com.julia.nextpoint.api.TripApi;
import com.julia.nextpoint.config.BaseTest;
import com.julia.nextpoint.models.BookingRequest;
import com.julia.nextpoint.models.BookingResponse;
import com.julia.nextpoint.models.TripRequest;
import com.julia.nextpoint.models.TripResponse;
import com.julia.nextpoint.wiremock.PaymentStub;
import io.qameta.allure.Allure;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

public class TestCancelBooking extends BaseTest {

    private final TripApi tripApi = new TripApi();
    private final BookingApi bookingApi = new BookingApi();
    private final PaymentStub paymentStub = new PaymentStub();


    @Test
    @DisplayName("Cancel booking and restore available seats")
    void testCancelBooking() {

        paymentStub.stubSuccessfulPayment();

        Allure.step("Create trip data");
        TripRequest tripRequest = new TripRequest(
                "Belgrade",
                "Budapest",
                50,
                LocalDate.now().plusDays(30).toString(),
                10
        );

        TripResponse trip = tripApi.createTrip(tripRequest);

        Allure.step("Create booking");
        BookingResponse booking = bookingApi.createBooking(
                new BookingRequest(trip.getId(), 3)
        );

        Allure.step("Check number of available seats after booking");
        TripResponse tripAfterBooking = tripApi.getTrip(trip.getId());

        assertThat(tripAfterBooking.getAvailableSeats())
                .as("The number of available seats after booking is incorrect.")
                .isEqualTo(7);

        Allure.step("Cancel booking");
        BookingResponse cancelledBooking =
                bookingApi.cancelBooking(booking.getId());

        Allure.step("Check booking status after cancellation");
        assertThat(cancelledBooking.getStatus())
                .as("Booking status after cancellation is incorrect.")
                .isEqualTo("CANCELLED");

        Allure.step("Check number of available seats after cancellation");
        TripResponse tripAfterCancellation = tripApi.getTrip(trip.getId());

        assertThat(tripAfterCancellation.getAvailableSeats())
                .as("The number of available seats was not restored after cancellation.")
                .isEqualTo(10);

        Allure.step("Cancel booking again");
        BookingResponse cancelledAgain =
                bookingApi.cancelBooking(booking.getId());

        Allure.step("Check repeated cancellation");
        TripResponse tripAfterSecondCancellation =
                tripApi.getTrip(trip.getId());

        assertThat(cancelledAgain.getStatus())
                .as("Booking status after repeated cancellation is incorrect.")
                .isEqualTo("CANCELLED");

        assertThat(tripAfterSecondCancellation.getAvailableSeats())
                .as("The number of available seats changed after repeated cancellation.")
                .isEqualTo(10);
    }
}