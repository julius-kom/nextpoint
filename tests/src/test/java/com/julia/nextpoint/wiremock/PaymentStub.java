package com.julia.nextpoint.wiremock;

import io.qameta.allure.Step;

import static com.github.tomakehurst.wiremock.client.WireMock.*;

public class PaymentStub {

    public PaymentStub() {
        configureFor("localhost", 8081);
    }

    @Step("Reset payment stubs")
    public void resetStubs() {
        reset();
    }

    @Step("Stub successful payment")
    public void stubSuccessfulPayment() {
        stubFor(post(urlEqualTo("/payments"))
                .willReturn(okJson("""
                        {
                          "success": true
                        }
                        """)));
    }

    @Step("Stub declined payment")
    public void stubDeclinedPayment() {
        stubFor(post(urlEqualTo("/payments"))
                .willReturn(okJson("""
                    {
                      "success": false
                    }
                    """)));
    }

    @Step("Stub payment service error")
    public void stubPaymentServiceError() {
        stubFor(post(urlEqualTo("/payments"))
                .willReturn(serverError()));
    }

    @Step("Stub payment service timeout")
    public void stubPaymentTimeout() {
        stubFor(post(urlEqualTo("/payments"))
                .willReturn(okJson("""
                    {
                      "success": true
                    }
                    """)
                        .withFixedDelay(5000)));
    }

    @Step("Verify payment request: bookingId={bookingId}, amount={amount}")
    public void verifyPaymentRequest(Long bookingId, int amount) {

        verify(postRequestedFor(urlEqualTo("/payments"))
                .withRequestBody(equalToJson("""
                    {
                      "bookingId": %d,
                      "amount": %d
                    }
                    """.formatted(bookingId, amount))));
    }

    @Step("Verify payment request with amount={amount}")
    public void verifyPaymentRequest(int amount) {
        verify(1, postRequestedFor(urlEqualTo("/payments"))
                .withRequestBody(matchingJsonPath("$.amount", equalTo(String.valueOf(amount)))));
    }
}