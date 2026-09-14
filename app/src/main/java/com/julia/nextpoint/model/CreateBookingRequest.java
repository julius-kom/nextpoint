package com.julia.nextpoint.model;

public class CreateBookingRequest {

    private Long tripId;
    private int travelers;

    public Long getTripId() {
        return tripId;
    }

    public int getTravelers() {
        return travelers;
    }
}