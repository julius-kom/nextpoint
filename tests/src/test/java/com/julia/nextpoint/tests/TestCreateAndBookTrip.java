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

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.hamcrest.Matchers.equalTo;

public class TestCreateAndBookTrip extends BaseTest {

    private final TripApi tripApi = new TripApi();
    private final BookingApi bookingApi = new BookingApi();
    private final PaymentStub paymentStub = new PaymentStub();

    @Test
    @DisplayName("Create trip and book it successfully")
    void testCreateAndBookTrip() {

        Allure.step("Configure successful payment");
        paymentStub.stubSuccessfulPayment();

        String departureDate = LocalDate.now().plusDays(30).toString();

        TripRequest tripRequest = new TripRequest(
                "Belgrade",
                "Zagreb",
                60,
                departureDate,
                10
        );

        Allure.step("Create trip data");
        TripResponse createdTrip = tripApi.createTrip(tripRequest);

        TripResponse savedTrip = tripApi.getTrip(createdTrip.getId());

        Allure.step("Check saved trip data");
        assertThat(savedTrip.getFrom())
                .as("The departure city is incorrect.")
                .isEqualTo("Belgrade");
        assertThat(savedTrip.getTo())
                .as("The destination city is incorrect.")
                .isEqualTo("Zagreb");
        assertThat(savedTrip.getPrice())
                .as("The trip price is incorrect.")
                .isEqualTo(60);
        assertThat(savedTrip.getDepartureDate())
                .as("The departure date is incorrect.")
                .isEqualTo(departureDate);
        assertThat(savedTrip.getAvailableSeats())
                .as("The number of available seats is incorrect.")
                .isEqualTo(10);

        Allure.step("Create booking");
        BookingRequest bookingRequest = new BookingRequest(createdTrip.getId(), 3);

        BookingResponse createdBooking = bookingApi.createBooking(bookingRequest);

        Allure.step("Verify request sent to Payment Service");
        paymentStub.verifyPaymentRequest(
                createdBooking.getId(),
                createdBooking.getTotalPrice()
        );

        assertThat(createdBooking.getTripId())
                .as("The trip id is incorrect.").isEqualTo(createdTrip.getId());
        assertThat(createdBooking.getTravelers())
                .as("The number of travelers is incorrect.").isEqualTo(3);
        assertThat(createdBooking.getTotalPrice())
                .as("The total booking price is incorrect.").isEqualTo(180);
        assertThat(createdBooking.getStatus())
                .as("The booking status is incorrect.").isEqualTo("CONFIRMED");

        BookingResponse savedBooking = bookingApi.getBooking(createdBooking.getId());

        assertThat(savedBooking.getId())
                .as("The booking id is incorrect.").isEqualTo(createdBooking.getId());
        assertThat(savedBooking.getStatus())
                .as("The booking status is incorrect.").isEqualTo("CONFIRMED");

        Allure.step("Check number of available seats");
        TripResponse tripAfterBooking =
                tripApi.getTrip(createdTrip.getId());
        assertThat(tripAfterBooking.getAvailableSeats())
                .as("The number of available seats has not changed.")
                .isEqualTo(7);
    }
}