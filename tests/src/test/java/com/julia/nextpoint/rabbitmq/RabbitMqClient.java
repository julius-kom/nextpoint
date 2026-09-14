package com.julia.nextpoint.rabbitmq;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.GetResponse;

import java.nio.charset.StandardCharsets;

public class RabbitMqClient {

    private static final String QUEUE_NAME = "booking.created";

    private final ConnectionFactory factory;

    public RabbitMqClient() {
        factory = new ConnectionFactory();
        factory.setHost("localhost");
        factory.setPort(5672);
        factory.setUsername("guest");
        factory.setPassword("guest");
    }

    public String getBookingCreatedMessage() {
        try (Connection connection = factory.newConnection();
             Channel channel = connection.createChannel()) {
            GetResponse response = channel.basicGet(QUEUE_NAME, true);
            if (response == null) {
                throw new AssertionError(
                        "No message found in queue " + QUEUE_NAME
                );
            }
            return new String(
                    response.getBody(),
                    StandardCharsets.UTF_8
            );
        } catch (Exception e) {
            throw new RuntimeException(
                    "Failed to read message from RabbitMQ", e
            );
        }
    }

    public void clearBookingCreatedQueue() {
        try (Connection connection = factory.newConnection();
             Channel channel = connection.createChannel()) {

            channel.queueDeclare(
                    QUEUE_NAME,
                    true,
                    false,
                    false,
                    null
            );

            channel.queuePurge(QUEUE_NAME);

        } catch (Exception e) {
            throw new RuntimeException("Failed to clear RabbitMQ queue", e);
        }
    }

    public boolean hasBookingCreatedMessage() {
        try (Connection connection = factory.newConnection();
             Channel channel = connection.createChannel()) {
            GetResponse response = channel.basicGet(QUEUE_NAME, true);
            return response != null;
        } catch (Exception e) {
            throw new RuntimeException(
                    "Failed to read message from RabbitMQ", e
            );
        }
    }
}