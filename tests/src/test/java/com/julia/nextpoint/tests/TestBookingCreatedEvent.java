package com.julia.nextpoint.tests;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
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

public class TestBookingCreatedEvent extends BaseTest {

    private final TripApi tripApi = new TripApi();
    private final BookingApi bookingApi = new BookingApi();
    private final PaymentStub paymentStub = new PaymentStub();
    private final RabbitMqClient rabbitMqClient = new RabbitMqClient();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void testBookingCreatedEvent() throws Exception {

        paymentStub.stubSuccessfulPayment();

        String departureDate = LocalDate.now().plusDays(30).toString();

        TripRequest tripRequest = new TripRequest(
                "Belgrade",
                "Budapest",
                60,
                departureDate,
                10
        );

        Allure.step("Create trip");
        TripResponse trip = tripApi.createTrip(tripRequest);

        Allure.step("Create booking");
        BookingResponse booking = bookingApi.createBooking(
                new BookingRequest(trip.getId(), 3)
        );

        Allure.step("Read BOOKING_CREATED event from RabbitMQ");
        String message = rabbitMqClient.getBookingCreatedMessage();

        JsonNode event = objectMapper.readTree(message);

        Allure.step("Check BOOKING_CREATED event data");
        assertThat(event.get("bookingId").asLong())
                .as("Booking id in event")
                .isEqualTo(booking.getId());

        assertThat(event.get("tripId").asLong())
                .as("Trip id in event")
                .isEqualTo(trip.getId());

        assertThat(event.get("travelers").asInt())
                .as("Number of travelers in event")
                .isEqualTo(3);

        assertThat(event.get("totalPrice").asInt())
                .as("Total price in event")
                .isEqualTo(180);
    }
}