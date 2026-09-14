package com.julia.nextpoint.event;

public record BookingCreatedEvent(
        Long bookingId,
        Long tripId,
        int travelers,
        int totalPrice
) {
}