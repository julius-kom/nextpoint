package com.julia.nextpoint.tests;

import com.julia.nextpoint.config.BaseTest;

import com.julia.nextpoint.api.BookingApi;
import com.julia.nextpoint.api.TripApi;
import com.julia.nextpoint.models.BookingRequest;
import com.julia.nextpoint.models.TripRequest;
import com.julia.nextpoint.models.TripResponse;
import com.julia.nextpoint.wiremock.PaymentStub;
import io.qameta.allure.Allure;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

public class TestPaymentTimeout  extends BaseTest {

    private final TripApi tripApi = new TripApi();
    private final BookingApi bookingApi = new BookingApi();
    private final PaymentStub paymentStub = new PaymentStub();

    @Test
    @DisplayName("Return 504 when Payment Service times out")
    void testPaymentTimeout() {

        Allure.step("Configure Payment Service timeout");
        paymentStub.stubPaymentTimeout();

        String departureDate = LocalDate.now().plusDays(30).toString();

        TripRequest tripRequest = new TripRequest(
                "Belgrade",
                "Budapest",
                70,
                departureDate,
                10
        );

        Allure.step("Create trip");
        TripResponse createdTrip = tripApi.createTrip(tripRequest);

        BookingRequest bookingRequest = new BookingRequest(createdTrip.getId(), 2);

        Allure.step("Try to create booking");
        Response response = bookingApi.tryCreateBooking(bookingRequest);

        Allure.step("Check response status");
        assertThat(response.statusCode())
                .as("The response status code is incorrect.")
                .isEqualTo(504);

        Allure.step("Check number of available seats");
        TripResponse tripAfterBooking =
                tripApi.getTrip(createdTrip.getId());

        assertThat(tripAfterBooking.getAvailableSeats())
                .as("The number of available seats has changed.")
                .isEqualTo(10);

        Allure.step("Verify request sent to Payment Service");
        paymentStub.verifyPaymentRequest(140);
    }
}