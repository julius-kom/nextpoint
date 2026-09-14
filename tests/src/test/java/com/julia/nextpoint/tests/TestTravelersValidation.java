package com.julia.nextpoint.tests;

import com.julia.nextpoint.api.BookingApi;
import com.julia.nextpoint.api.TripApi;
import com.julia.nextpoint.config.BaseTest;
import com.julia.nextpoint.models.BookingRequest;
import com.julia.nextpoint.models.TripRequest;
import com.julia.nextpoint.models.TripResponse;
import io.qameta.allure.Allure;
import io.restassured.response.Response;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

public class TestTravelersValidation extends BaseTest {

    private final TripApi tripApi = new TripApi();
    private final BookingApi bookingApi = new BookingApi();

    @ParameterizedTest(name = "Booking with {0} travelers")
    @ValueSource(ints = {0, -1})
    void testTravelersValidation(int travelers) {

        Allure.step("Create trip data");
        TripRequest tripRequest = new TripRequest(
                "Belgrade",
                "Budapest",
                50,
                LocalDate.now().plusDays(30).toString(),
                10
        );

        TripResponse createdTrip = tripApi.createTrip(tripRequest);

        Allure.step("Try to create booking with invalid number of travelers");
        BookingRequest bookingRequest = new BookingRequest(createdTrip.getId(), travelers);

        Response response = bookingApi.tryCreateBooking(bookingRequest);

        Allure.step("Check booking error");
        assertThat(response.statusCode())
                .as("Status code for booking with invalid number of travelers")
                .isEqualTo(400);

        Allure.step("Check number of available seats");
        TripResponse tripAfterFailedBooking =
                tripApi.getTrip(createdTrip.getId());

        assertThat(tripAfterFailedBooking.getAvailableSeats())
                .as("The number of available seats changed after failed booking.")
                .isEqualTo(10);
    }
}