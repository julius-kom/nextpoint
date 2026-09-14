package com.julia.nextpoint.model;

import jakarta.persistence.*;

@Entity
public class Booking {

    public Booking(Long tripId, int travelers, int totalPrice) {
        this.tripId = tripId;
        this.travelers = travelers;
        this.totalPrice = totalPrice;
    }

    @Enumerated(EnumType.STRING)
    private BookingStatus status;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long tripId;

    private int travelers;

    private int totalPrice;

    public Booking() {
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

    public void setTotalPrice(int totalPrice) {
        this.totalPrice = totalPrice;
    }

    public BookingStatus getStatus() {
        return status;
    }

    public void setStatus(BookingStatus status) {
        this.status = status;
    }
}