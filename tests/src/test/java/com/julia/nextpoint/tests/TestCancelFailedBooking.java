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
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;


public class TestCancelFailedBooking extends BaseTest {

    private final TripApi tripApi = new TripApi();
    private final BookingApi bookingApi = new BookingApi();
    private final PaymentStub paymentStub = new PaymentStub();

    @Test
    @DisplayName("Cannot cancel booking with failed payment")
    void testCancelFailedBooking() {

        paymentStub.stubDeclinedPayment();

        TripRequest tripRequest = new TripRequest(
                "Belgrade",
                "Budapest",
                50,
                LocalDate.now().plusDays(30).toString(),
                10
        );

        Allure.step("Create trip");
        TripResponse trip = tripApi.createTrip(tripRequest);

        Allure.step("Create booking with declined payment");
        BookingResponse booking = bookingApi.createBooking(
                new BookingRequest(trip.getId(), 3)
        );

        assertThat(booking.getStatus()).isEqualTo("PAYMENT_FAILED");

        Allure.step("Try to cancel failed booking");
        Response response = bookingApi.tryCancelBooking(booking.getId());

        assertThat(response.statusCode()).isEqualTo(409);

        Allure.step("Check available seats");
        TripResponse tripAfterCancellationAttempt =
                tripApi.getTrip(trip.getId());

        assertThat(tripAfterCancellationAttempt.getAvailableSeats()).isEqualTo(10);
    }
}
