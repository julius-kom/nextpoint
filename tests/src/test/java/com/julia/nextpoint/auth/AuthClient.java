package com.julia.nextpoint.auth;

import com.julia.nextpoint.config.TestConfig;
import io.qameta.allure.Step;

import static io.restassured.RestAssured.given;

public class AuthClient {

    private final String tokenUrl =
            TestConfig.KEYCLOAK_URL
                    + "/realms/"
                    + TestConfig.KEYCLOAK_REALM
                    + "/protocol/openid-connect/token";

    @Step("Get ADMIN access token")
    public String getAdminToken() {
        return getAccessToken(
                TestConfig.ADMIN_USERNAME,
                TestConfig.ADMIN_PASSWORD
        );
    }

    @Step("Get TRAVELER access token")
    public String getTravelerToken() {
        return getAccessToken(
                TestConfig.TRAVELER_USERNAME,
                TestConfig.TRAVELER_PASSWORD
        );
    }

    private String getAccessToken(String username, String password) {
        return given()
                .noFilters()
                .contentType("application/x-www-form-urlencoded")
                .formParam("client_id", TestConfig.KEYCLOAK_CLIENT)
                .formParam("username", username)
                .formParam("password", password)
                .formParam("grant_type", "password")
                .when()
                .post(tokenUrl)
                .then()
                .statusCode(200)
                .extract()
                .jsonPath()
                .getString("access_token");
    }
}