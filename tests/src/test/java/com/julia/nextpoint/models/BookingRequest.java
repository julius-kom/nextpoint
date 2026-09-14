package com.julia.nextpoint.models;

import java.util.Objects;

public class BookingRequest {

    private Long tripId;
    private int travelers;

    public BookingRequest(Long tripId, int travelers) {
        this.tripId = tripId;
        this.travelers = travelers;
    }

    public Long getTripId() {
        return tripId;
    }

    public int getTravelers() {
        return travelers;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        BookingRequest that = (BookingRequest) o;
        return travelers == that.travelers && Objects.equals(tripId, that.tripId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(tripId, travelers);
    }

    @Override
    public String toString() {
        return "BookingRequest{" +
                "tripId=" + tripId +
                ", travelers=" + travelers +
                '}';
    }
}