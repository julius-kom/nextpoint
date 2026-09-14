package com.julia.nextpoint.tests;

import com.julia.nextpoint.api.TripApi;
import com.julia.nextpoint.config.BaseTest;
import com.julia.nextpoint.models.TripRequest;
import com.julia.nextpoint.models.TripResponse;
import io.qameta.allure.Allure;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

public class TestUpdateTrip extends BaseTest {

    private final TripApi tripApi = new TripApi();

    @Test
    @DisplayName("Update existing trip")
    void testUpdateTrip() {

        String departureDate = LocalDate.now().plusDays(30).toString();

        Allure.step("Create trip data");
        TripRequest tripRequest = new TripRequest(
                "Belgrade",
                "Zagreb",
                60,
                departureDate,
                10
        );

        TripResponse createdTrip = tripApi.createTrip(tripRequest);

        Allure.step("Prepare updated trip data");
        String updatedDepartureDate = LocalDate.now().plusDays(40).toString();

        TripRequest updatedTripRequest = new TripRequest(
                "Belgrade",
                "Budapest",
                80,
                updatedDepartureDate,
                15
        );

        Allure.step("Update trip");
        tripApi.updateTrip(
                createdTrip.getId(),
                updatedTripRequest
        );

        Allure.step("Check saved updated trip data");
        TripResponse savedTrip = tripApi.getTrip(createdTrip.getId());

        assertThat(savedTrip.getFrom())
                .as("The departure city is incorrect.")
                .isEqualTo("Belgrade");
        assertThat(savedTrip.getTo())
                .as("The destination city was not updated.")
                .isEqualTo("Budapest");
        assertThat(savedTrip.getPrice())
                .as("The trip price was not updated.")
                .isEqualTo(80);
        assertThat(savedTrip.getDepartureDate())
                .as("The departure date was not updated.")
                .isEqualTo(updatedDepartureDate);
        assertThat(savedTrip.getAvailableSeats())
                .as("The number of available seats was not updated.")
                .isEqualTo(15);
    }
}