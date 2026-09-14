package com.julia.nextpoint.api;

import com.julia.nextpoint.auth.AuthClient;
import com.julia.nextpoint.models.BookingRequest;
import com.julia.nextpoint.models.BookingResponse;
import io.qameta.allure.Step;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class BookingApi {

    private final AuthClient authClient = new AuthClient();

    @Step("Create booking")
    public BookingResponse createBooking(BookingRequest request) {

        String token = authClient.getTravelerToken();

        return given()
                .auth().oauth2(token)
                .contentType(ContentType.JSON)
                .body(request)
                .when()
                .post("/api/bookings")
                .then()
                .statusCode(201)
                .extract()
                .as(BookingResponse.class);
    }

    @Step("Try to create booking")
    public Response tryCreateBooking(BookingRequest request) {

        String token = authClient.getTravelerToken();

        return given()
                .auth().oauth2(token)
                .contentType(ContentType.JSON)
                .body(request)
                .when()
                .post("/api/bookings")
                .then()
                .extract()
                .response();
    }


    @Step("Get booking with id={bookingId}")
    public BookingResponse getBooking(Long bookingId) {

        String token = authClient.getTravelerToken();

        return given()
                .auth().oauth2(token)
                .when()
                .get("/api/bookings/{id}", bookingId)
                .then()
                .statusCode(200)
                .extract()
                .as(BookingResponse.class);
    }

    @Step("Cancel booking with id={bookingId}")
    public BookingResponse cancelBooking(Long bookingId) {

        String token = authClient.getTravelerToken();

        return given()
                .auth().oauth2(token)
                .when()
                .patch("/api/bookings/{id}/cancel", bookingId)
                .then()
                .statusCode(200)
                .extract()
                .as(BookingResponse.class);
    }

    @Step("Try to cancel booking with id={bookingId}")
    public Response tryCancelBooking(Long bookingId) {

        String token = authClient.getTravelerToken();

        return given()
                .auth().oauth2(token)
                .when()
                .patch("/api/bookings/{id}/cancel", bookingId)
                .then()
                .extract()
                .response();
    }
}
