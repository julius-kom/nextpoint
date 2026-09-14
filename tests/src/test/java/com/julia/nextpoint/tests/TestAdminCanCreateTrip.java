package com.julia.nextpoint.tests;

import com.julia.nextpoint.api.TripApi;
import com.julia.nextpoint.auth.AuthClient;
import com.julia.nextpoint.config.BaseTest;
import com.julia.nextpoint.models.TripRequest;
import com.julia.nextpoint.models.TripResponse;
import io.qameta.allure.Allure;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

public class TestAdminCanCreateTrip extends BaseTest {

    private final TripApi tripApi = new TripApi();
    private final AuthClient authClient = new AuthClient();

    @Test
    void testAdminCanCreateTrip() {

        Allure.step("Get ADMIN access token");
        String token = authClient.getAdminToken();

        TripRequest request = new TripRequest(
                "Belgrade",
                "Budapest",
                60,
                LocalDate.now().plusDays(30).toString(),
                10
        );

        Allure.step("Create trip as ADMIN");
        TripResponse trip = tripApi.createTrip(request, token);

        Allure.step("Check created trip");
        assertThat(trip.getId())
                .as("Created trip id is not correct")
                .isNotNull();
    }
}