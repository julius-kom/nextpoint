package com.julia.nextpoint.tests;

import com.julia.nextpoint.api.BookingApi;
import com.julia.nextpoint.api.TripApi;
import com.julia.nextpoint.config.BaseTest;
import com.julia.nextpoint.models.BookingRequest;
import com.julia.nextpoint.models.TripRequest;
import com.julia.nextpoint.models.TripResponse;
import io.qameta.allure.Allure;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

public class TestNotEnoughSeats extends BaseTest {

    private final TripApi tripApi = new TripApi();
    private final BookingApi bookingApi = new BookingApi();

    @Test
    @DisplayName("Booking fails when there are not enough available seats")
    void testNotEnoughSeats() {

        Allure.step("Create trip data");
        TripRequest tripRequest = new TripRequest(
                "Belgrade",
                "Budapest",
                50,
                LocalDate.now().plusDays(30).toString(),
                2
        );

        TripResponse createdTrip = tripApi.createTrip(tripRequest);

        Allure.step("Try to book more seats than available");
        BookingRequest bookingRequest = new BookingRequest(createdTrip.getId(), 3);

        Response response = bookingApi.tryCreateBooking(bookingRequest);

        Allure.step("Check booking error");
        assertThat(response.statusCode())
                .as("Status code for booking with not enough seats")
                .isEqualTo(409);

        Allure.step("Check number of available seats");
        TripResponse tripAfterFailedBooking = tripApi.getTrip(createdTrip.getId());

        assertThat(tripAfterFailedBooking.getAvailableSeats())
                .as("The number of available seats changed after failed booking.")
                .isEqualTo(2);
    }
}