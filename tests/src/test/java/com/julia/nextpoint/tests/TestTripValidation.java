package com.julia.nextpoint.tests;

import com.julia.nextpoint.api.TripApi;
import com.julia.nextpoint.config.BaseTest;
import com.julia.nextpoint.models.TripRequest;
import io.qameta.allure.Allure;
import io.restassured.response.Response;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.time.LocalDate;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

public class TestTripValidation extends BaseTest {

    private final TripApi tripApi = new TripApi();

    @ParameterizedTest(name = "{0}")
    @MethodSource("invalidTripData")
    void testTripValidation(String testName, TripRequest tripRequest) {

        Allure.step("Try to create trip with invalid data");
        Response response = tripApi.tryCreateTrip(tripRequest);

        Allure.step("Check trip validation error");
        assertThat(response.statusCode())
                .as("Status code for trip with invalid data")
                .isEqualTo(400);
    }

    static Stream<Arguments> invalidTripData() {
        String futureDate = LocalDate.now().plusDays(30).toString();
        String pastDate = LocalDate.now().minusDays(1).toString();

        return Stream.of(
                Arguments.of(
                        "Empty departure city",
                        new TripRequest("", "Zagreb", 60, futureDate, 10)
                ),
                Arguments.of(
                        "Empty destination city",
                        new TripRequest("Belgrade", "", 60, futureDate, 10)
                ),
                Arguments.of(
                        "Zero price",
                        new TripRequest("Belgrade", "Zagreb", 0, futureDate, 10)
                ),
                Arguments.of(
                        "Past departure date",
                        new TripRequest("Belgrade", "Zagreb", 60, pastDate, 10)
                ),
                Arguments.of(
                        "Negative available seats",
                        new TripRequest("Belgrade", "Zagreb", 60, futureDate, -1)
                )
        );
    }
}