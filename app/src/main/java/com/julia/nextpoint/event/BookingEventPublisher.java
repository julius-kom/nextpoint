package com.julia.nextpoint.event;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
public class BookingEventPublisher {

    private static final String QUEUE_NAME = "booking.created";

    private final RabbitTemplate rabbitTemplate;

    public BookingEventPublisher(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void publishBookingCreated(BookingCreatedEvent event) {
        rabbitTemplate.convertAndSend(QUEUE_NAME, event);
    }
}