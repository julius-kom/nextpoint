package com.julia.nextpoint.tests;

import com.julia.nextpoint.api.BookingApi;
import com.julia.nextpoint.api.TripApi;
import com.julia.nextpoint.config.BaseTest;
import com.julia.nextpoint.models.BookingRequest;
import com.julia.nextpoint.models.BookingResponse;
import com.julia.nextpoint.models.TripRequest;
import com.julia.nextpoint.models.TripResponse;
import com.julia.nextpoint.rabbitmq.RabbitMqClient;
import com.julia.nextpoint.wiremock.PaymentStub;
import io.qameta.allure.Allure;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

public class TestNoBookingCreatedEventAfterFailedPayment extends BaseTest {

    private final TripApi tripApi = new TripApi();
    private final BookingApi bookingApi = new BookingApi();
    private final PaymentStub paymentStub = new PaymentStub();
    private final RabbitMqClient rabbitMqClient = new RabbitMqClient();

    @Test
    void testNoBookingCreatedEventAfterFailedPayment() {

        paymentStub.stubDeclinedPayment();

        TripRequest tripRequest = new TripRequest(
                "Belgrade",
                "Budapest",
                60,
                LocalDate.now().plusDays(30).toString(),
                10
        );

        Allure.step("Create trip");
        TripResponse trip = tripApi.createTrip(tripRequest);

        Allure.step("Create booking with declined payment");
        BookingResponse booking = bookingApi.createBooking(
                new BookingRequest(trip.getId(), 3)
        );

        Allure.step("Check booking status");
        assertThat(booking.getStatus())
                .as("Booking status")
                .isEqualTo("PAYMENT_FAILED");

        Allure.step("Check that BOOKING_CREATED event was not published");
        assertThat(rabbitMqClient.hasBookingCreatedMessage())
                .as("BOOKING_CREATED event must not be published after failed payment")
                .isFalse();
    }
}