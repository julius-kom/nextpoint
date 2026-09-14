package com.julia.nextpoint.tests;

import com.julia.nextpoint.api.BookingApi;
import com.julia.nextpoint.api.TripApi;
import com.julia.nextpoint.config.BaseTest;
import com.julia.nextpoint.db.DatabaseClient;
import com.julia.nextpoint.models.BookingRequest;
import com.julia.nextpoint.models.TripRequest;
import com.julia.nextpoint.models.TripResponse;
import io.qameta.allure.Allure;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

public class TestBookingPastTrip extends BaseTest {

    private final TripApi tripApi = new TripApi();
    private final BookingApi bookingApi = new BookingApi();
    private final DatabaseClient databaseClient = new DatabaseClient();

    @Test
    @DisplayName("Booking a past trip returns 409")
    void testBookingPastTrip() throws SQLException {

        TripRequest tripRequest = new TripRequest(
                "Belgrade",
                "Budapest",
                50,
                LocalDate.now().plusDays(30).toString(),
                10
        );

        Allure.step("Create future trip through API");
        TripResponse createdTrip = tripApi.createTrip(tripRequest);

        Allure.step("Change trip departure date to past in database");
        databaseClient.updateTripDepartureDate(
                createdTrip.getId(),
                LocalDate.now().minusDays(1)
        );

        BookingRequest bookingRequest = new BookingRequest(createdTrip.getId(), 2);

        Allure.step("Try to book past trip");
        Response response = bookingApi.tryCreateBooking(bookingRequest);

        Allure.step("Check response status");
        assertThat(response.statusCode())
                .as("Booking a past trip should return 409.")
                .isEqualTo(409);
    }
}