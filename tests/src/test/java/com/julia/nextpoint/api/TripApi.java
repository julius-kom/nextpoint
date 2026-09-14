package com.julia.nextpoint.api;

import com.julia.nextpoint.auth.AuthClient;
import com.julia.nextpoint.models.TripRequest;
import com.julia.nextpoint.models.TripResponse;
import io.qameta.allure.Step;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

import java.util.List;

import static io.restassured.RestAssured.given;

public class TripApi {

    private final AuthClient authClient = new AuthClient();

    @Step("Create trip")
    public TripResponse createTrip(TripRequest request) {

        String token = authClient.getAdminToken();

        return given()
                .auth().oauth2(token)
                .contentType(ContentType.JSON)
                .body(request)
                .when()
                .post("/api/trips")
                .then()
                .statusCode(201)
                .extract()
                .as(TripResponse.class);
    }

    @Step("Get trip with id={tripId}")
    public TripResponse getTrip(Long tripId) {

        String token = authClient.getTravelerToken();

        return given()
                .auth().oauth2(token)
                .when()
                .get("/api/trips/{id}", tripId)
                .then()
                .statusCode(200)
                .extract()
                .as(TripResponse.class);
    }

    @Step("Try to create trip")
    public Response tryCreateTrip(TripRequest request) {

        String token = authClient.getAdminToken();

        return given()
                .auth().oauth2(token)
                .contentType(ContentType.JSON)
                .body(request)
                .when()
                .post("/api/trips")
                .then()
                .extract()
                .response();
    }

    @Step("Try to create trip without access token")
    public Response tryCreateTripWithoutToken(TripRequest request) {

        return given()
                .contentType(ContentType.JSON)
                .body(request)
                .when()
                .post("/api/trips")
                .then()
                .extract()
                .response();
    }

    @Step("Update trip with id={tripId}")
    public TripResponse updateTrip(Long tripId, TripRequest request) {

        String token = authClient.getAdminToken();

        return given()
                .auth().oauth2(token)
                .contentType(ContentType.JSON)
                .body(request)
                .when()
                .put("/api/trips/{id}", tripId)
                .then()
                .statusCode(200)
                .extract()
                .as(TripResponse.class);
    }

    @Step("Get all trips")
    public List<TripResponse> getTrips() {

        String token = authClient.getTravelerToken();

        return given()
                .auth().oauth2(token)
                .when()
                .get("/api/trips")
                .then()
                .statusCode(200)
                .extract()
                .jsonPath()
                .getList("", TripResponse.class);
    }

    @Step("Create trip with access token")
    public TripResponse createTrip(TripRequest request, String token) {
        return given()
                .auth().oauth2(token)
                .contentType(ContentType.JSON)
                .body(request)
                .when()
                .post("/api/trips")
                .then()
                .statusCode(201)
                .extract()
                .as(TripResponse.class);
    }

    @Step("Try to create trip with access token")
    public Response tryCreateTrip(TripRequest request, String token) {
        return given()
                .auth().oauth2(token)
                .contentType(ContentType.JSON)
                .body(request)
                .when()
                .post("/api/trips")
                .then()
                .extract()
                .response();
    }
}