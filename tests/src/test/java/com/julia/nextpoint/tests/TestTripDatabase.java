package com.julia.nextpoint.tests;

import com.julia.nextpoint.api.TripApi;
import com.julia.nextpoint.config.BaseTest;
import com.julia.nextpoint.db.DatabaseClient;
import com.julia.nextpoint.models.TripRequest;
import com.julia.nextpoint.models.TripResponse;
import io.qameta.allure.Allure;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

public class TestTripDatabase extends BaseTest {

    private final TripApi tripApi = new TripApi();
    private final DatabaseClient databaseClient = new DatabaseClient();

    @Test
    @DisplayName("Created trip is saved in database")
    void testTripDatabase() throws SQLException {

        String departureDate = LocalDate.now().plusDays(30).toString();

        TripRequest tripRequest = new TripRequest(
                "Belgrade",
                "Zagreb",
                60,
                departureDate,
                10
        );

        Allure.step("Create trip through API");
        TripResponse createdTrip = tripApi.createTrip(tripRequest);

        Allure.step("Get created trip directly from database");
        TripResponse tripFromDb = databaseClient.getTripById(createdTrip.getId());

        Allure.step("Check trip data in database");
        assertThat(tripFromDb).as("Trip was not found in database.").isNotNull();

        assertThat(tripFromDb.getId()).as("Trip id in database is incorrect.")
                .isEqualTo(createdTrip.getId());

        assertThat(tripFromDb.getFrom())
                .as("Departure city in database is incorrect.")
                .isEqualTo(createdTrip.getFrom());

        assertThat(tripFromDb.getTo())
                .as("Destination city in database is incorrect.")
                .isEqualTo(createdTrip.getTo());

        assertThat(tripFromDb.getPrice())
                .as("Trip price in database is incorrect.")
                .isEqualTo(createdTrip.getPrice());

        assertThat(tripFromDb.getDepartureDate())
                .as("Departure date in database is incorrect.")
                .isEqualTo(createdTrip.getDepartureDate());

        assertThat(tripFromDb.getAvailableSeats())
                .as("Available seats in database are incorrect.")
                .isEqualTo(createdTrip.getAvailableSeats());
    }
}