package com.julia.nextpoint.tests;

import com.julia.nextpoint.api.TripApi;
import com.julia.nextpoint.config.BaseTest;
import com.julia.nextpoint.models.TripRequest;
import com.julia.nextpoint.models.TripResponse;
import io.qameta.allure.Allure;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class TestGetTrips extends BaseTest {

    private final TripApi tripApi = new TripApi();

    @Test
    @DisplayName("Get list of trips")
    void testGetTrips() {

        Allure.step("Create trip data");
        TripRequest tripRequest = new TripRequest(
                "Belgrade",
                "Vienna",
                70,
                LocalDate.now().plusDays(30).toString(),
                10
        );

        TripResponse createdTrip = tripApi.createTrip(tripRequest);

        Allure.step("Get list of trips");
        List<TripResponse> trips = tripApi.getTrips();

        Allure.step("Check created trip is present in the list");
        assertThat(trips)
                .as("Created trip is not present in the trips list.")
                .extracting(TripResponse::getId)
                .contains(createdTrip.getId());
    }
}