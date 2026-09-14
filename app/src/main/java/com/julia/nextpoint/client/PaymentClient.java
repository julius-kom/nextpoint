package com.julia.nextpoint.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientException;


@Component
public class PaymentClient {


    private final RestClient restClient;

    public PaymentClient(@Value("${payment.service.url}") String paymentServiceUrl) {
        SimpleClientHttpRequestFactory requestFactory =
                new SimpleClientHttpRequestFactory();

        requestFactory.setConnectTimeout(2000);
        requestFactory.setReadTimeout(2000);

        this.restClient = RestClient.builder()
                .baseUrl(paymentServiceUrl)
                .requestFactory(requestFactory)
                .build();
    }

    public boolean pay(Long bookingId, int amount) {

        PaymentRequest request =
                new PaymentRequest(bookingId, amount);

        try {
            PaymentResponse response = restClient.post()
                    .uri("/payments")
                    .body(request)
                    .retrieve()
                    .body(PaymentResponse.class);

            return response != null && response.success();

        } catch (RestClientResponseException e) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_GATEWAY,
                    "Payment service error"
            );

        } catch (ResourceAccessException e) {
            throw new ResponseStatusException(
                    HttpStatus.GATEWAY_TIMEOUT,
                    "Payment service timeout"
            );

        } catch (RestClientException e) {
            throw new ResponseStatusException(
                    HttpStatus.GATEWAY_TIMEOUT,
                    "Payment service timeout"
            );
        }
    }

    public record PaymentRequest(Long bookingId, int amount) {
    }

    public record PaymentResponse(boolean success) {
    }
}