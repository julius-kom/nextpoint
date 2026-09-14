package com.julia.nextpoint.tests;

import com.julia.nextpoint.api.BookingApi;
import com.julia.nextpoint.api.TripApi;
import com.julia.nextpoint.config.BaseTest;
import com.julia.nextpoint.db.DatabaseClient;
import com.julia.nextpoint.models.BookingRequest;
import com.julia.nextpoint.models.BookingResponse;
import com.julia.nextpoint.models.TripRequest;
import com.julia.nextpoint.models.TripResponse;
import com.julia.nextpoint.wiremock.PaymentStub;
import io.qameta.allure.Allure;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

public class TestBookingDatabase extends BaseTest {

    private final TripApi tripApi = new TripApi();
    private final BookingApi bookingApi = new BookingApi();
    private final DatabaseClient databaseClient = new DatabaseClient();
    private final PaymentStub paymentStub = new PaymentStub();

    @Test
    @DisplayName("Created booking is saved in database")
    void testBookingDatabase() throws SQLException {

        paymentStub.stubSuccessfulPayment();
        String departureDate = LocalDate.now().plusDays(30).toString();

        TripRequest tripRequest = new TripRequest(
                "Belgrade",
                "Budapest",
                50,
                departureDate,
                10
        );

        Allure.step("Create trip through API");
        TripResponse createdTrip = tripApi.createTrip(tripRequest);

        BookingRequest bookingRequest = new BookingRequest(
                createdTrip.getId(),
                3
        );

        Allure.step("Create booking through API");
        BookingResponse createdBooking = bookingApi.createBooking(bookingRequest);

        Allure.step("Get booking directly from database");
        BookingResponse bookingFromDb = databaseClient.getBookingById(createdBooking.getId());

        Allure.step("Check booking data in database");
        assertThat(bookingFromDb).as("Booking was not found in database.").isNotNull();

        assertThat(bookingFromDb.getId())
                .as("Booking id in database is incorrect.")
                .isEqualTo(createdBooking.getId());
        assertThat(bookingFromDb.getTripId())
                .as("Trip id in database is incorrect.")
                .isEqualTo(createdTrip.getId());
        assertThat(bookingFromDb.getTravelers())
                .as("Number of travelers in database is incorrect.")
                .isEqualTo(3);
        assertThat(bookingFromDb.getTotalPrice())
                .as("Total price in database is incorrect.")
                .isEqualTo(150);
        assertThat(bookingFromDb.getStatus())
                .as("Booking status in database is incorrect.")
                .isEqualTo("CONFIRMED");

        Allure.step("Get trip directly from database");
        TripResponse tripFromDb = databaseClient.getTripById(createdTrip.getId());

        Allure.step("Check available seats in database");
        assertThat(tripFromDb.getAvailableSeats())
                .as("Available seats were not decreased after booking.")
                .isEqualTo(7);
    }
}