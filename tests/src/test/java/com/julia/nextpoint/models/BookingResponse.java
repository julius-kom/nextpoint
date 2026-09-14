package com.julia.nextpoint.models;

import java.util.Objects;

public class BookingResponse {

    private Long id;
    private Long tripId;
    private int travelers;
    private int totalPrice;
    private String status;

    public BookingResponse() {
    }

    public BookingResponse(Long id, Long tripId, int travelers,
                           int totalPrice, String status) {
        this.id = id;
        this.tripId = tripId;
        this.travelers = travelers;
        this.totalPrice = totalPrice;
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public Long getTripId() {
        return tripId;
    }

    public int getTravelers() {
        return travelers;
    }

    public int getTotalPrice() {
        return totalPrice;
    }

    public String getStatus() {
        return status;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        BookingResponse that = (BookingResponse) o;
        return travelers == that.travelers && totalPrice == that.totalPrice && Objects.equals(id, that.id) && Objects.equals(tripId, that.tripId) && Objects.equals(status, that.status);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, tripId, travelers, totalPrice, status);
    }

    @Override
    public String toString() {
        return "BookingResponse{" +
                "id=" + id +
                ", tripId=" + tripId +
                ", travelers=" + travelers +
                ", totalPrice=" + totalPrice +
                ", status='" + status + '\'' +
                '}';
    }
}