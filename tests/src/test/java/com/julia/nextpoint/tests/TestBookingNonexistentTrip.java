package com.julia.nextpoint.tests;

import com.julia.nextpoint.api.BookingApi;
import com.julia.nextpoint.config.BaseTest;
import com.julia.nextpoint.models.BookingRequest;
import io.qameta.allure.Allure;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class TestBookingNonexistentTrip extends BaseTest {

    private final BookingApi bookingApi = new BookingApi();

    @Test
    @DisplayName("Booking fails when trip does not exist")
    void testBookingNonexistentTrip() {

        Allure.step("Create booking data");
        BookingRequest bookingRequest =
                new BookingRequest(999999L, 2);

        Allure.step("Try to book nonexistent trip");
        Response response =
                bookingApi.tryCreateBooking(bookingRequest);

        Allure.step("Check booking error");
        assertThat(response.statusCode())
                .as("Status code for booking a nonexistent trip")
                .isEqualTo(404);
    }
}