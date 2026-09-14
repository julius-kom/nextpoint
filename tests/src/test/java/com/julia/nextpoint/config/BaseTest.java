package com.julia.nextpoint.config;

import com.julia.nextpoint.db.DatabaseClient;
import com.julia.nextpoint.rabbitmq.RabbitMqClient;
import com.julia.nextpoint.wiremock.PaymentStub;
import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeAll;
import io.qameta.allure.restassured.AllureRestAssured;
import org.junit.jupiter.api.BeforeEach;

public class BaseTest {
    protected final PaymentStub paymentStub = new PaymentStub();
    protected final RabbitMqClient rabbitMqClient = new RabbitMqClient();

    @BeforeAll
    static void setUp() {
        RestAssured.baseURI = "http://localhost:8080";
        RestAssured.replaceFiltersWith(new AllureRestAssured());
    }

    @BeforeEach
    void cleanTestEnvironment() {
        paymentStub.resetStubs();
        rabbitMqClient.clearBookingCreatedQueue();
    }
}