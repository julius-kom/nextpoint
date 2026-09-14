package com.julia.nextpoint.tests;

import com.julia.nextpoint.api.TripApi;
import com.julia.nextpoint.auth.AuthClient;
import com.julia.nextpoint.config.BaseTest;
import com.julia.nextpoint.models.TripRequest;
import io.qameta.allure.Allure;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

public class TestTravelerCannotCreateTrip extends BaseTest {

    private final TripApi tripApi = new TripApi();
    private final AuthClient authClient = new AuthClient();

    @Test
    void testTravelerCannotCreateTrip() {

        Allure.step("Get TRAVELER access token");
        String token = authClient.getTravelerToken();

        TripRequest request = new TripRequest(
                "Belgrade",
                "Budapest",
                60,
                LocalDate.now().plusDays(30).toString(),
                10
        );

        Allure.step("Try to create trip as TRAVELER");
        Response response = tripApi.tryCreateTrip(request, token);

        Allure.step("Check forbidden response");
        assertThat(response.statusCode())
                .as("Status code is not correct")
                .isEqualTo(403);
    }
}